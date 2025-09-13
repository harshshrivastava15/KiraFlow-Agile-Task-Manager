package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.TaskEntity; // or com.kiraflow.entity.Task if you kept name
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findAllByColumn_IdOrderByCreatedAt(UUID columnId);
    List<TaskEntity> findAllByAssigneeId(UUID assigneeId);
}

