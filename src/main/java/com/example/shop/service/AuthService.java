package com.example.shop.service;

import com.example.shop.dto.ChangePasswordRequest;
import com.example.shop.dto.LoginRequest;
import com.example.shop.dto.LoginResponse;
import com.example.shop.dto.RegisterRequest;
import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.exception.BadRequestException;
import com.example.shop.exception.ConflictException;
import com.example.shop.repository.UserRepository;
import com.example.shop.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException(
                    "Пользователь с таким именем уже существует");
        }
        String hash = passwordEncoder.encode(request.password());
        userRepository.save(
                new User(request.username(), hash, Role.USER));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();
        String token = jwtService.generateToken(
                user.getUsername(), user.getRole().name());

        return new LoginResponse(token, "Bearer");
    }

    //10.1
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username).orElseThrow();
        if (!passwordEncoder.matches(request.oldPassword(),
                user.getPasswordHash())) {
            throw new BadRequestException("Старый пароль указан неверно");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}