package com.example.Kiraflow.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class OrgMemberId implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID orgId;
    private UUID userId;
}
