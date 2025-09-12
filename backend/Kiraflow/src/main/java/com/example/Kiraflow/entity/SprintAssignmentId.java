package com.example.Kiraflow.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter @Setter @NoArgsConstructor
public class SprintAssignmentId implements Serializable {
    private UUID sprintId;
    private UUID taskId;
}
