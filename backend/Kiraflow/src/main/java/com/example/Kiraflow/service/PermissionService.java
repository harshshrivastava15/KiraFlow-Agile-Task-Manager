package com.example.Kiraflow.service;

import com.example.Kiraflow.entity.OrgMember;
import com.example.Kiraflow.entity.Organization;
import com.example.Kiraflow.entity.Project;
import com.example.Kiraflow.entity.User;
import com.example.Kiraflow.repository.OrgMemberRepository;
import com.example.Kiraflow.repository.OrganizationRepository;
import com.example.Kiraflow.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final OrgMemberRepository orgMemberRepo;
    private final ProjectRepository projectRepo;
    private final OrganizationRepository organizationRepo;
    private final CurrentUserService currentUserService;

    // check membership by org id
    public boolean isOrgMember(UUID orgId) {
        User u = currentUserService.getCurrentUser();
        return orgMemberRepo.existsByOrg_IdAndUser_Id(orgId, u.getId());
    }

    public void requireOrgMember(UUID orgId) {
        if (!isOrgMember(orgId)) throw new AccessDeniedException("User is not a member of organization");
    }

    // project -> org -> membership
    public boolean isProjectMember(UUID projectId) {
        Project p = projectRepo.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Organization org = p.getOrganization();
        if (org == null) throw new IllegalArgumentException("Project missing organization");
        return isOrgMember(org.getId());
    }

    public void requireProjectMember(UUID projectId) {
        if (!isProjectMember(projectId)) throw new AccessDeniedException("User is not a member of project");
    }

    // require specific role in org (e.g. "owner" or "admin")
    public void requireOrgRole(UUID orgId, String requiredRole) {
        User u = currentUserService.getCurrentUser();
        OrgMember om = orgMemberRepo.findByOrg_IdAndUser_Id(orgId, u.getId())
                .orElseThrow(() -> new AccessDeniedException("Not a member"));
        if (!requiredRole.equalsIgnoreCase(om.getRole())) {
            throw new AccessDeniedException("Requires role: " + requiredRole);
        }
    }

    // convenience: check owner or admin
    public void requireOrgAdminOrOwner(UUID orgId) {
        User u = currentUserService.getCurrentUser();
        OrgMember om = orgMemberRepo.findByOrg_IdAndUser_Id(orgId, u.getId())
                .orElseThrow(() -> new AccessDeniedException("Not a member"));
        String r = om.getRole();
        if (!( "owner".equalsIgnoreCase(r) || "admin".equalsIgnoreCase(r) )) {
            throw new AccessDeniedException("Requires org admin or owner");
        }
    }
}
