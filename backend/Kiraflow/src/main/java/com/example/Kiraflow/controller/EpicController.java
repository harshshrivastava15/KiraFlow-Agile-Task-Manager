package com.example.Kiraflow.controller;
import com.example.Kiraflow.dto.*;
import com.example.Kiraflow.service.EpicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/epics")
@RequiredArgsConstructor
public class EpicController {
    private final EpicService epicService;

    @PostMapping
    public ResponseEntity<EpicDto> create(@RequestBody @Valid CreateEpicRequest req) {
        return ResponseEntity.status(201).body(epicService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpicDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(epicService.get(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<EpicDto>> listByProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(epicService.listByProject(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpicDto> update(@PathVariable UUID id, @RequestBody @Valid UpdateEpicRequest req) {
        return ResponseEntity.ok(epicService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        epicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
