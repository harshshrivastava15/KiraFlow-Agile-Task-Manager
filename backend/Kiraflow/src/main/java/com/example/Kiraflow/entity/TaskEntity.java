package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor
public class TaskEntity {
    @Id private UUID id;

    private String type;
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private Integer storyPoints;
    private String status;
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne @JoinColumn(name="column_id")
    private com.example.Kiraflow.entity.ColumnEntity column;

    @ManyToOne @JoinColumn(name="epic_id")
    private com.example.Kiraflow.entity.Epic epic;

    @ManyToOne @JoinColumn(name="sprint_id")
    private com.example.Kiraflow.entity.Sprint sprint;

    @ManyToOne @JoinColumn(name="version_id")
    private Version version;

    @ManyToOne @JoinColumn(name="assignee_id")
    private User assignee;

    @ManyToOne @JoinColumn(name="parent_task_id")
    private TaskEntity parentTaskEntity;

    @OneToMany(mappedBy = "taskEntity")
    private Set<com.example.Kiraflow.entity.TaskComment> comments;

    @OneToMany(mappedBy = "taskEntity")
    private Set<Attachment> attachments;

    @OneToMany(mappedBy = "taskEntity")
    private Set<com.example.Kiraflow.entity.TaskLabel> labels;

    @PrePersist public void prePersist(){
        if(id==null) id=UUID.randomUUID();
        createdAt = Instant.now();
    }
    @PreUpdate public void preUpdate(){ updatedAt = Instant.now(); }
}
