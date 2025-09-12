package com.example.Kiraflow.entity;

import com.example.Kiraflow.entity.Organization;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "org_members")
@Getter @Setter @NoArgsConstructor
public class OrgMember {
    @EmbeddedId
    private OrgMemberId id;

    @ManyToOne
    @MapsId("orgId")
    @JoinColumn(name = "org_id")
    private Organization org;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    private String role;
    private Instant joinedAt = Instant.now();
}

