package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    List<Organization> findAllByOwner_Id(UUID ownerId);

}
