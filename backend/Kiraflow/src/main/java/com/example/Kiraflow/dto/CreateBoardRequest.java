package com.example.Kiraflow.dto;

import java.util.UUID;

public record CreateBoardRequest(UUID projectId, String name) {}
