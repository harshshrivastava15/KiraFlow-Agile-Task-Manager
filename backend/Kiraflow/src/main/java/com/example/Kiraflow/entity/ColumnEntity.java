package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import com.example.Kiraflow.entity.TaskEntity;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "columns")
@Getter @Setter @NoArgsConstructor
public class ColumnEntity {
    @Id private UUID id;
    private String title;
    private Integer positionIndex;

    @ManyToOne @JoinColumn(name="board_id")
    private com.example.Kiraflow.entity.Board board;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }

    @OneToMany(mappedBy = "column")
    private Set<TaskEntity> tasks;
}
