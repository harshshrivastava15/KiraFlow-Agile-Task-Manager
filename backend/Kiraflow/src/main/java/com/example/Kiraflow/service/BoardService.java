package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.BoardDto;
import com.example.Kiraflow.dto.CreateBoardRequest;
import com.example.Kiraflow.dto.UpdateBoardRequest;
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
    private final PermissionService permissionService;

    public BoardDto create(CreateBoardRequest req) {
        Board b = new Board();
        b.setId(UUID.randomUUID());
        b.setName(req.name());

        if (req.projectId() != null) {
            Project p = projectRepo.findById(req.projectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            // require membership in project
            permissionService.requireProjectMember(p.getId());
            b.setProject(p);
        }

        Board saved = boardRepo.save(b);
        return new BoardDto(saved.getId(), saved.getProject() == null ? null : saved.getProject().getId(), saved.getName());
    }

    public List<BoardDto> listAll() {
        return boardRepo.findAll().stream()
                .map(b -> new BoardDto(b.getId(), b.getProject()==null?null:b.getProject().getId(), b.getName()))
                .collect(Collectors.toList());
    }

    public BoardDto get(UUID id) {
        Board b = boardRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        // viewing a board requires being project member
        if (b.getProject() != null) permissionService.requireProjectMember(b.getProject().getId());
        return new BoardDto(b.getId(), b.getProject()==null?null:b.getProject().getId(), b.getName());
    }


    public BoardDto update(UUID id, UpdateBoardRequest req) {
        Board b = boardRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        if (b.getProject() != null) permissionService.requireProjectMember(b.getProject().getId());
        b.setName(req.name());
        // allow changing projectId only if project exists and user is member
        if (req.projectId() != null) {
            Project p = projectRepo.findById(req.projectId()).orElseThrow(() -> new IllegalArgumentException("Project not found"));
            permissionService.requireProjectMember(p.getId());
            b.setProject(p);
        }
        Board s = boardRepo.save(b);
        return new BoardDto(s.getId(), s.getProject()==null?null:s.getProject().getId(), s.getName());
    }

    public void delete(UUID id) {
        Board b = boardRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        if (b.getProject() != null) {
            // require admin/owner to delete a board
            permissionService.requireOrgAdminOrOwner(b.getProject().getOrganization().getId());
        }
        boardRepo.deleteById(id);
    }
}
