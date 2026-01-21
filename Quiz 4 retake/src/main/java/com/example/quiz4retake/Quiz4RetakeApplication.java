package com.example.quiz4retake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Quiz4RetakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Quiz4RetakeApplication.class, args);
    }
}
