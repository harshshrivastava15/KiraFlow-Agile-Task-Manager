package com.example.Kiraflow.dto;
import jakarta.validation.constraints.NotBlank;
public record UpdateLabelRequest(@NotBlank String name, String color) {}
