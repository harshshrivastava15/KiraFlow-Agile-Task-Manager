// src/main/java/com/kiraflow/repository/TaskDependencyRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.TaskDependency;
import com.example.Kiraflow.entity.TaskDependencyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDependencyRepository extends JpaRepository<TaskDependency, TaskDependencyId> {}
