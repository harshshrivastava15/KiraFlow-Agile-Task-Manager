package com.example.Kiraflow.dto;
import jakarta.validation.constraints.NotBlank;
public record UpdateCommentRequest(@NotBlank String content) {}