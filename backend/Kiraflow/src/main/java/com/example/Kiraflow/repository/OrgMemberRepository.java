// src/main/java/com/kiraflow/repository/OrgMemberRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.OrgMember;
import com.example.Kiraflow.entity.OrgMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgMemberRepository extends JpaRepository<OrgMember, OrgMemberId> {}
