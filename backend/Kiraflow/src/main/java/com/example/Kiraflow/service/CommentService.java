package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.*;
import com.example.Kiraflow.entity.*;
import com.example.Kiraflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final TaskCommentRepository commentRepo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public CommentDto create(CreateCommentRequest req) {
        TaskEntity t = taskRepo.findById(req.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        User u = userRepo.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TaskComment c = new TaskComment();
        c.setId(UUID.randomUUID());
        c.setTaskEntity(t);
        c.setUser(u);
        c.setContent(req.content());
        c.setCreatedAt(Instant.now());

        TaskComment saved = commentRepo.save(c);
        return new CommentDto(saved.getId(), t.getId(), u.getId(), saved.getContent(), saved.getCreatedAt());
    }

    public List<CommentDto> listByTask(UUID taskId) {
        return commentRepo.findAllByTaskEntityIdOrderByCreatedAtAsc(taskId).stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getTaskEntity() == null ? null : c.getTaskEntity().getId(),
                        c.getUser() == null ? null : c.getUser().getId(),
                        c.getContent(),
                        c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public CommentDto update(UUID id, UpdateCommentRequest req) {
        TaskComment c = commentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        c.setContent(req.content());
        TaskComment s = commentRepo.save(c);
        return new CommentDto(
                s.getId(),
                s.getTaskEntity() == null ? null : s.getTaskEntity().getId(),
                s.getUser() == null ? null : s.getUser().getId(),
                s.getContent(),
                s.getCreatedAt()
        );
    }

    public void delete(UUID id) {
        commentRepo.deleteById(id);
    }
}
