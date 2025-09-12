// src/main/java/com/kiraflow/repository/SprintRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SprintRepository extends JpaRepository<Sprint, UUID> {}
