package com.example.quiz4retake.controller;

import com.example.quiz4retake.model.Order;
import com.example.quiz4retake.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/api/orders")
    public List<Order> orders(@RequestParam(required = false) String q) {
        if (q != null && !q.isEmpty()) {
            return service.search(q);
        }
        return service.getAll();
    }

    @PostMapping("/api/orders/create")
    public Order create(@RequestParam String customerName,
                        @RequestParam String productName,
                        @RequestParam int quantity,
                        @RequestParam double price,
                        @RequestParam String status) {

        return service.create(customerName, productName, quantity, price, status);
    }

    // Only "ADMIN" can update
    @PostMapping("/api/orders/update")
    public Order update(@RequestParam String id,
                        @RequestParam String customerName,
                        @RequestParam String productName,
                        @RequestParam int quantity,
                        @RequestParam double price,
                        @RequestParam String status,
                        Authentication authentication) {

        if (authentication == null) {
            return null;
        }

        boolean isAdmin = authentication.getAuthorities().toString().contains("ROLE_ADMIN");
        if (!isAdmin) {
            return null;
        }

        return service.update(id, customerName, productName, quantity, price, status);
    }

    // Only ADMIN can delete
    @PostMapping("/api/orders/delete")
    public String delete(@RequestParam String id, Authentication authentication) {

        if (authentication == null) {
            return "Unauthorized";
        }

        boolean isAdmin = authentication.getAuthorities().toString().contains("ROLE_ADMIN");
        if (!isAdmin) {
            return "Forbidden";
        }

        return service.delete(id);
    }
}
