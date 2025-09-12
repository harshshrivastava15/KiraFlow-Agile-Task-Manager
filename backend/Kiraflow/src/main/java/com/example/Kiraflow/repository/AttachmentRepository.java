// src/main/java/com/kiraflow/repository/AttachmentRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
    List<Attachment> findAllById(UUID id);
}
