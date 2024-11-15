package com.example.rbacsystem.interceptor;

import com.example.rbacsystem.util.JwtTokenUtil;
import com.example.rbacsystem.util.PermissionChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PermissionChecker permissionChecker;

    // 定义 URL 与权限的映射关系
    private static final HashMap<String, String> URL_PERMISSION_MAP = new HashMap<>();

    static {
        URL_PERMISSION_MAP.put("/api/users", "view_user");
        URL_PERMISSION_MAP.put("/api/users/create", "create_user");
        // 添加更多的 URL 与权限的映射
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String username = (String) request.getAttribute("username");
        if (username == null) {
            // 未登录，直接返回
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String requestURI = request.getRequestURI();
        String requiredPermission = URL_PERMISSION_MAP.get(requestURI);

        // 如果不需要特殊权限，直接放行
        if (requiredPermission == null) {
            return true;
        }

        // 检查用户是否具有所需权限
        if (!permissionChecker.hasPermission(username, requiredPermission)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        // 权限校验通过，放行
        return true;
    }
}
