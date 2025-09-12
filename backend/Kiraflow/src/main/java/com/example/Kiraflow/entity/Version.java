package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "versions")
@Getter @Setter @NoArgsConstructor
public class Version {
    @Id private UUID id;
    private String name;
    private LocalDate releaseDate;

    @ManyToOne @JoinColumn(name="project_id")
    private com.example.Kiraflow.entity.Project project;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }
}

