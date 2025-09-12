// src/main/java/com/kiraflow/repository/ColumnRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ColumnRepository extends JpaRepository<ColumnEntity, UUID> {
    List<ColumnEntity> findAllByBoardIdOrderByPositionIndex(UUID boardId);
}
