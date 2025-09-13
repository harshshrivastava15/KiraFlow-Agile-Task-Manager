package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.AddOrgMemberRequest;
import com.example.Kiraflow.dto.CreateOrganizationRequest;
import com.example.Kiraflow.dto.OrganizationDto;
import com.example.Kiraflow.entity.OrgMemberId;
import com.example.Kiraflow.entity.Organization;
import com.example.Kiraflow.entity.OrgMember;
import com.example.Kiraflow.entity.User;
import com.example.Kiraflow.repository.OrganizationRepository;
import com.example.Kiraflow.repository.OrgMemberRepository;
import com.example.Kiraflow.repository.UserRepository;
import com.example.Kiraflow.util.UuidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository orgRepo;
    private final OrgMemberRepository orgMemberRepo;
    private final UserRepository userRepo;
    private final CurrentUserService currentUserService;

    public OrganizationDto create(CreateOrganizationRequest req) {
        User owner = currentUserService.getCurrentUser();

        Organization o = new Organization();
        o.setId(UUID.randomUUID());
        o.setName(req.name());
        o.setOwner(owner);
        Organization saved = orgRepo.save(o);

        // add owner as org member with "owner" role
        OrgMember om = new OrgMember();
        om.setId(new OrgMemberId(o.getId(), owner.getId()));
        om.setOrg(o);
        om.setUser(owner);
        om.setRole("owner");
        orgMemberRepo.save(om);


        return new OrganizationDto(saved.getId(), saved.getName(), owner.getId());
    }

    public OrganizationDto getById(UUID id) {
        Organization o = orgRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        return new OrganizationDto(o.getId(), o.getName(), o.getOwner() == null ? null : o.getOwner().getId());
    }

    public List<OrganizationDto> listForCurrentUser() {
        User me = currentUserService.getCurrentUser();
        // find orgs where user is a member via OrgMemberRepository
        List<OrgMember> memberships = orgMemberRepo.findAllByUser_Id(me.getId());
        return memberships.stream()
                .map(m -> {
                    Organization o = m.getOrg();
                    return new OrganizationDto(o.getId(), o.getName(), o.getOwner() == null ? null : o.getOwner().getId());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMember(UUID orgId, UUID userId, String role) {
        Organization o = orgRepo.findById(orgId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        // only admin or owner can add members
        // Using permission service would be fine; but since orgId is known, check current user role via OrgMemberRepository
        User me = currentUserService.getCurrentUser();
        OrgMember myMembership = orgMemberRepo.findByOrg_IdAndUser_Id(orgId, me.getId())
                .orElseThrow(() -> new IllegalArgumentException("You are not a member of this org"));
        String myRole = myMembership.getRole();
        if (!("owner".equalsIgnoreCase(myRole) || "admin".equalsIgnoreCase(myRole))) {
            throw new SecurityException("Requires admin or owner role to add members");
        }

        User userToAdd = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User to add not found"));

        // avoid duplicate
        if (orgMemberRepo.findByOrg_IdAndUser_Id(orgId, userToAdd.getId()).isPresent()) return;

        OrgMember nm = new OrgMember();
        nm.setId(new OrgMemberId(o.getId(), userToAdd.getId()));
        nm.setOrg(o);
        nm.setUser(userToAdd);
        nm.setRole(role);
        orgMemberRepo.save(nm);

    }

    @Transactional
    public void removeMember(UUID orgId, UUID userId) {
        Organization o = orgRepo.findById(orgId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        // require admin/owner
        User me = currentUserService.getCurrentUser();
        OrgMember myMembership = orgMemberRepo.findByOrg_IdAndUser_Id(orgId, me.getId())
                .orElseThrow(() -> new IllegalArgumentException("You are not a member of this org"));
        String myRole = myMembership.getRole();
        if (!("owner".equalsIgnoreCase(myRole) || "admin".equalsIgnoreCase(myRole))) {
            throw new SecurityException("Requires admin or owner role to remove members");
        }

        OrgMember target = orgMemberRepo.findByOrg_IdAndUser_Id(orgId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        orgMemberRepo.delete(target);
    }
}
