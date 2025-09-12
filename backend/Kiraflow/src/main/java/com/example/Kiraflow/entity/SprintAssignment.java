package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sprint_assignments")
@Getter @Setter @NoArgsConstructor
public class SprintAssignment {
    @EmbeddedId
    private SprintAssignmentId id;

    @ManyToOne @MapsId("sprintId") @JoinColumn(name="sprint_id")
    private com.example.Kiraflow.entity.Sprint sprint;

    @ManyToOne @MapsId("taskId") @JoinColumn(name="task_id")
    private TaskEntity taskEntity;

    private Integer positionIndex;
    private Boolean committed;
}
