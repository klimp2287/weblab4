package com.example.shop.controller;

import com.example.shop.dto.UserResponse;
import com.example.shop.repository.UserRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(
                        u.getId(), u.getUsername(), u.getRole().name()))
                .toList();
    }
}