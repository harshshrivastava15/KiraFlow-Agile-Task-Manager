package com.example.Kiraflow.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CreateBoardRequest(
        UUID projectId,
        @NotBlank(message = "board name must not be blank") String name
) {}
