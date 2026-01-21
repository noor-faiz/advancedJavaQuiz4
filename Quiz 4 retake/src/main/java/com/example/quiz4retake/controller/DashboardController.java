package com.example.quiz4retake.controller;

import com.example.quiz4retake.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final OrderService service;

    public DashboardController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/api/order-count")
    public int countToday() {
        return service.orderCountToday();
    }

    @GetMapping("/api/pending-count")
    public int pendingToday() {
        return service.pendingCountToday();
    }

    @GetMapping("/api/revenue")
    public double revenue() {
        return service.revenueThisMonth();
    }
}
