package com.example.shop.controller;

import com.example.shop.dto.CategoryStatResponse;
import com.example.shop.service.CategoryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final CategoryService categoryService;

    public StatsController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryStatResponse> getStats() {
        return categoryService.getStats();
    }
}