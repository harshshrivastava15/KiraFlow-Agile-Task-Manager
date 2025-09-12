package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.CreateTaskRequest;
import com.example.Kiraflow.dto.TaskDto;
import com.example.Kiraflow.dto.UpdateTaskRequest;
import com.example.Kiraflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody @Valid CreateTaskRequest req) {
        return ResponseEntity.status(201).body(taskService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @GetMapping("/column/{columnId}")
    public ResponseEntity<List<TaskDto>> listByColumn(@PathVariable UUID columnId) {
        return ResponseEntity.ok(taskService.listByColumn(columnId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable UUID id, @RequestBody @Valid UpdateTaskRequest req) {
        return ResponseEntity.ok(taskService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<TaskDto> move(@PathVariable UUID id,
                                        @RequestParam UUID targetColumnId,
                                        @RequestParam(required = false) Integer positionIndex) {
        return ResponseEntity.ok(taskService.moveTask(id, targetColumnId, positionIndex));
    }
}
