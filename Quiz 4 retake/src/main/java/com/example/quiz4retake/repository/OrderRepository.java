package com.example.quiz4retake.repository;


import com.example.quiz4retake.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Optional<Order> findByCustomerName(String customerName);

    Optional<Order> findByProductName(String productName);

    List<Order> findByLocalDateTimeBetween(LocalDateTime from, LocalDateTime to);

    long countByLocalDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Order> findByStatusAndLocalDateTimeBetween(String status, LocalDateTime from, LocalDateTime to);
}

