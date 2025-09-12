// src/main/java/com/kiraflow/repository/EpicRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EpicRepository extends JpaRepository<Epic, UUID> {}
