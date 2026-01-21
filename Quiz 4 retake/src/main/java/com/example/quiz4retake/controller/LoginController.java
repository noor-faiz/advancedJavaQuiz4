package com.example.quiz4retake.controller;

import com.example.quiz4retake.security.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class LoginController {

    private final JwtService jwtService;

    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // role should be "ADMIN" or "USER"
    @PostMapping("/api/login")
    public String token(@RequestParam String username, @RequestParam String role) {

        HashMap<String, String> payload = new HashMap<>();
        payload.put("role", role);

        String token = jwtService.generateToken(username, payload);
        System.out.println(token);

        return token;
    }
}

