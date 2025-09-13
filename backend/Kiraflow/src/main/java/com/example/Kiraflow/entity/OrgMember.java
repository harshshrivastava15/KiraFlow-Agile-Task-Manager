package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "org_members")
@Getter @Setter @NoArgsConstructor
public class OrgMember {

    @EmbeddedId
    private OrgMemberId id;

    @MapsId("orgId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization org;

    @MapsId("userId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String role;

    private Instant joinedAt;

    @PrePersist
    public void prePersist() {
        if (joinedAt == null) joinedAt = Instant.now();
    }
}
