package com.example.Kiraflow.service;

import com.example.Kiraflow.entity.User;
import com.example.Kiraflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new IllegalStateException("No authenticated user");
        String principal = auth.getName(); // email as principal
        return userRepository.findByEmail(principal)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + principal));
    }
}
