package com.example.Kiraflow.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTaskRequest(UUID columnId, UUID epicId, UUID assigneeId, String type, String title,
                                String description, Integer storyPoints, String status, LocalDate dueDate) {}
