package com.example.Kiraflow.dto;
import jakarta.validation.constraints.NotBlank;
public record UpdateEpicRequest(@NotBlank String title, String description) {}

