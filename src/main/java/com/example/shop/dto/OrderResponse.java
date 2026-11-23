package com.example.shop.dto;

import com.example.shop.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        String username,
        String productName,
        int quantity,
        BigDecimal totalPrice,
        OrderStatus status,
        Instant createdAt
) {
}