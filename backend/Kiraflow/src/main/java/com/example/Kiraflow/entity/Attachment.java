package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "attachments")
@Getter @Setter @NoArgsConstructor
public class Attachment {
    @Id private UUID id;

    @ManyToOne @JoinColumn(name="task_id")
    private TaskEntity taskEntity;

    @ManyToOne @JoinColumn(name="uploaded_by")
    private User uploadedBy;

    private String url;
    private Instant uploadedAt;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); uploadedAt = Instant.now(); }
}
