// src/main/java/com/kiraflow/repository/BurndownRecordRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.BurndownRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface BurndownRecordRepository extends JpaRepository<BurndownRecord, UUID> {
    List<BurndownRecord> findAllBySprintIdOrderBySnapshotDateAsc(UUID sprintId);
}
