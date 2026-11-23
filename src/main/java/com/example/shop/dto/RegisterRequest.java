package com.example.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Имя пользователя обязательно")
        @Size(min = 3, max = 50, message = "От 3 до 50 символов")
        String username,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 6, max = 100, message = "От 6 до 100 символов")
        String password
) {
}