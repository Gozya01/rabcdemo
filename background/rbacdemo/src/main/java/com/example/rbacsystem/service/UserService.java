package com.example.rbacsystem.service;

import com.example.rbacsystem.mapper.UserMapper;
import com.example.rbacsystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    

    public User getUserById(Integer userId) {
        return userMapper.getUserById(userId);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public int createUser(User user) {
        return userMapper.insertUser(user);
    }
    public boolean validatePassword(String rawPassword, String storedPassword) {
        return rawPassword.equals(storedPassword);
    }
    // 其他业务逻辑方法
}
