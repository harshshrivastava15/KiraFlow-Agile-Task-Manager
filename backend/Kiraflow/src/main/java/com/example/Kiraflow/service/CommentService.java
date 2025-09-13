package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.CommentDto;
import com.example.Kiraflow.dto.CreateCommentRequest;
import com.example.Kiraflow.dto.UpdateCommentRequest;
import com.example.Kiraflow.entity.TaskComment;
import com.example.Kiraflow.entity.TaskEntity;
import com.example.Kiraflow.entity.User;
import com.example.Kiraflow.repository.TaskCommentRepository;
import com.example.Kiraflow.repository.TaskRepository;
import com.example.Kiraflow.repository.UserRepository;
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
    private final PermissionService permissionService;
    private final CurrentUserService currentUserService;

    public CommentDto create(CreateCommentRequest req) {
        TaskEntity t = taskRepo.findById(req.taskId()).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        permissionService.requireProjectMember(t.getColumn().getBoard().getProject().getId());

        User u;
        // if userId provided, ensure current user matches or is allowed; otherwise use current user
        if (req.userId() != null) {
            u = userRepo.findById(req.userId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        } else {
            u = currentUserService.getCurrentUser();
        }

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
        TaskEntity t = taskRepo.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        permissionService.requireProjectMember(t.getColumn().getBoard().getProject().getId());
        return commentRepo.findAllByTaskEntityIdOrderByCreatedAtAsc(taskId).stream()
                .map(c -> new CommentDto(c.getId(), c.getTaskEntity()==null?null:c.getTaskEntity().getId(),
                        c.getUser()==null?null:c.getUser().getId(), c.getContent(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }


    public CommentDto update(UUID id, UpdateCommentRequest req) {
        TaskComment c = commentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        permissionService.requireProjectMember(c.getTaskEntity().getColumn().getBoard().getProject().getId());
        // optionally only allow author or admin to update — enforce here if desired
        c.setContent(req.content());
        TaskComment s = commentRepo.save(c);
        return new CommentDto(s.getId(), s.getTaskEntity()==null?null:s.getTaskEntity().getId(),
                s.getUser()==null?null:s.getUser().getId(), s.getContent(), s.getCreatedAt());
    }

    public void delete(UUID id) {
        TaskComment c = commentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        permissionService.requireProjectMember(c.getTaskEntity().getColumn().getBoard().getProject().getId());
        // optionally only allow author or admin
        commentRepo.deleteById(id);
    }
}
