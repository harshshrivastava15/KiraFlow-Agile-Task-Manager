package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {

    List<ActivityLog> findAllByOrg_IdOrderByCreatedAtDesc(UUID orgId);

    List<ActivityLog> findAllByTaskEntity_IdOrderByCreatedAtDesc(UUID taskId);
}

