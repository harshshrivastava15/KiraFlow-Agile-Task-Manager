package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.*;
import com.example.Kiraflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        authService.register(req.name(), req.email(), req.password());
        return ResponseEntity.status(201).body("registered");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.usernameOrEmail(), req.password());
        return ResponseEntity.ok(new JwtResponse(token, "Bearer", req.usernameOrEmail()));
    }
}
