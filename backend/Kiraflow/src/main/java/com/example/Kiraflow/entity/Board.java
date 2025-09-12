package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "boards")
@Getter @Setter @NoArgsConstructor
public class Board {
    @Id private UUID id;
    private String name;

    @ManyToOne @JoinColumn(name="project_id")
    private com.example.Kiraflow.entity.Project project;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }

    @OneToMany(mappedBy = "board")
    private Set<ColumnEntity> columns;
}

