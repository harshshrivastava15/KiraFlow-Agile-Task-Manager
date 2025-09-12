package com.example.Kiraflow.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TaskDto(UUID id, UUID columnId, UUID epicId, String type, String title, String description,
                      Integer storyPoints, String status, LocalDate dueDate, Instant createdAt, Instant updatedAt) {}
