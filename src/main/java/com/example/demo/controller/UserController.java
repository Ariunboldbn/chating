package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User's API", description = "users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Get all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get logged in user")
    @ApiResponse(responseCode = "200", description = "Get logged in user")
    @GetMapping("/me")
    public User getLoggedUser(@RequestParam String username) {
        return userService.getLoggedUser(username);
    }

    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "200", description = "Get user by id")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "200", description = "Create user")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "200", description = "Delete user")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
