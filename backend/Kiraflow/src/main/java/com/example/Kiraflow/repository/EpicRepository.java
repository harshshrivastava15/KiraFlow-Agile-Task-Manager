package com.example.Kiraflow.repository;
import com.example.Kiraflow.entity.Epic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface EpicRepository extends JpaRepository<Epic, UUID> {
    List<Epic> findAllByProjectId(UUID projectId);
}