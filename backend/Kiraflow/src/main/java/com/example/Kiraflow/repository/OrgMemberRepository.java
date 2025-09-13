package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.OrgMember;
import com.example.Kiraflow.entity.OrgMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrgMemberRepository extends JpaRepository<OrgMember, OrgMemberId> {
    boolean existsByOrg_IdAndUser_Id(UUID orgId, UUID userId);
    Optional<OrgMember> findByOrg_IdAndUser_Id(UUID orgId, UUID userId);
    List<OrgMember> findAllByUser_Id(UUID userId);
}
