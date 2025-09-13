package com.example.Kiraflow.service;
import com.example.Kiraflow.dto.*;
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

    public LabelDto create(CreateLabelRequest req) {
        Organization o = orgRepo.findById(req.orgId()).orElseThrow(() -> new IllegalArgumentException("Org not found"));
        Label l = new Label();
        l.setId(UUID.randomUUID());
        l.setName(req.name());
        l.setColor(req.color());
        l.setOrg(o);
        Label saved = labelRepo.save(l);
        return new LabelDto(saved.getId(), o.getId(), saved.getName(), saved.getColor());
    }

    public LabelDto get(UUID id) {
        Label l = labelRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Label not found"));
        return new LabelDto(l.getId(), l.getOrg()==null?null:l.getOrg().getId(), l.getName(), l.getColor());
    }

    public List<LabelDto> listByOrg(UUID orgId) {
        return labelRepo.findAllByOrgId(orgId).stream()
                .map(l -> new LabelDto(l.getId(), l.getOrg()==null?null:l.getOrg().getId(), l.getName(), l.getColor()))
                .collect(Collectors.toList());
    }

    public LabelDto update(UUID id, UpdateLabelRequest req) {
        Label l = labelRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Label not found"));
        l.setName(req.name());
        l.setColor(req.color());
        Label s = labelRepo.save(l);
        return new LabelDto(s.getId(), s.getOrg()==null?null:s.getOrg().getId(), s.getName(), s.getColor());
    }

    public void delete(UUID id) { labelRepo.deleteById(id); }
}