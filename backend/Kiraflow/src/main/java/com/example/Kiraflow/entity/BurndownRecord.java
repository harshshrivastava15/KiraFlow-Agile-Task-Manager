package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "burndown_records")
@Getter @Setter @NoArgsConstructor
public class BurndownRecord {
    @Id private UUID id;

    @ManyToOne @JoinColumn(name="sprint_id")
    private com.example.Kiraflow.entity.Sprint sprint;

    private LocalDate snapshotDate;
    private Integer remainingStoryPoints;
    private Integer completedStoryPoints;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }
}

