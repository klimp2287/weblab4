package com.example.shop.service;

import com.example.shop.dto.ProductRequest;
import com.example.shop.dto.ProductResponse;
import com.example.shop.entity.Category;
import com.example.shop.entity.Product;
import com.example.shop.exception.NotFoundException;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductRepository;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll(Long categoryId, String search) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (search != null && !search.isBlank()) {
            products = productRepository
                    .findByNameContainingIgnoreCase(search);
        } else {
            products = productRepository.findAll();
        }
        return products.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return toResponse(findProduct(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = findCategory(request.categoryId());
        Product product = new Product(request.name(), request.price(),
                request.quantity(), category);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProduct(id);
        product.setName(request.name());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setCategory(findCategory(request.categoryId()));
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Товар с id=" + id + " не найден");
        }
        productRepository.deleteById(id);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Товар с id=" + id + " не найден"));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Категория с id=" + id
                        + " не найдена"));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(),
                p.getQuantity(), p.getCategory().getId(),
                p.getCategory().getTitle());
    }

    //8.1
    @Transactional
    public ProductResponse updateQuantity(Long id, int quantity) {
        Product product = findProduct(id);
        product.setQuantity(quantity);
        return toResponse(productRepository.save(product));
    }

    //8.3
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll(Long categoryId, String search,
                                        int page, int size) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (search != null && !search.isBlank()) {
            products = productRepository
                    .findByNameContainingIgnoreCase(search);
        } else {
            int safePage = Math.max(page, 0);
            int safeSize = Math.min(Math.max(size, 1), 100);
            products = productRepository
                    .findAll(PageRequest.of(safePage, safeSize,
                            Sort.by("id")))
                    .getContent();
        }
        return products.stream().map(this::toResponse).toList();
    }
}