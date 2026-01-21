package com.example.quiz4retake.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;

    private String customerName;
    private String productName;
    private int quantity;
    private double price;
    private String status;
    private LocalDateTime localDateTime;
}

