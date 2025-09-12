package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "activity_logs")
@Getter @Setter @NoArgsConstructor
public class ActivityLog {
    @Id private UUID id;

    @ManyToOne @JoinColumn(name="org_id")
    private Organization org;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

    @ManyToOne @JoinColumn(name="task_id")
    private TaskEntity taskEntity;

    private String actionType;

    @Column(columnDefinition = "json")
    private String metadata; // store JSON as String (MySQL JSON type)

    private Instant createdAt;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); createdAt = Instant.now(); }
}
