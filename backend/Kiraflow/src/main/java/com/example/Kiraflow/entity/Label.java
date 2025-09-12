package com.example.Kiraflow.entity;

import com.example.Kiraflow.entity.Organization;
import com.example.Kiraflow.entity.TaskLabel;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "labels")
@Getter @Setter @NoArgsConstructor
public class Label {
    @Id private UUID id;
    private String name;
    private String color;

    @ManyToOne @JoinColumn(name="org_id")
    private Organization org;

    @PrePersist public void prePersist(){ if(id==null) id=UUID.randomUUID(); }

    @OneToMany(mappedBy = "label")
    private Set<TaskLabel> taskLabels;
}
