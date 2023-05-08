/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.pscomponent.usermanagementservice;

import com.fico.dmp.core.*;
import com.fico.dmp.telusagentuidb.Group;
import com.fico.dmp.telusagentuidb.GroupRole;
import com.fico.dmp.telusagentuidb.Permission;
import com.fico.dmp.telusagentuidb.Role;
import com.fico.dmp.telusagentuidb.RolePermission;
import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.UserGroup;
import com.fico.dmp.telusagentuidb.models.query.GetPermissionByUserIdResponse;
import com.fico.dmp.telusagentuidb.service.*;
import com.fico.pscomponent.handlers.PropertiesHandler;
import com.fico.pscomponent.handlers.RoleManagementHandler;
import com.fico.pscomponent.handlers.UserManagementHandler;
import com.fico.pscomponent.model.UserDTO;
import com.wavemaker.runtime.WMAppContext;
import com.wavemaker.runtime.data.model.FawbApplicationSessionContext;
import com.wavemaker.runtime.hazelcast.FawbAppHazelcastInstance;
import com.wavemaker.runtime.hazelcast.repository.FawbAppHazelcastSessionRepository;
import com.wavemaker.runtime.security.WMUser;
import com.wavemaker.runtime.security.dmp.WMAppDmpAuthenticationToken;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.util.FawbAppCookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExposeToClient
public class UserManagementService {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserManagementHandler userManagementHandler;

    @Autowired
    private RoleManagementHandler roleManagementHandler;

    @Autowired
    private TELUSAgentUIDBQueryExecutorServiceImpl coreQueryExecutorServiceImpl;

    @Autowired
    private PropertiesHandler propertiesHandler;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private GroupRoleService groupRoleService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private Environment environment;

    @Autowired
    private FawbAppHazelcastInstance fawbAppHazelcastInstance;

    public ResponseEntity<String> createUser(UserDTO user) {
        return userManagementHandler.createUser(user);
    }

    public ResponseEntity<String> updateUser(UserDTO user) {
        return userManagementHandler.updateUser(user);
    }

    public ResponseEntity<String> deletUser(String userId) {
        return userManagementHandler.deleteUser(userId);
    }

    public List<String> getAllRoles() {
        return roleManagementHandler.getAllRoles();
    }

    public List<String> getLoggedInUserRoles() {
        return userManagementHandler.getLoggedInUserRoles();
    }

    public boolean createSystemRoles() {
        return roleManagementHandler.createSystemRoles();
    }

    public UserDTO getLoggedInUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userManagementHandler.getLoggedInUserSecurityId());
        userDTO.setEmail(userManagementHandler.getLoggedInUserSecurityId());
        userDTO.setFirstName((userManagementHandler.getLoggedInUserName().split(" "))[0]);
        userDTO.setLastName((userManagementHandler.getLoggedInUserName().split(" "))[1]);
        userDTO.setStatus("Active");
        userDTO.setRole("Role");
        userDTO.setActive(true);
        userDTO.setEmplId(userManagementHandler.getLoggedInUser().getEmplId());
        return userDTO;

    }

    public User getLoggedInUserId() {
        return userManagementHandler.getLoggedInUser();
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        try {
            userDTOList = userManagementHandler.getAllUsers();
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Unexpected occurred while fetching User(s) for application", e);
        }
        return userDTOList;
    }

    public ResponseEntity<Object> activateDeactivateUsers(String userJSONList) {
        try {
            userManagementHandler.activateDeactivateUsers(userJSONList);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Unexpected error occurred while trying to bulk activate/de-activate User(s)", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> importUsers(String importFileContents) {
        try {
            userManagementHandler.importUsers(importFileContents);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Unexpected error occurred while trying to import User(s)", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public List<GetPermissionByUserIdResponse> executeGetPermissionByUserId() throws Exception {
        try {
            Pageable pageable = PageRequest.of(0, 2000);
            User loggedUser = userManagementHandler.getLoggedInUser();
            if (loggedUser != null) {
                //check if the logged in USER is active
                if (loggedUser.isActive())
                    return coreQueryExecutorServiceImpl.executeGetPermissionByUserId(loggedUser.getUserId(), pageable).toList();
                else
                    return new ArrayList<>();
            } else
                throw new javax.persistence.EntityNotFoundException("Unable to fetch user permissions as logged in user's >> 'UserId' was not found!");
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Unexpected error occurred while fetching user permissions", e);
            throw e;
        }
    }

    public ResponseEntity<Object> updateUserLimitAndTimeZone(Integer userId, Double lendingLimit, String timeZone) {
        try {
            if (userId != null) {
                User userDTO = userService.findById(userId);
                if (userDTO != null) {
                    userDTO.setLendingLimit(lendingLimit != null ? Double.valueOf(lendingLimit) : null);
                    userDTO.setTimeZone(timeZone != null ? timeZone : null);
                    userDTO = userService.update(userDTO);

                    return new ResponseEntity<>(HttpStatus.OK);
                } else
                    throw new Exception("No User found with supplied Id");
            } else
                throw new Exception("UserId is null");
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Unexpected error occurred while trying to update User", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public String getUserOrDefaultTimeZone() {
        try {
            String timeZone = null;
            User loggedUser = userManagementHandler.getLoggedInUser();

            if (loggedUser != null && loggedUser.getTimeZone() != null)
                timeZone = loggedUser.getTimeZone();
            else
                timeZone = propertiesHandler.getPropertyValueByName("DEFAULT_TIMEZONE");

            return (timeZone == null ? "UTC" : timeZone);
        } catch (Exception e) {
            if (logger.isWarnEnabled())
                logger.warn("Unexpected error occurred while fetching User's timezone. Defaulting to 'UTC'", e);
            return "UTC";
        }
    }

    public void refreshRoles(HttpServletRequest request) {

        final Cookie authCookie = FawbAppCookieUtil.getAuthCookie(request);
        final String autoCookieStr = authCookie.getValue(); //this is the cookie that is the key to entry on hazelcast

        final FawbAppHazelcastSessionRepository fawbAppHazelcastSessionRepository = WMAppContext.getInstance().getSpringBean(FawbAppHazelcastSessionRepository.class);  //This bean is part of project security .
        final FawbApplicationSessionContext sessionContext = fawbAppHazelcastSessionRepository.get(autoCookieStr);

//        final IMap<String, FawbApplicationSessionContext> hazelcastSessions = fawbAppHazelcastInstance.getSessionContextMap();
//        for(String sessionId : hazelcastSessions.keySet()){
//            logger.info("inside hazelcast: " + sessionId + " and " + hazelcastSessions.get(sessionId));
//        }

        //org.springframework.security.core.context.SecurityContextImpl
        final SecurityContextImpl securityContext = (SecurityContextImpl) sessionContext.getSecurityContext();
        //logger.info("Type of SecurityContext : " + securityContext.getClass());//org.springframework.security.core.context.SecurityContextImpl

        final WMAppDmpAuthenticationToken authentication = (WMAppDmpAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final WMUser principal = (WMUser) authentication.getPrincipal();

        final List<String> customRoles = new ArrayList<>();
        customRoles.addAll(roleManagementHandler.getUserRoleByUsername(principal.getUserId()));

        final Set<GrantedAuthority> updatedAuthorities = new HashSet<>(authentication.getAuthorities());
        customRoles.stream().forEach(s -> {
            //updatedAuthorities.add(new SimpleGrantedAuthority(s)); //not sure about that
            updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + s));
        });

        final WMAppDmpAuthenticationToken newAuth = new WMAppDmpAuthenticationToken(principal, updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        securityContext.setAuthentication(newAuth);

        fawbAppHazelcastSessionRepository.put(autoCookieStr, autoCookieStr);
    }

    public boolean isSystemOnBootstrapStatus() {
//        try{
//            IMap values = this.fawbAppHazelcastInstance.getSessionContextMap();
//            logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//            values.entrySet().stream().forEach(o -> {
//                logger.info("Entry {}, type {}", o, o.getClass());
//            });
//            logger.info("--------------------------------");
//            values.values().stream().forEach(o -> {
//                logger.info("Value {}, type {}", o, o.getClass());
//            });
//            logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//        }//com.wavemaker.runtime.data.model.FawbApplicationSessionContext
//        catch (Throwable t){
//            logger.error(t.getMessage());
//            t.printStackTrace();
//        }

        try {
            final Pageable pageable = PageRequest.of(0, 10);
            final Page<User> users = userService.findAll("", pageable);
            return (users == null || users.isEmpty());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public void setupSuperUsers(HttpServletRequest request, String newUsers) {

        if (isSystemOnBootstrapStatus() && newUsers != null) {

            final String defaultPermissions = environment.getProperty("app.environment.app.standard.permissions");

            final Pageable pageable = PageRequest.of(0, 100);

            //create all permissions on the DB
            final List<Integer> permissionIds = new ArrayList<>();
            final String[] permissions = defaultPermissions.split(",");
            for (String permission : permissions) {
                Permission p = new Permission();
                p.setName(permission);
                p.setDescription(permission);
                p = permissionService.create(p);
                permissionIds.add(p.getId());
            }

            //get All roles
            roleManagementHandler.createSystemRoles();
            final Set<Integer> rolesIds = new HashSet<>();
            final Page<Role> pageRoles = roleService.findAll("", pageable);
            pageRoles.stream().forEach(role -> {
                rolesIds.add(role.getId());
            });

            //create a role that will hold reference to all permissions
            Role allPermissionsRole = new Role();
            allPermissionsRole.setDescription("AllPermissions");
            allPermissionsRole.setRole("AllPermissions");
            allPermissionsRole.setActive(true);
            allPermissionsRole.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
            allPermissionsRole.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            try {
                allPermissionsRole = roleService.create(allPermissionsRole);
            } catch (RuntimeException e) {
                allPermissionsRole = roleService.getByRole(allPermissionsRole.getRole());
            }
            rolesIds.add(allPermissionsRole.getId());

            //set all permissions to the AllPermissions role
            for (Integer permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setPermissionId(permissionId);
                rolePermission.setRoleId(allPermissionsRole.getId());
                rolePermissionService.create(rolePermission);
            }

            //Create default superuser group
            Group superGroup = groupService.getByName("SUPERUSERS");
            if (superGroup == null || superGroup.getId() == null) {
                superGroup = new Group();
                superGroup.setActive(true);
                superGroup.setDescription("SuperUsers group");
                superGroup.setName("SUPERUSERS");
                superGroup.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                superGroup.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
                superGroup = groupService.create(superGroup);
            }

            //Bind roles to SUPERUSER
            for (Integer roleId : rolesIds) {
                GroupRole groupRole = new GroupRole();
                groupRole.setGroupId(superGroup.getId());
                groupRole.setRoleId(roleId);
                groupRoleService.create(groupRole);
            }

            //Create users
            final Set<Integer> userIds = new HashSet<>();
            final String[] emails = newUsers.split("\\n");
            for (String userID : emails) {
                User user = new User();
                user.setUserId(userID.replace(";", "").replace(",", ""));
                user.setEmail(userID);
                user.setFirstName(userID);
                user.setLastName("");
                user.setActive(true);
                user.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                user = userService.create(user);
                userIds.add(user.getId());
            }

            //bind user to SUPERGROUP
            for (Integer userid : userIds) {
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(superGroup.getId());
                userGroup.setUserId(userid);
                userGroupService.create(userGroup);
            }

            refreshRoles(request);
        } else {
            logger.error("System already has configured user");
            throw new IllegalArgumentException("The system already has users configured");
        }
    }
}
