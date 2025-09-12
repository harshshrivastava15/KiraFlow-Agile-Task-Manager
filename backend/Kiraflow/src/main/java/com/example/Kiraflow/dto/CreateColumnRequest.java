package com.example.Kiraflow.dto;

import java.util.UUID;

public record CreateColumnRequest(UUID boardId, String title, Integer positionIndex) {}
