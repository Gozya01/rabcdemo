package com.example.rbacsystem.controller;

import com.example.rbacsystem.util.JwtTokenUtil;
import com.example.rbacsystem.LoginRequest;
import com.example.rbacsystem.model.TokenResponse;
import com.example.rbacsystem.model.User;
import com.example.rbacsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 用户登录接口
     *
     * @param loginRequest 包含用户名和密码的请求对象
     * @return 包含 JWT Token 的响应
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());
        System.out.println(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            String token = jwtTokenUtil.generateAccessToken(user.getUsername());
            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    /**
     * 获取当前登录用户信息
     *
     * @param request HTTP 请求对象，用于获取用户的 JWT Token
     * @return 当前用户的详细信息
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // 去掉 "Bearer "
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * 用户退出接口，清除 Session
     *
     * @param request HTTP 请求对象，用于清除用户的 Session
     * @return 退出成功信息
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}