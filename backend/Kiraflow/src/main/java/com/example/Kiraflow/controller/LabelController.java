package com.example.Kiraflow.controller;
import com.example.Kiraflow.dto.*;
import com.example.Kiraflow.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @PostMapping
    public ResponseEntity<LabelDto> create(@RequestBody @Valid CreateLabelRequest req) {
        return ResponseEntity.status(201).body(labelService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(labelService.get(id));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<LabelDto>> listByOrg(@PathVariable UUID orgId) {
        return ResponseEntity.ok(labelService.listByOrg(orgId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelDto> update(@PathVariable UUID id, @RequestBody @Valid UpdateLabelRequest req) {
        return ResponseEntity.ok(labelService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        labelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
