package com.example.shop.repository;

import com.example.shop.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String part);

    // Статистика: количество товаров в каждой категории
    @Query(""" 
            select c.title as title, count(p) as productCount 
            from Product p join p.category c 
            group by c.title 
            """)
    List<CategoryStat> countByCategory();
    List<Product> findByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    //8.2
    boolean existsByCategoryId(Long categoryId);
}