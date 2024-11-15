package com.example.rbacsystem.mapper;

import com.example.rbacsystem.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User getUserById(@Param("userId") Integer userId);

    User findByUsername(@Param("username") String username);

    int insertUser(User user);

    // 其他需要的方法
    // 获取用户的权限列表
    List<String> getUserPermissions(@Param("userId") Integer userId);

    // 根据用户名查找用户
}
