package com.example.Kiraflow.controller;
import com.example.Kiraflow.dto.*;
import com.example.Kiraflow.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> create(@RequestBody @Valid CreateCommentRequest req) {
        return ResponseEntity.status(201).body(commentService.create(req));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDto>> listByTask(@PathVariable UUID taskId) {
        return ResponseEntity.ok(commentService.listByTask(taskId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable UUID id, @RequestBody @Valid UpdateCommentRequest req) {
        return ResponseEntity.ok(commentService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
