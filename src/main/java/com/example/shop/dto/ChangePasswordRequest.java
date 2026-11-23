package com.example.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//10.1
public record ChangePasswordRequest(
        @NotBlank(message = "Старый пароль обязателен")
        String oldPassword,

        @NotBlank(message = "Новый пароль обязателен")
        @Size(min = 6, max = 100, message = "От 6 до 100 символов")
        String newPassword
) {
}