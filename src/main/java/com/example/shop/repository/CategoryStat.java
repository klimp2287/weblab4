package com.example.shop.repository;

// Проекция: Spring Data сам создаст объект с этими геттерами.
// Имена соответствуют псевдонимам (as title, as productCount) в запросе.
public interface CategoryStat {
    String getTitle();
    Long getProductCount();
}