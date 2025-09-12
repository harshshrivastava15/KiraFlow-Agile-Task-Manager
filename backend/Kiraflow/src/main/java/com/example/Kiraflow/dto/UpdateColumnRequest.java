package com.example.Kiraflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateColumnRequest(
        @NotBlank(message = "title required") String title,
        @NotNull(message = "positionIndex required") Integer positionIndex
) {}
