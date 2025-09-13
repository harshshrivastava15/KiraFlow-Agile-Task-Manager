package com.example.Kiraflow.dto;
import java.util.UUID;
public record EpicDto(UUID id, UUID projectId, String key, String title, String description) {}
