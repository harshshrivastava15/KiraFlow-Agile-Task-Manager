package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.LabelDto;
import com.example.Kiraflow.service.TaskLabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-entities/{taskEntityId}/labels")  // ✅ explicit naming
@RequiredArgsConstructor
public class TaskLabelController {
    private final TaskLabelService service;

    @PostMapping
    public ResponseEntity<List<LabelDto>> addLabel(
            @PathVariable UUID taskEntityId,
            @RequestParam UUID labelId
    ) {
        return ResponseEntity.ok(service.addLabelToTask(taskEntityId, labelId));
    }

    @DeleteMapping("/{labelId}")
    public ResponseEntity<List<LabelDto>> removeLabel(
            @PathVariable UUID taskEntityId,
            @PathVariable UUID labelId
    ) {
        return ResponseEntity.ok(service.removeLabelFromTask(taskEntityId, labelId));
    }

    @GetMapping
    public ResponseEntity<List<LabelDto>> list(@PathVariable UUID taskEntityId) {
        return ResponseEntity.ok(service.listLabelsForTask(taskEntityId));
    }
}
