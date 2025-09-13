package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByOrganization_Id(UUID orgId);
}
