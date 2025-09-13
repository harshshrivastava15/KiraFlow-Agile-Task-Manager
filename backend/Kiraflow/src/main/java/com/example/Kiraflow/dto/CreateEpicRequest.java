package com.example.Kiraflow.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public record CreateEpicRequest(@NotNull UUID projectId, @NotBlank String key, @NotBlank String title, String description) {}
