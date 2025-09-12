package com.example.Kiraflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateColumnRequest(
        @NotNull(message = "boardId is required") UUID boardId,
        @NotBlank(message = "title must not be blank") String title,
        Integer positionIndex
) {}
