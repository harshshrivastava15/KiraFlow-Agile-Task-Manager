package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.CreateTaskRequest;
import com.example.Kiraflow.dto.TaskDto;
import com.example.Kiraflow.entity.*;
import com.example.Kiraflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepo;
    private final ColumnRepository columnRepo;
    private final EpicRepository epicRepo;
    private final UserRepository userRepo;

    public TaskDto create(CreateTaskRequest req) {
        ColumnEntity column = columnRepo.findById(req.columnId())
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));

        TaskEntity t = new TaskEntity();
        t.setId(UUID.randomUUID());
        t.setColumn(column);
        if (req.epicId() != null) {
            Epic e = epicRepo.findById(req.epicId()).orElseThrow(() -> new IllegalArgumentException("Epic not found"));
            t.setEpic(e);
        }
        if (req.assigneeId() != null) {
            User u = userRepo.findById(req.assigneeId()).orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
            t.setAssignee(u);
        }
        t.setType(req.type());
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setStoryPoints(req.storyPoints());
        t.setStatus(req.status() == null ? "TODO" : req.status());
        t.setDueDate(req.dueDate());
        t.setCreatedAt(Instant.now());
        TaskEntity saved = taskRepo.save(t);
        return new TaskDto(saved.getId(), saved.getColumn().getId(),
                saved.getEpic() == null ? null : saved.getEpic().getId(),
                saved.getType(), saved.getTitle(), saved.getDescription(),
                saved.getStoryPoints(), saved.getStatus(), saved.getDueDate(),
                saved.getCreatedAt(), saved.getUpdatedAt());
    }
}
