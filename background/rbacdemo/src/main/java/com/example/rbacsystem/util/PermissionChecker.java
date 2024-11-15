package com.example.rbacsystem.util;

import com.example.rbacsystem.mapper.UserMapper;
import com.example.rbacsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionChecker {

    @Autowired
    private UserMapper userMapper;

    /**
     * 检查用户是否具有指定的权限
     * @param username 用户名
     * @param requiredPermission 需要的权限
     * @return 如果用户具有该权限，返回 true；否则返回 false
     */
    public boolean hasPermission(String username, String requiredPermission) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return false;
        }

        List<String> permissions = userMapper.getUserPermissions(user.getUserId());
        return permissions.contains(requiredPermission);
    }
}
