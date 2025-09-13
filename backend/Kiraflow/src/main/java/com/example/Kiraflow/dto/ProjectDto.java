package com.example.Kiraflow.dto;

import java.util.UUID;

public record ProjectDto(UUID id, UUID orgId, String name, String description) {}

