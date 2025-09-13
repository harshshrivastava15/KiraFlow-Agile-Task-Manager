package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.CreateLabelRequest;
import com.example.Kiraflow.dto.LabelDto;
import com.example.Kiraflow.dto.UpdateLabelRequest;
import com.example.Kiraflow.entity.Label;
import com.example.Kiraflow.entity.Organization;
import com.example.Kiraflow.repository.LabelRepository;
import com.example.Kiraflow.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepo;
    private final OrganizationRepository orgRepo;
    private final PermissionService permissionService;

    public LabelDto create(CreateLabelRequest req) {
        Organization o = orgRepo.findById(req.orgId()).orElseThrow(() -> new IllegalArgumentException("Org not found"));
        permissionService.requireOrgMember(o.getId()); // allow members to create; change to requireOrgAdminOrOwner if needed

        Label l = new Label();
        l.setId(UUID.randomUUID());
        l.setName(req.name());
        l.setColor(req.color());
        l.setOrg(o);
        Label s = labelRepo.save(l);
        return new LabelDto(s.getId(), o.getId(), s.getName(), s.getColor());
    }

    public LabelDto get(UUID id) {
        Label l = labelRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Label not found"));
        permissionService.requireOrgMember(l.getOrg().getId());
        return new LabelDto(l.getId(), l.getOrg()==null?null:l.getOrg().getId(), l.getName(), l.getColor());
    }

    public List<LabelDto> listByOrg(UUID orgId) {
        permissionService.requireOrgMember(orgId);
        return labelRepo.findAllByOrgId(orgId).stream()
                .map(l -> new LabelDto(l.getId(), l.getOrg()==null?null:l.getOrg().getId(), l.getName(), l.getColor()))
                .collect(Collectors.toList());
    }


    public LabelDto update(UUID id, UpdateLabelRequest req) {
        Label l = labelRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Label not found"));
        permissionService.requireOrgMember(l.getOrg().getId());
        l.setName(req.name());
        l.setColor(req.color());
        Label s = labelRepo.save(l);
        return new LabelDto(s.getId(), s.getOrg()==null?null:s.getOrg().getId(), s.getName(), s.getColor());
    }

    public void delete(UUID id) {
        Label l = labelRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Label not found"));
        permissionService.requireOrgAdminOrOwner(l.getOrg().getId()); // require admin/owner to delete
        labelRepo.deleteById(id);
    }
}
