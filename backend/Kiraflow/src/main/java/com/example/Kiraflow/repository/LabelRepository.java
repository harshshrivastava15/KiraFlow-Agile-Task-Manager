package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface LabelRepository extends JpaRepository<Label, UUID> {
    List<Label> findAllByOrgId(UUID orgId);
}

