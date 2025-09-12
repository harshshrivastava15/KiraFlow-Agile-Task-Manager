package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sprints")
@Getter @Setter @NoArgsConstructor
public class Sprint {
    @Id private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    @ManyToOne @JoinColumn(name="project_id")
    private com.example.Kiraflow.entity.Project project;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }

    @OneToMany(mappedBy = "sprint")
    private Set<SprintAssignment> assignments;

    @OneToMany(mappedBy = "sprint")
    private Set<BurndownRecord> burndownRecords;
}

