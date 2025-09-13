package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.CreateEpicRequest;
import com.example.Kiraflow.dto.EpicDto;
import com.example.Kiraflow.dto.UpdateEpicRequest;
import com.example.Kiraflow.entity.Epic;
import com.example.Kiraflow.entity.Project;
import com.example.Kiraflow.repository.EpicRepository;
import com.example.Kiraflow.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpicService {
    private final EpicRepository epicRepo;
    private final ProjectRepository projectRepo;
    private final PermissionService permissionService;

    public EpicDto create(CreateEpicRequest req) {
        Project p = projectRepo.findById(req.projectId()).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        permissionService.requireProjectMember(p.getId());

        Epic e = new Epic();
        e.setId(UUID.randomUUID());
        e.setKey(req.key());
        e.setTitle(req.title());
        e.setDescription(req.description());
        e.setProject(p);
        Epic s = epicRepo.save(e);
        return new EpicDto(s.getId(), p.getId(), s.getKey(), s.getTitle(), s.getDescription());
    }

    public EpicDto get(UUID id) {
        Epic e = epicRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Epic not found"));
        permissionService.requireProjectMember(e.getProject().getId());
        return new EpicDto(e.getId(), e.getProject()==null?null:e.getProject().getId(), e.getKey(), e.getTitle(), e.getDescription());
    }

    public List<EpicDto> listByProject(UUID projectId) {
        permissionService.requireProjectMember(projectId);
        return epicRepo.findAllByProjectId(projectId).stream()
                .map(e -> new EpicDto(e.getId(), e.getProject()==null?null:e.getProject().getId(), e.getKey(), e.getTitle(), e.getDescription()))
                .collect(Collectors.toList());
    }


    public EpicDto update(UUID id, UpdateEpicRequest req) {
        Epic e = epicRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Epic not found"));
        permissionService.requireProjectMember(e.getProject().getId());
        e.setTitle(req.title());
        e.setDescription(req.description());
        Epic s = epicRepo.save(e);
        return new EpicDto(s.getId(), s.getProject()==null?null:s.getProject().getId(), s.getKey(), s.getTitle(), s.getDescription());
    }

    public void delete(UUID id) {
        Epic e = epicRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Epic not found"));
        permissionService.requireProjectMember(e.getProject().getId());
        epicRepo.deleteById(id);
    }
}
