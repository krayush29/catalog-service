package com.example.catalog_service.controller;

import com.example.catalog_service.dto.request.UserRegisterRequest;
import com.example.catalog_service.dto.request.UserRequest;
import com.example.catalog_service.dto.response.UserResponse;
import com.example.catalog_service.enums.Role;
import com.example.catalog_service.exception.DuplicateUsernameException;
import com.example.catalog_service.exception.InvalidPasswordException;
import com.example.catalog_service.exception.UserNotFoundException;
import com.example.catalog_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest("test_user", "password", Role.ADMIN.toString());
        UserResponse userResponse = new UserResponse("test_user", Role.ADMIN);

        when(userService.createUser(any(UserRegisterRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    void testExceptionDuplicateUsername() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest("test_user", "password", Role.CUSTOMER.toString());

        doThrow(new DuplicateUsernameException("Username already exists")).when(userService).createUser(any(UserRegisterRequest.class));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetUser() throws Exception {
        UserRequest userRequest = new UserRequest("test_user", "password");
        UserResponse userResponse = new UserResponse("test_user", Role.CUSTOMER);

        when(userService.getUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    void testExceptionUserNotFound() throws Exception {
        UserRequest userRequest = new UserRequest("test_user", "password");

        doThrow(new UserNotFoundException("User not found")).when(userService).getUser(any(UserRequest.class));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testExceptionInvalidPassword() throws Exception {
        UserRequest userRequest = new UserRequest("test_user", "wrong_password");

        doThrow(new InvalidPasswordException("Invalid password")).when(userService).getUser(any(UserRequest.class));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isUnauthorized());
    }
}