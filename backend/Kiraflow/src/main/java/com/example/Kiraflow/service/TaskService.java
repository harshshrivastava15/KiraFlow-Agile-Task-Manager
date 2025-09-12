package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.CreateTaskRequest;
import com.example.Kiraflow.dto.TaskDto;
import com.example.Kiraflow.dto.UpdateTaskRequest;
import com.example.Kiraflow.entity.*;
import com.example.Kiraflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return map(saved);
    }

    public TaskDto getById(UUID id) {
        TaskEntity t = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return map(t);
    }

    public List<TaskDto> listByColumn(UUID columnId) {
        return taskRepo.findAllByColumnIdOrderByCreatedAt(columnId).stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskDto update(UUID id, UpdateTaskRequest req) {
        TaskEntity t = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (req.columnId() != null) {
            ColumnEntity c = columnRepo.findById(req.columnId()).orElseThrow(() -> new IllegalArgumentException("Column not found"));
            t.setColumn(c);
        }
        if (req.epicId() != null) {
            Epic e = epicRepo.findById(req.epicId()).orElseThrow(() -> new IllegalArgumentException("Epic not found"));
            t.setEpic(e);
        } else {
            t.setEpic(null);
        }
        if (req.assigneeId() != null) {
            User u = userRepo.findById(req.assigneeId()).orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
            t.setAssignee(u);
        } else {
            t.setAssignee(null);
        }
        t.setType(req.type());
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setStoryPoints(req.storyPoints());
        t.setStatus(req.status());
        t.setDueDate(req.dueDate());
        t.setUpdatedAt(Instant.now());
        TaskEntity saved = taskRepo.save(t);
        return map(saved);
    }

    public void delete(UUID id) {
        taskRepo.deleteById(id);
    }

    @Transactional
    public TaskDto moveTask(UUID taskId, UUID targetColumnId, Integer newPositionIndex) {
        TaskEntity t = taskRepo.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        ColumnEntity target = columnRepo.findById(targetColumnId).orElseThrow(() -> new IllegalArgumentException("Target column not found"));
        t.setColumn(target);
        // position handling: you can implement ordering field; here we don't maintain a separate position
        t.setUpdatedAt(Instant.now());
        TaskEntity saved = taskRepo.save(t);
        return map(saved);
    }

    private TaskDto map(TaskEntity saved) {
        return new TaskDto(
                saved.getId(),
                saved.getColumn() == null ? null : saved.getColumn().getId(),
                saved.getEpic() == null ? null : saved.getEpic().getId(),
                saved.getType(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStoryPoints(),
                saved.getStatus(),
                saved.getDueDate(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
