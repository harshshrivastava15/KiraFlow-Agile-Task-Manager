package com.example.Kiraflow.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record UpdateBoardRequest(
        @NotBlank(message = "name required") String name,
        UUID projectId
) {}
