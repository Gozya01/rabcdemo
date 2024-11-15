package com.example.rbacsystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // 从配置文件中读取 JWT 密钥和有效期
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_token.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh_token.expiration}")
    private Long refreshTokenExpiration;

    /**
     * 生成访问令牌
     * @param username 用户名
     * @return 生成的访问令牌
     */
    public String generateAccessToken(String username) {
        Map<String, Object> extraClaims = new HashMap<>();
        return doGenerateToken(extraClaims, username, accessTokenExpiration);
    }

    /**
     * 生成刷新令牌
     * @param username 用户名
     * @param extraClaims 额外的声明信息
     * @return 生成的刷新令牌
     */
    public String generateRefreshToken(String username, Map<String, Object> extraClaims) {
        return doGenerateToken(extraClaims, username, refreshTokenExpiration);
    }

    /**
     * 生成令牌的通用方法
     * @param claims 声明信息
     * @param subject 主题信息
     * @param expiration 过期时间（秒）
     * @return 生成的令牌
     */
    private String doGenerateToken(Map<String, Object> claims, String subject, Long expiration) {
        claims.putIfAbsent("created", new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 从令牌中提取用户名
     * @param token JWT 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从令牌中提取过期日期
     * @param token JWT 令牌
     * @return 过期日期
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从令牌中获取指定声明信息
     * @param token JWT 令牌
     * @param claimsResolver 声明解析函数
     * @return 声明信息
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 获取令牌中的所有声明信息
     * @param token JWT 令牌
     * @return 所有声明信息
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 检查令牌是否已过期
     * @param token JWT 令牌
     * @return 如果已过期返回 true，否则返回 false
     */
    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * 验证令牌是否有效
     * @param token JWT 令牌
     * @return 如果有效返回 true，否则返回 false
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            // 可以增加日志记录异常信息
            System.err.println("JWT验证失败: " + e.getMessage());
            return false;
        }
    }
}
