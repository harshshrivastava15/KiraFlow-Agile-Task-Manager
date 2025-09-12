package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.ColumnDto;
import com.example.Kiraflow.dto.CreateColumnRequest;
import com.example.Kiraflow.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/columns")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @PostMapping
    public ResponseEntity<ColumnDto> create(@RequestBody CreateColumnRequest req) {
        return ResponseEntity.status(201).body(columnService.create(req));
    }
}
