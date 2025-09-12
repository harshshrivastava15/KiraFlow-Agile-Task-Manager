package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.BoardDto;
import com.example.Kiraflow.dto.CreateBoardRequest;
import com.example.Kiraflow.entity.Board;
import com.example.Kiraflow.entity.Project;
import com.example.Kiraflow.repository.BoardRepository;
import com.example.Kiraflow.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepo;
    private final ProjectRepository projectRepo;

    public BoardDto create(CreateBoardRequest req) {
        Board b = new Board();
        b.setId(UUID.randomUUID());
        b.setName(req.name());
        if (req.projectId() != null) {
            Project p = projectRepo.findById(req.projectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            b.setProject(p);
        }
        Board saved = boardRepo.save(b);
        return new BoardDto(saved.getId(), saved.getProject() == null ? null : saved.getProject().getId(), saved.getName());
    }

    public List<BoardDto> listAll() {
        return boardRepo.findAll().stream()
                .map(b -> new BoardDto(b.getId(), b.getProject() == null ? null : b.getProject().getId(), b.getName()))
                .collect(Collectors.toList());
    }

    public BoardDto get(UUID id) {
        Board b = boardRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        return new BoardDto(b.getId(), b.getProject() == null ? null : b.getProject().getId(), b.getName());
    }

    public void delete(UUID id) {
        boardRepo.deleteById(id);
    }
}
