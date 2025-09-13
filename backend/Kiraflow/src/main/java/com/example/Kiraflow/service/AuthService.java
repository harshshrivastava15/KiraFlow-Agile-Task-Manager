package com.example.Kiraflow.service;

import com.example.Kiraflow.entity.User;
import com.example.Kiraflow.repository.UserRepository;
import com.example.Kiraflow.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(String name, String email, String rawPassword) {
        if (email == null || rawPassword == null || name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name, email and password are required");
        }

        if (userRepo.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already used");
        }

        User u = new User();
        u.setId(UUID.randomUUID());
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles(Set.of("ROLE_USER"));
        userRepo.save(u);
    }

    public String login(String usernameOrEmail, String password) {
        if (usernameOrEmail == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username/email and password are required");
        }

        var userOpt = userRepo.findByEmail(usernameOrEmail).or(() -> userRepo.findByName(usernameOrEmail));
        var user = userOpt.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        try {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        } catch (Exception ex) {
            // log and wrap unexpected errors (e.g. passwordEncoder null or other)
            log.error("Error checking password for user={}, cause={}", usernameOrEmail, ex.toString(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication error");
        }

        if (jwtUtil == null) {
            log.error("JwtUtil bean is null - token generation unavailable");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Token generator not configured");
        }

        try {
            return jwtUtil.generateToken(user.getEmail());
        } catch (Exception ex) {
            log.error("Token generation failed for user={}, cause={}", usernameOrEmail, ex.toString(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate token");
        }
    }
}
