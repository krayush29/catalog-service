package com.example.catalog_service.service;

import com.example.catalog_service.dto.request.UserRegisterRequest;
import com.example.catalog_service.dto.request.UserRequest;
import com.example.catalog_service.dto.response.DeliveryPartnerResponse;
import com.example.catalog_service.dto.response.UserResponse;
import com.example.catalog_service.entity.DeliveryPartner;
import com.example.catalog_service.entity.User;
import com.example.catalog_service.enums.Role;
import com.example.catalog_service.exception.DuplicateUsernameException;
import com.example.catalog_service.exception.InvalidPasswordException;
import com.example.catalog_service.exception.UnAuthorizeRoleException;
import com.example.catalog_service.exception.UserNotFoundException;
import com.example.catalog_service.repository.DeliveryPartnerRepository;
import com.example.catalog_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    public UserResponse createUser(UserRegisterRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateUsernameException("Username already exists: " + userRequest.getUsername());
        }
        if (Role.DELIVERY_PARTNER.equals(userRequest.getRole())) {
            DeliveryPartner deliveryPartner = new DeliveryPartner(userRequest.getUsername(), userRequest.getPassword());
            deliveryPartnerRepository.save(deliveryPartner);
            return new DeliveryPartnerResponse(deliveryPartner.getId(), deliveryPartner.getUsername(), deliveryPartner.getRole(), deliveryPartner.isAvailable(), deliveryPartner.getLocation());
        }

        User user = new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRole());
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public UserResponse getUser(UserRequest userRequest) {
        User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found for username: " + userRequest.getUsername()));

        if (user.getPassword().equals(userRequest.getPassword())) {
            return new UserResponse(user.getId(), user.getUsername(), user.getRole());
        } else {
            throw new InvalidPasswordException("Invalid password for username: " + userRequest.getUsername());
        }
    }

    public void isAuthorize(String username, String password, Role role) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        if (!user.getPassword().equals(password))
            throw new InvalidPasswordException("Invalid password for username: " + username);
        if (!user.getRole().equals(role))
            throw new UnAuthorizeRoleException("UnAuthorize user: " + username);
    }
}
