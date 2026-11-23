package com.example.shop.controller;

import com.example.shop.dto.OrderRequest;
import com.example.shop.dto.OrderResponse;
import com.example.shop.service.OrderService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {
        OrderResponse created =
                orderService.create(authentication.getName(), request);
        return ResponseEntity
                .created(URI.create("/api/orders/" + created.id()))
                .body(created);
    }

    @GetMapping
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        return orderService.getByUser(authentication.getName());
    }
}