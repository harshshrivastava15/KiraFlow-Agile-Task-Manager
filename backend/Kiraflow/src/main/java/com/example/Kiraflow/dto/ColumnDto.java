package com.example.Kiraflow.dto;

import java.util.UUID;

public record ColumnDto(UUID id, UUID boardId, String title, Integer positionIndex) {}
