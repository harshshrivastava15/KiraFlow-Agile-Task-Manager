package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.ColumnDto;
import com.example.Kiraflow.dto.CreateColumnRequest;
import com.example.Kiraflow.dto.UpdateColumnRequest;
import com.example.Kiraflow.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/columns")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @PostMapping
    public ResponseEntity<ColumnDto> create(@RequestBody @Valid CreateColumnRequest req) {
        return ResponseEntity.status(201).body(columnService.create(req));
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<ColumnDto>> listByBoard(@PathVariable UUID boardId) {
        return ResponseEntity.ok(columnService.listByBoard(boardId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColumnDto> update(@PathVariable UUID id, @RequestBody @Valid UpdateColumnRequest req) {
        return ResponseEntity.ok(columnService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        columnService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
