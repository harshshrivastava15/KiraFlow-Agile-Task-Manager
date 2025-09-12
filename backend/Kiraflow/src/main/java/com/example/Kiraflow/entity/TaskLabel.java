package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_labels")
@Getter @Setter @NoArgsConstructor
public class TaskLabel {
    @EmbeddedId
    private TaskLabelId id;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private TaskEntity taskEntity;

    @ManyToOne
    @MapsId("labelId")
    @JoinColumn(name = "label_id")
    private Label label;
}
