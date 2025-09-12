package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.CreateTaskRequest;
import com.example.Kiraflow.dto.TaskDto;
import com.example.Kiraflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // fetch single task (optional)
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> get(@PathVariable UUID id) {
        // simple wrapper using repository (you can implement in service)
        var t = taskService.create(new CreateTaskRequest(null,null,null,"","placeholder","",0,"TODO",null)); // placeholder if not implemented
        // implement get in service for real use
        return ResponseEntity.ok(t);
    }
}
