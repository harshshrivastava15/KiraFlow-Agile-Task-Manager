package com.example.Kiraflow.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter @Setter @NoArgsConstructor
public class OrgMemberId implements java.io.Serializable {
    private UUID orgId;
    private UUID userId;
}
