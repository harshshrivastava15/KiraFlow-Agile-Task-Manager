package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.CreateProjectRequest;
import com.example.Kiraflow.dto.ProjectDto;
import com.example.Kiraflow.entity.Organization;
import com.example.Kiraflow.entity.Project;
import com.example.Kiraflow.repository.OrganizationRepository;
import com.example.Kiraflow.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepo;
    private final OrganizationRepository orgRepo;
    private final PermissionService permissionService;

    public ProjectDto create(CreateProjectRequest req) {
        Organization o = orgRepo.findById(req.orgId()).orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        // require org membership to create project
        permissionService.requireOrgMember(o.getId());

        Project p = new Project();
        p.setId(UUID.randomUUID());
        p.setOrganization(o);
        p.setName(req.name());
        p.setDescription(req.description());
        Project s = projectRepo.save(p);
        return new ProjectDto(s.getId(), o.getId(), s.getName(), s.getDescription());
    }

    public ProjectDto getById(UUID id) {
        Project p = projectRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return new ProjectDto(p.getId(), p.getOrganization() == null ? null : p.getOrganization().getId(), p.getName(), p.getDescription());
    }

    public List<ProjectDto> listByOrg(UUID orgId) {
        permissionService.requireOrgMember(orgId);
        return projectRepo.findAllByOrganization_Id(orgId).stream()
                .map(p -> new ProjectDto(p.getId(), p.getOrganization()==null?null:p.getOrganization().getId(), p.getName(), p.getDescription()))
                .collect(Collectors.toList());
    }
}
