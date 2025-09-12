package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor
public class Notification {
    @Id private UUID id;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

    @ManyToOne @JoinColumn(name="task_id")
    private TaskEntity taskEntity;

    @Column(columnDefinition = "json")
    private String payload;

    private boolean readFlag;

    private Instant createdAt;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); createdAt = Instant.now(); }
}
