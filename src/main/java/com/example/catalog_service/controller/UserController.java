package com.example.catalog_service.controller;

import com.example.catalog_service.dto.request.UserRegisterRequest;
import com.example.catalog_service.dto.request.UserRequest;
import com.example.catalog_service.dto.response.UserResponse;
import com.example.catalog_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.createUser(userRegisterRequest);
    }

    @GetMapping
    public UserResponse getUser(@RequestBody UserRequest userRequest) {
        return userService.getUser(userRequest);
    }
}
