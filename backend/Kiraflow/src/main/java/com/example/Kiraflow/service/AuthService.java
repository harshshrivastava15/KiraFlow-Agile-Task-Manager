package com.example.Kiraflow.service;

import com.example.Kiraflow.entity.User;
import com.example.Kiraflow.repository.UserRepository;
import com.example.Kiraflow.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(String name, String email, String rawPassword) {
        if (userRepo.existsByEmail(email)) throw new RuntimeException("Email already used");
        User u = new User();
        u.setId(UUID.randomUUID());
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles(Set.of("ROLE_USER"));
        userRepo.save(u);
    }

    public String login(String usernameOrEmail, String password) {
        var userOpt = userRepo.findByEmail(usernameOrEmail).or(() -> userRepo.findByName(usernameOrEmail));
        var user = userOpt.orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) throw new RuntimeException("Invalid credentials");
        return jwtUtil.generateToken(user.getEmail());
    }
}
