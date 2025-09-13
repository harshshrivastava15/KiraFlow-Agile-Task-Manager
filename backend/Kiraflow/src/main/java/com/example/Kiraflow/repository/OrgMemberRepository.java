package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.OrgMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrgMemberRepository extends JpaRepository<OrgMember, /* if composite key class put its type here, else UUID */ Object> {
    boolean existsByOrg_IdAndUser_Id(UUID orgId, UUID userId);
    Optional<OrgMember> findByOrg_IdAndUser_Id(UUID orgId, UUID userId);
}
