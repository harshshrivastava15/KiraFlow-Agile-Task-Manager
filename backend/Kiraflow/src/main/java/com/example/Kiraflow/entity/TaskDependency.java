package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_dependencies")
@Getter @Setter @NoArgsConstructor
public class TaskDependency {
    @EmbeddedId
    private TaskDependencyId id;

    @ManyToOne @MapsId("taskId") @JoinColumn(name="task_id")
    private TaskEntity taskEntity;

    @ManyToOne @MapsId("dependsOnTaskId") @JoinColumn(name="depends_on_task_id")
    private TaskEntity dependsOn;
}
