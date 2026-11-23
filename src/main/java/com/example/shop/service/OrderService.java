package com.example.shop.service;

import com.example.shop.dto.OrderRequest;
import com.example.shop.dto.OrderResponse;
import com.example.shop.entity.Order;
import com.example.shop.entity.Product;
import com.example.shop.event.OrderCreatedEvent;
import com.example.shop.event.OrderEventProducer;
import com.example.shop.exception.ConflictException;
import com.example.shop.exception.NotFoundException;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEventProducer eventProducer;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public OrderResponse create(String username, OrderRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NotFoundException(
                        "Товар с id=" + request.productId()
                                + " не найден"));

        if (product.getQuantity() < request.quantity()) {
            throw new ConflictException(
                    "Недостаточно товара на складе: осталось "
                            + product.getQuantity());
        }
        product.setQuantity(product.getQuantity() - request.quantity());

        BigDecimal total = product.getPrice()
                .multiply(BigDecimal.valueOf(request.quantity()));
        Order order = orderRepository.save(
                new Order(username, product, request.quantity(), total));

        eventProducer.send(new OrderCreatedEvent(order.getId(), username,
                product.getName(), order.getQuantity(), total));

        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getByUser(String username) {
        return orderRepository.findByUsernameOrderByIdDesc(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order o) {
        return new OrderResponse(o.getId(), o.getUsername(),
                o.getProduct().getName(), o.getQuantity(),
                o.getTotalPrice(), o.getStatus(), o.getCreatedAt());
    }
}