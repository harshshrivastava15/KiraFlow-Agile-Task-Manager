package com.example.Kiraflow.controller;

import com.example.Kiraflow.dto.AddOrgMemberRequest;
import com.example.Kiraflow.dto.CreateOrganizationRequest;
import com.example.Kiraflow.dto.OrganizationDto;
import com.example.Kiraflow.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService orgService;

    @PostMapping
    public ResponseEntity<OrganizationDto> create(@RequestBody @Valid CreateOrganizationRequest req) {
        OrganizationDto dto = orgService.create(req);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(orgService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> listMine() {
        return ResponseEntity.ok(orgService.listForCurrentUser());
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMember(@PathVariable UUID id, @RequestBody @Valid AddOrgMemberRequest req) throws BadRequestException {
        UUID userId;
        try {
            userId = com.example.Kiraflow.util.UuidUtil.parseFlexibleUuid(req.userId());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid userId format: " + ex.getMessage()); // map to 400 in your GlobalExceptionHandler
        }
        orgService.addMember(id, userId, req.role());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable UUID id, @PathVariable UUID userId) {
        orgService.removeMember(id, userId);
        return ResponseEntity.noContent().build();
    }
}
