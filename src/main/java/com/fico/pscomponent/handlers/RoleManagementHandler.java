package com.fico.pscomponent.handlers;

import com.fico.dmp.telusagentuidb.Role;
import com.fico.dmp.telusagentuidb.UserRole;

import com.fico.dmp.telusagentuidb.models.query.GetActiveRolesByUserNameResponse;
import com.fico.dmp.telusagentuidb.service.RoleService;
import com.fico.dmp.telusagentuidb.service.UserRoleService;

import com.fico.dmp.telusagentuidb.service.TELUSAgentUIDBQueryExecutorServiceImpl;
import com.wavemaker.commons.model.security.RolesConfig;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.security.config.WMAppSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleManagementHandler {

    @Autowired
    private RoleService roleService;
    
    @Autowired
	private UserRoleService userRoleService;

    @Autowired
    private WMAppSecurityConfig wmAppSecurityConfig;

    @Autowired
    private TELUSAgentUIDBQueryExecutorServiceImpl coreQueryExecutorServiceImpl;

    private static final String USERID = "UserId=";

    private static final Logger logger = LoggerFactory.getLogger(RoleManagementHandler.class);

    public List<String> getSystemRoles() {
        RolesConfig rolesConfig = wmAppSecurityConfig.getRolesConfig();
        List<String> rolesList = Objects.isNull(rolesConfig) ? Collections.emptyList()
                : new ArrayList<>(rolesConfig.getRoleMap().keySet());
        return rolesList.stream().filter(role -> !role.startsWith("component_")).collect(Collectors.toList());
    }

    public boolean createSystemRoles() {
        try {
            final List<String> rolesList = getSystemRoles();
            for (String role : rolesList) {
                createRole(role);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error creating system roles: ", e);
            return false;
        }
    }

    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<String>();
		/*Pageable pageable = PageRequest.of(0, 100);
		List<Role> roleList = new ArrayList<Role>();
		Page<Role> rolePage = roleService.findAll("", pageable);
		while (!rolePage.isEmpty()) {
			pageable = pageable.next();
			roleList.addAll(rolePage.getContent());
			rolePage = roleService.findAll("", pageable);
		}
		if (!roleList.isEmpty()) {
			roles = roleList.stream().map(Role::getRole).collect(Collectors.toList());
		}*/
        return roles;
    }

    public List<String> getUserRoleByUserId(int userId) {
        List<String> roles = new ArrayList<String>();
		Pageable pageable = PageRequest.of(0, 100);
		List<UserRole> userRoleList = new ArrayList<UserRole>();
		Page<UserRole> userRolePage = userRoleService.findAll(USERID + userId, pageable);
		while (!userRolePage.isEmpty()) {
			pageable = pageable.next();
			userRoleList.addAll(userRolePage.getContent());
			userRolePage = userRoleService.findAll(USERID + userId, pageable);
		}
		if (!userRoleList.isEmpty()) {
			roles.addAll(userRoleList.stream().map(userRole -> userRole.getRole().getRole())
					.collect(Collectors.toList()));
		}
        return roles;
    }

    public List<String> getUserRoleByUsername(String username) {
        final List<String> roles = new ArrayList<String>();
        final Pageable pageable = PageRequest.of(0, 200);
        final Page<GetActiveRolesByUserNameResponse> response = coreQueryExecutorServiceImpl.executeGetActiveRolesByUserName(username, pageable);
        final List<GetActiveRolesByUserNameResponse> rolesResponse = response.toList();
        for (GetActiveRolesByUserNameResponse r : rolesResponse) {
            roles.add(r.getRole());
        }
        return roles;
    }

    public Role createRole(String role) {
        Role fraudRole = null;
        try {
            fraudRole = roleService.getByRole(role);
        } catch (EntityNotFoundException e) {
            fraudRole = new Role();
            fraudRole.setRole(role);
            fraudRole = roleService.create(fraudRole);
        }
        return fraudRole;
    }
}
