// src/main/java/com/kiraflow/repository/NotificationRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
