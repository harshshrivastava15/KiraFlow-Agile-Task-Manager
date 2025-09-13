package com.example.Kiraflow.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public record CreateCommentRequest(@NotNull UUID taskId, @NotNull UUID userId, @NotBlank String content) {}
