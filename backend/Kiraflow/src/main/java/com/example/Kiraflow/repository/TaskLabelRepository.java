package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.TaskLabel;
import com.example.Kiraflow.entity.TaskLabelId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskLabelRepository extends JpaRepository<TaskLabel, TaskLabelId> {}