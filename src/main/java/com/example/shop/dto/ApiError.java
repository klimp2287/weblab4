package com.example.shop.dto;

import java.util.Map;

// Единый формат ошибок API
public record ApiError(int status, String message, Map<String, String> errors) {
}