package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {}