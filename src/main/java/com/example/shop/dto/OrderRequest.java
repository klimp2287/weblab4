package com.example.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull(message = "Товар обязателен")
        Long productId,

        @Min(value = 1, message = "Количество должно быть не менее 1")
        int quantity
) {
}