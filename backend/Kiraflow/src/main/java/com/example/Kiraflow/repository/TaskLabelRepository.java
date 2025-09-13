package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.TaskLabel;
import com.example.Kiraflow.entity.TaskLabelId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskLabelRepository extends JpaRepository<TaskLabel, TaskLabelId> {
    List<TaskLabel> findAllByTaskEntity_Id(UUID taskEntityId);

    void deleteByTaskEntity_IdAndLabel_Id(UUID taskEntityId, UUID labelId);

    boolean existsByTaskEntity_IdAndLabel_Id(UUID taskEntityId, UUID labelId);
}
