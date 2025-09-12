package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface TaskCommentRepository extends JpaRepository<TaskComment, UUID> {
    List<TaskComment> findAllByIdOrderByCreatedAtAsc(UUID id);
}