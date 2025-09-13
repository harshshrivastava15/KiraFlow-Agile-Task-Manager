package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.CreateProjectRequest;
import com.example.Kiraflow.dto.ProjectDto;
import com.example.Kiraflow.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> create(@RequestBody @Valid CreateProjectRequest req) {
        ProjectDto dto = projectService.create(req);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<ProjectDto>> listByOrg(@PathVariable UUID orgId) {
        return ResponseEntity.ok(projectService.listByOrg(orgId));
    }
}
