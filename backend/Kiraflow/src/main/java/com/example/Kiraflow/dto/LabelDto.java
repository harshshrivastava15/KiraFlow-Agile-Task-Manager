package com.example.Kiraflow.dto;
import java.util.UUID;
public record LabelDto(UUID id, UUID orgId, String name, String color) {}