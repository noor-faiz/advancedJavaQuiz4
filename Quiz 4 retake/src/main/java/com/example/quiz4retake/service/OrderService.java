package com.example.quiz4retake.service;

import com.example.quiz4retake.model.Order;
import com.example.quiz4retake.repository.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = {"orders", "dashboard"}, allEntries = true)
    public Order create(String customerName, String productName, int quantity, double price, String status) {

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setProductName(productName);
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setStatus(status);
        order.setLocalDateTime(LocalDateTime.now());

        return repository.save(order);
    }

    @CacheEvict(value = {"orders", "dashboard"}, allEntries = true)
    public Order update(String id, String customerName, String productName, int quantity, double price, String status) {

        Order order = repository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }

        order.setCustomerName(customerName);
        order.setProductName(productName);
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setStatus(status);

        return repository.save(order);
    }

    @CacheEvict(value = {"orders", "dashboard"}, allEntries = true)
    public String delete(String id) {
        Order order = repository.findById(id).orElse(null);
        if (order == null) {
            return "Not Found";
        }
        repository.delete(order);
        return "Deleted";
    }

    @Cacheable(value = "orders")
    public List<Order> getAll() {
        return repository.findAll();
    }

    public Order getByCustomer(String customer) {
        return repository.findByCustomerName(customer).orElse(null);
    }

    public Order getByProduct(String product) {
        return repository.findByProductName(product).orElse(null);
    }

    public List<Order> search(String q) {
        List<Order> all = getAll();
        if (q == null || q.trim().isEmpty()) {
            return all;
        }

        String query = q.toLowerCase();
        all.removeIf(o ->
                (o.getCustomerName() == null || !o.getCustomerName().toLowerCase().contains(query)) &&
                        (o.getProductName() == null || !o.getProductName().toLowerCase().contains(query)) &&
                        (o.getStatus() == null || !o.getStatus().toLowerCase().contains(query))
        );

        return all;
    }

    @Cacheable(value = "dashboard", key = "'orderCountToday'")
    public int orderCountToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<Order> list = repository.findByLocalDateTimeBetween(start, end);
        int count = 0;
        for (Order order : list) {
            count = count + order.getQuantity();
        }
        return count;
    }

    @Cacheable(value = "dashboard", key = "'pendingCountToday'")
    public int pendingCountToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<Order> list = repository.findByStatusAndLocalDateTimeBetween("Pending", start, end);
        int count = 0;
        for (Order order : list) {
            count = count + order.getQuantity();
        }
        return count;
    }

    @Cacheable(value = "dashboard", key = "'revenueThisMonth'")
    public double revenueThisMonth() {

        LocalDateTime start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1);

        List<Order> list = repository.findByLocalDateTimeBetween(start, end);
        double total = 0.0;
        for (Order order : list) {
            total = total + order.getPrice();
        }

        return total;
    }
}

