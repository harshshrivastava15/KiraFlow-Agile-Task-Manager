package com.example.Kiraflow.dto;
import java.time.Instant;
import java.util.UUID;
public record CommentDto(UUID id, UUID taskId, UUID userId, String content, Instant createdAt) {}
