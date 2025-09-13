package com.example.Kiraflow.service;
import com.example.Kiraflow.dto.*;
import com.example.Kiraflow.entity.*;
import com.example.Kiraflow.repository.*;
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

    public EpicDto create(CreateEpicRequest req) {
        Project p = projectRepo.findById(req.projectId()).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Epic e = new Epic();
        e.setId(UUID.randomUUID());
        e.setKey(req.key());
        e.setTitle(req.title());
        e.setDescription(req.description());
        e.setProject(p);
        Epic saved = epicRepo.save(e);
        return new EpicDto(saved.getId(), p.getId(), saved.getKey(), saved.getTitle(), saved.getDescription());
    }

    public EpicDto get(UUID id) {
        Epic e = epicRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Epic not found"));
        return new EpicDto(e.getId(), e.getProject()==null?null:e.getProject().getId(), e.getKey(), e.getTitle(), e.getDescription());
    }

    public List<EpicDto> listByProject(UUID projectId) {
        return epicRepo.findAllByProjectId(projectId).stream()
                .map(e -> new EpicDto(e.getId(), e.getProject()==null?null:e.getProject().getId(), e.getKey(), e.getTitle(), e.getDescription()))
                .collect(Collectors.toList());
    }

    public EpicDto update(UUID id, UpdateEpicRequest req) {
        Epic e = epicRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Epic not found"));
        e.setTitle(req.title());
        e.setDescription(req.description());
        Epic s = epicRepo.save(e);
        return new EpicDto(s.getId(), s.getProject()==null?null:s.getProject().getId(), s.getKey(), s.getTitle(), s.getDescription());
    }

    public void delete(UUID id) { epicRepo.deleteById(id); }
}
