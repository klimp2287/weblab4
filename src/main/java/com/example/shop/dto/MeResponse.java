package com.example.shop.dto;

import java.util.List;

public record MeResponse(String username, List<String> roles) {
}