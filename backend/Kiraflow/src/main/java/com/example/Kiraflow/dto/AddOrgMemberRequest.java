package com.example.Kiraflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;


public record AddOrgMemberRequest(String userId, @NotBlank String role) {}
