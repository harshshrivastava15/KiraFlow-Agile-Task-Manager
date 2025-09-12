// src/main/java/com/kiraflow/repository/SprintAssignmentRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.SprintAssignment;
import com.example.Kiraflow.entity.SprintAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintAssignmentRepository extends JpaRepository<SprintAssignment, SprintAssignmentId> {}
