package com.example.Kiraflow.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskRequest(
        UUID columnId,
        UUID epicId,
        UUID assigneeId,
        @NotBlank(message = "type required") String type,
        @NotBlank(message = "title required") String title,
        String description,
        Integer storyPoints,
        String status,
        LocalDate dueDate
) {}
