package com.example.quiz4retake.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationProvider extends OncePerRequestFilter {

    protected final JwtService jwtService;

    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("my-cookie")) {
                    token = cookie.getValue();
                }
            }
        }

        if (token.isEmpty()) {
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                token = auth.substring(7);
            }
        }

        if (token.isEmpty()) {
            SecurityContextHolder.clearContext();
        } else {
            try {
                String username = jwtService.getUsername(token);
                String role = jwtService.getRole(token);

                System.out.println(username);
                System.out.println(role);

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(
                        new SimpleGrantedAuthority("ROLE_" + role)
                ));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException e) {
                SecurityContextHolder.clearContext();
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}
