// src/main/java/com/kiraflow/repository/VersionRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VersionRepository extends JpaRepository<Version, UUID> {}
