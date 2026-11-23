package com.example.shop.dto;

import jakarta.validation.constraints.Min;

//8.1
public record QuantityRequest(
        @Min(value = 0, message = "Количество не может быть отрицательным")
        int quantity
) {
}
