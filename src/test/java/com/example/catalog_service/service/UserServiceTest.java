package com.example.catalog_service.service;

import com.example.catalog_service.dto.request.UserRegisterRequest;
import com.example.catalog_service.dto.request.UserRequest;
import com.example.catalog_service.dto.response.UserResponse;
import com.example.catalog_service.entity.User;
import com.example.catalog_service.enums.Role;
import com.example.catalog_service.exception.DuplicateUsernameException;
import com.example.catalog_service.exception.InvalidPasswordException;
import com.example.catalog_service.exception.UserNotFoundException;
import com.example.catalog_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() {
        UserRegisterRequest userRequest = new UserRegisterRequest("test_user", "password", "ADMIN");
        User user = new User("test_user", "password", Role.ADMIN);

        when(userRepository.existsByUsername("test_user")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse userResponse = userService.createUser(userRequest);

        assertEquals("test_user", userResponse.getUsername());
    }

    @Test
    void testExceptionForDuplicateUsername() {
        UserRegisterRequest userRequest = new UserRegisterRequest("test_user", "password", "ADMIN");

        when(userRepository.existsByUsername("test_user")).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void testGetUserSuccess() {
        UserRequest userRequest = new UserRequest("test_user", "password");
        User user = new User("test_user", "password", Role.CUSTOMER);

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUser(userRequest);

        assertEquals("test_user", userResponse.getUsername());
        assertEquals(Role.CUSTOMER, userResponse.getRole());
    }

    @Test
    void testExceptionForUserNotFound() {
        UserRequest userRequest = new UserRequest("test_user", "password");

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(userRequest));
    }

    @Test
    void testExceptionForInvalidPassword() {
        UserRequest userRequest = new UserRequest("test_user", "wrong_password");
        User user = new User("test_user", "password", Role.OWNER);

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));

        assertThrows(InvalidPasswordException.class, () -> userService.getUser(userRequest));
    }
}