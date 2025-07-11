package org.example.stage_back.controller;

import org.example.stage_back.dto.LoginRequest;
import org.example.stage_back.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String role = authService.authenticate(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(Map.of("role", role));
    }
} 