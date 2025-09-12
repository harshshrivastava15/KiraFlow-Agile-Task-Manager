package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@Getter @Setter @NoArgsConstructor
public class Organization {
    @Id private UUID id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @PrePersist
    public void prePersist(){ if(id==null) id=UUID.randomUUID(); }

    @OneToMany(mappedBy = "organization")
    private Set<Project> projects;

    @OneToMany(mappedBy = "org")
    private Set<OrgMember> members;

    @OneToMany(mappedBy = "org")
    private Set<ActivityLog> activityLogs;
}

