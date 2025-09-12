package com.example.Kiraflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @PrePersist
    public void prePersist() { if (id == null) id = UUID.randomUUID(); }

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "uploadedBy")
    private Set<Attachment> attachments;
}
