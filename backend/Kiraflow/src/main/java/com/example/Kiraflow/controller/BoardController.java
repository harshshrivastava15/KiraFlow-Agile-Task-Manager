package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.BoardDto;
import com.example.Kiraflow.dto.CreateBoardRequest;
import com.example.Kiraflow.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardDto> create(@RequestBody CreateBoardRequest req) {
        return ResponseEntity.status(201).body(boardService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<BoardDto>> list() {
        return ResponseEntity.ok(boardService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(boardService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
