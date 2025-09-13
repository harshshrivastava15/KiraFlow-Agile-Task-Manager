package com.example.Kiraflow.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public record CreateLabelRequest(@NotNull UUID orgId, @NotBlank String name, String color) {}