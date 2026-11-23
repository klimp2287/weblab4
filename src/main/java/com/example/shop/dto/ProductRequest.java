package com.example.shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Название товара обязательно")
        @Size(max = 200, message = "Не более 200 символов")
        String name,

        @NotNull(message = "Цена обязательна")
        @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
        BigDecimal price,

        @Min(value = 0, message = "Количество не может быть отрицательным")
        int quantity,

        @NotNull(message = "Категория обязательна")
        Long categoryId
) {
}