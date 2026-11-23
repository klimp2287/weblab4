package com.example.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Название категории обязательно")
        @Size(max = 100, message = "Не более 100 символов")
        String title
) {
}