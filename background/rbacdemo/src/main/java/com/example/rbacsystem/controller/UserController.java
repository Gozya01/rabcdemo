package com.example.rbacsystem.controller;

import com.example.rbacsystem.model.User;
import com.example.rbacsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/")
    public String createUser(@RequestBody User user) {
        int result = userService.createUser(user);
        return result > 0 ? "User created successfully" : "Failed to create user";
    }
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User loginUser, HttpServletRequest request) {
        User user = userService.findByUsername(loginUser.getUsername());
        //System.out.println(user.getUsername());
        Map<String, Object> response = new HashMap<>();
        if (user != null && userService.validatePassword(loginUser.getPassword(), user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);
            response.put("message", "Login successful");
        } else {
            response.put("message", "Invalid username or password");
        }
        return response;
    }

    @GetMapping("/getInfo")
    public Map<String, Object> getInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
        } else {
            response.put("message", "User not logged in");
        }
        return response;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logout successful");
        return response;
    }

    // 其他请求处理方法
}
