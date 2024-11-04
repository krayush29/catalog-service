package com.example.catalog_service.service;

import com.example.catalog_service.dto.request.UserRegisterRequest;
import com.example.catalog_service.dto.request.UserRequest;
import com.example.catalog_service.dto.response.UserResponse;
import com.example.catalog_service.entity.User;
import com.example.catalog_service.exception.DuplicateUsernameException;
import com.example.catalog_service.exception.InvalidPasswordException;
import com.example.catalog_service.exception.UserNotFoundException;
import com.example.catalog_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(UserRegisterRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateUsernameException("Username already exists: " + userRequest.getUsername());
        }

        User user = new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRole());
        userRepository.save(user);
        return new UserResponse(user.getUsername(), user.getRole());
    }

    public UserResponse getUser(UserRequest userRequest) {
        User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found for username: " + userRequest.getUsername()));

        if (user.getPassword().equals(userRequest.getPassword())) {
            return new UserResponse(user.getUsername(), user.getRole());
        } else {
            throw new InvalidPasswordException("Invalid password for username: " + userRequest.getUsername());
        }
    }
}
