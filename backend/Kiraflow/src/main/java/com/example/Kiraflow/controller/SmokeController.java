// src/main/java/com/kiraflow/controller/SmokeController.java
package com.example.Kiraflow.controller;

import com.example.Kiraflow.entity.User;
import com.example.Kiraflow.repository.UserRepository;
import com.example.Kiraflow.repository.BoardRepository;
import com.example.Kiraflow.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/smoke")
@RequiredArgsConstructor
public class SmokeController {
    private final UserRepository userRepo;
    private final BoardRepository boardRepo;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User u) {
        if (u.getId() == null) u.setId(UUID.randomUUID());
        return ResponseEntity.ok(userRepo.save(u));
    }

    @GetMapping("/boards")
    public ResponseEntity<?> boards() {
        return ResponseEntity.ok(boardRepo.findAll());
    }
}
