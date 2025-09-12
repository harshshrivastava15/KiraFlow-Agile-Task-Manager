// src/main/java/com/kiraflow/repository/BoardRepository.java
package com.example.Kiraflow.repository;

import com.example.Kiraflow.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {}

