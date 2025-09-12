package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import com.example.Kiraflow.entity.TaskEntity;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "epics")
@Getter @Setter @NoArgsConstructor
public class Epic {
    @Id private UUID id;
    private String key;
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne @JoinColumn(name="project_id")
    private com.example.Kiraflow.entity.Project project;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }

    @OneToMany(mappedBy = "epic")
    private Set<TaskEntity> tasks;
}
