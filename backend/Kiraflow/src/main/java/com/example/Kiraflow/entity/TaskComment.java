package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "task_comments")
@Getter @Setter @NoArgsConstructor
public class TaskComment {
    @Id private UUID id;

    @ManyToOne @JoinColumn(name="task_id")
    private TaskEntity taskEntity;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

    @Column(columnDefinition = "text")
    private String content;

    private Instant createdAt;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); createdAt = Instant.now(); }
}
