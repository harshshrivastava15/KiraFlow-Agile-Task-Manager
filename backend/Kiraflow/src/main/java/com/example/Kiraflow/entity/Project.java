package com.example.Kiraflow.entity;

import com.example.Kiraflow.entity.Organization;
import com.example.Kiraflow.entity.Sprint;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter @Setter @NoArgsConstructor
public class Project {
    @Id private UUID id;
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }

    @OneToMany(mappedBy = "project")
    private Set<com.example.Kiraflow.entity.Board> boards;

    @OneToMany(mappedBy = "project")
    private Set<com.example.Kiraflow.entity.Epic> epics;

    @OneToMany(mappedBy = "project")
    private Set<Version> versions;

    @OneToMany(mappedBy = "project")
    private Set<Sprint> sprints;
}

