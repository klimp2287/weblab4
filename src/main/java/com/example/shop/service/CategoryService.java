package com.example.shop.service;

import com.example.shop.dto.CategoryRequest;
import com.example.shop.dto.CategoryResponse;
import com.example.shop.dto.CategoryStatResponse;
import com.example.shop.entity.Category;
import com.example.shop.exception.ConflictException;
import com.example.shop.exception.NotFoundException;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByTitle(request.title())) {
            throw new ConflictException("Категория «" + request.title()
                    + "» уже существует");
        }
        Category saved = categoryRepository.save(
                new Category(request.title()));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryStatResponse> getStats() {
        return productRepository.countByCategory().stream()
                .map(s -> new CategoryStatResponse(
                        s.getTitle(), s.getProductCount()))
                .toList();
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getTitle());
    }

    //8.2
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Категория с id=" + id
                    + " не найдена");
        }
        if (productRepository.existsByCategoryId(id)) {
            throw new ConflictException(
                    "Нельзя удалить категорию: в ней есть товары");
        }
        categoryRepository.deleteById(id);
    }
}