package com.fico.pscomponent.handlers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fico.dmp.telusagentuidb.Role;
import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.UserRole;
import com.fico.dmp.telusagentuidb.service.UserRoleService;


import com.fico.dmp.telusagentuidb.service.RoleService;
import com.fico.dmp.telusagentuidb.service.UserService;
import com.fico.pscomponent.constant.PSComponentConstants;
import com.fico.pscomponent.model.UserDTO;
import com.fico.pscomponent.service.user.UserValidationService;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.security.SecurityService;

@Service
public class UserManagementHandler {

	@Autowired
	@Qualifier("TELUSAgentUIDB.UserService")
	private UserService userService;

	//@Autowired
	//private AlertService alertService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private UserValidationService userValidatorService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private RoleManagementHandler roleMangementHandler;

	private static final Logger logger = LoggerFactory.getLogger(UserManagementHandler.class);

	private static final String USERID = "UserId=";

	public ResponseEntity<String> createUser(UserDTO userDTO) {
		try {
		    //logger.info("In handler createUser :::::::::::::::::::::");
		userValidatorService.validateUserBody(userDTO);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		// Check if user id and email id already exists
		try {
			userService.getByUserId(userDTO.getUserId());
			return new ResponseEntity<String>("User Id already exists", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {

		}

		try {
			userService.getByEmail(userDTO.getEmail());
			return new ResponseEntity<String>("Email Id already exists", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {

		}
		//logger.info("In handler before object assigning :::::::::::::::::::::");
		
	   System.out.println(" Team ID================================" +userDTO.getRole());

		User user = new User();
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setUserId(userDTO.getUserId());
		user.setEmail(userDTO.getEmail());
		user.setActive(userDTO.isActive());
		user.setCreatedOn(new Timestamp(System.currentTimeMillis()));

		try {
		    
		    	// Only Super user has permission to upgrade a role to super user
			if (!isAllowedToUpgradeToSuperUser(userDTO.getRole())) {
				return new ResponseEntity<String>("User does not have permission", HttpStatus.BAD_REQUEST);
			}

			// Get the assigned roles from FRAUDROLE table
			Role role = roleService.getByRole(userDTO.getRole());


			// Create user entry
			user = userService.create(user);
			
			// Create a user and role relationship
			UserRole userRole = new UserRole();
			userRole.setUser(user);
			userRole.setRole(role);
            logger.info("In handler before create userrole :::::::::::::::::::::");
			userRoleService.create(userRole);

			return new ResponseEntity<String>("User created successfully", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception creating user: ", e);
			return new ResponseEntity<String>("Failed to create User", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> updateUser(UserDTO userDTO) {
		try {
			userValidatorService.validateUserBody(userDTO);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		try {
			// Only Super user has permission to upgrade a role to super user
			if (!isAllowedToUpgradeToSuperUser(userDTO.getRole())) {
				return new ResponseEntity<String>("User does not have permission", HttpStatus.BAD_REQUEST);
			}

			User dbUser = userService.getByUserId(userDTO.getEmail());

			// Check if details really changed
			if (!userDTO.getFirstName().equals(dbUser.getFirstName())) {
				dbUser.setFirstName(userDTO.getFirstName());
			}
			if (!userDTO.getLastName().equals(dbUser.getLastName())) {
				dbUser.setLastName(userDTO.getLastName());
			}
			if (!userDTO.getEmail().equalsIgnoreCase(dbUser.getEmail())) {
				// Check if email address is already present
				try {
					userService.getByEmail(userDTO.getEmail());
					return new ResponseEntity<String>("Email Id already exists", HttpStatus.BAD_REQUEST);
				} catch (EntityNotFoundException e) {
					dbUser.setEmail(userDTO.getEmail());
				}
			}

			dbUser.setUpdatedOn(new Timestamp(System.currentTimeMillis()));

			dbUser = userService.update(dbUser);
			
				Pageable pageable = PageRequest.of(0, 1);
			UserRole userRole = null;
			Page<UserRole> userRolePage = userRoleService.findAll(USERID + dbUser.getId(), pageable);
			if (userRolePage.hasContent()) {
				List<UserRole> userRoleList = userRolePage.getContent();
				if (!userRoleList.isEmpty()) {
					userRole = userRoleList.get(0);
				}
			}

			if (!Objects.isNull(userRole)) {
				if (!userRole.getRole().getRole().equals(userDTO.getRole())) {
					Role role = roleService.getByRole(userDTO.getRole());
					userRole.setRole(role);
					userRoleService.update(userRole);
				}
			} else {
				userRole = new UserRole();
				Role role = roleService.getByRole(userDTO.getRole());
				userRole.setRole(role);
				userRole.setUser(dbUser);
				userRoleService.create(userRole);
			}

		} catch (Exception e) {
			logger.error("Exception updating user: ", e);
			return new ResponseEntity<String>("Failed to Update User", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("User updated successfully ", HttpStatus.OK);
	}

	private boolean isAllowedToUpgradeToSuperUser(String role) {
		if (role.equals(PSComponentConstants.SUPERUSER)) {
			List<String> loggedInUserRoles = getLoggedInUserRoles();
			// IsEmpty check added to allow user update option from end point
			if (!loggedInUserRoles.isEmpty() && !loggedInUserRoles.contains(PSComponentConstants.SUPERUSER)) {
				return false;
			}
		}
		return true;
	}

	private boolean isUserAllowedToDeleteSuperUser(String userId) {
		List<String> loggedInUserRoles = getLoggedInUserRoles();
		List<String> roleList = getUserRolesByUserId(userId);
		// IsEmpty check added to allow user update option from end point
		if (roleList.contains(PSComponentConstants.SUPERUSER) && !loggedInUserRoles.isEmpty()
				&& !loggedInUserRoles.contains(PSComponentConstants.SUPERUSER)) {
			return false;
		}
		return true;
	}

	public ResponseEntity<String> deleteUser(String userId) {
		try {
			User dbUser = userService.getByUserId(userId);

			// Only Super user has permission to delete Super User
			if (!isUserAllowedToDeleteSuperUser(dbUser.getUserId())) {
				return new ResponseEntity<String>("User does not have permission", HttpStatus.BAD_REQUEST);
			}

			// Check if links to user are present
			// If present delete that link
			/*
			 * getAllAlerts().stream().forEach(alert -> { if
			 * (!Objects.isNull(alert.getUser())) { alert.setUser(null);
			 * alert.setUserId(null); alertService.update(alert); } });
			 */

			userService.delete(dbUser);
		} catch (Exception e) {
			logger.error("Exception delete user: ", e);
			return new ResponseEntity<String>("Failed to delete User", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("User deleted successfully", HttpStatus.OK);
	}

	public List<String> getLoggedInUserRoles() {
		List<String> roles = new ArrayList<String>();
		try {
			String userId = securityService.getUserName();
			User user = userService.getByUserId(userId);
		} catch (Exception e) {
			logger.error("Error fetching roles of logged in user: ", e);
		}
		return roles;
	}

	public List<String> getUserRolesByUserId(String userId) {
		List<String> roles = new ArrayList<String>();
		try {
			User user = userService.getByUserId(userId);
		} catch (Exception e) {
			logger.error("Error fetching roles of logged in user: ", e);
		}
		return roles;
	}
	
	public String getLoggedInUserSecurityId()
	{
		
		return securityService.getUserId();
	}

	public User getLoggedInUser() {
		// Get the current logged in user
		String loggedInUser = securityService.getUserId();

		logger.info("Logged in User "+ loggedInUser);
		try {
			return userService.getByUserId(loggedInUser);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public String getLoggedInUserName() {
		return securityService.getUserName();
	}
	
	public List<UserDTO> getAllUsers() throws Exception {
		//logger.info("----Inside method 'getAllUsers'");
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
	    List<User> usersList = userService.findAll("" , PageRequest.of(0, Integer.MAX_VALUE)).toList();
	    	if(usersList == null || usersList.isEmpty());
	    	else{
	    	    usersList.forEach((user) -> {
					try {
					    UserDTO userDTO = new UserDTO();
					    userDTO.setFirstName(user.getFirstName());
					    userDTO.setLastName(user.getLastName());
                		userDTO.setUserId(user.getUserId());
                		userDTO.setEmail(user.getEmail());
                		userDTO.setActive(user.isActive());
                		if(userDTO != null)
							userDTOs.add(userDTO);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	    	}
    	return userDTOs;
	}

	/**
	 * Bulk activates a list of Users with the supplied user Id & the required active status within the
	 * supplied JSON user's list. The expected JSON format of user's list is: <br>
	 * <b>{"userId":1,"isActivate":true}</b>
	 * @param userJSONList
	 * @throws Exception
	 */
	public void activateDeactivateUsers(String userJSONList) throws Exception {
		//logger.info("-----Inside method 'activateDeactivateUsers'");
		
		if(userJSONList == null || userJSONList.isEmpty());
		else {
			userJSONList = userJSONList.replaceAll("&quot;", "\"");
			ObjectMapper mapper = new ObjectMapper();
			BulkActivationWrapper[] serializedUserList = mapper.readValue(userJSONList, BulkActivationWrapper[].class);
			
			if(serializedUserList != null && serializedUserList.length > 0) {
				for(BulkActivationWrapper serializedUser : serializedUserList) {
					User userDTO = userService.findById(serializedUser.getUserId());
					if(userDTO != null) {
						userDTO.setActive(serializedUser.getIsActivate() != null ? serializedUser.getIsActivate().booleanValue() : false);
						userService.update(userDTO);
					}
				}
			}
		}
		
		//logger.info("-----Inside method 'activateDeactivateUsers'. Bulk activated/de-activated User(s) successfully");
	}
	
	/**
	 * Import User configuration from the supplied file content
	 * @param importFileContents
	 * The <b>Base-64</b> encoded uploaded JSON file contents
	 * @throws Exception
	 */
	public void importUsers(String importFileContents) throws Exception {
		//logger.info("-----Inside method 'importFileContents'");
		
		if(importFileContents != null && !importFileContents.isEmpty()) {
			final byte[] decodedContents = Base64.getDecoder().decode(importFileContents);
			if(decodedContents.length > 0) {
				ObjectMapper mapper = new ObjectMapper();
				ArrayNode rootNode = mapper.readValue(decodedContents, ArrayNode.class);
				
				if(rootNode != null) {
					for(JsonNode userNode : rootNode) {
						final ObjectNode userObjectNode = (ObjectNode) userNode;
						boolean userExists = false;
						User user = null;
						try {
							user = userService.getByUserId(userObjectNode.get("userId").asText());
							userExists = true;
						}
						catch (EntityNotFoundException e) {}
						
						//create/update user information block
						if(!userExists) {
							user = new User();
							user.setEmail(userObjectNode.get("Emailaddress").asText());
							user.setUserId(userObjectNode.get("userId").asText());
							user.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
						}
						user.setFirstName(userObjectNode.get("firstName").asText());
						user.setLastName(userObjectNode.get("lastName").asText());
						user.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
						
						if(userObjectNode.get("IsActive") != null)
							user.setActive(userObjectNode.get("IsActive").asBoolean());
						if(userObjectNode.get("lendingLimit") != null)
							user.setLendingLimit(userObjectNode.get("lendingLimit").asDouble());
						
						
						if(!userExists)
							user = userService.create(user);
						else
							user = userService.update(user);	
					} //iteration per user object in the JSON content
					
					//logger.info("-----Inside method 'importFileContents'. User(s) imported successfully");
				}
			}
		} //check on the supplied contents
	}
	
	/*
	 * private List<Alert> getAllAlerts() { Pageable pageable = PageRequest.of(0,
	 * 1000); Page<Alert> alertPage = alertService.findAll("", pageable);
	 * List<Alert> alertPageList = new ArrayList<Alert>();
	 * 
	 * while (!alertPage.isEmpty()) { pageable = pageable.next();
	 * alertPageList.addAll(alertPage.getContent()); alertPage =
	 * alertService.findAll("", pageable); }
	 * 
	 * return alertPageList; }
	 */

//	public String getLoggedInUserName() {
//		String loggedInUser = securityService.getUserName();
//		if (!Objects.isNull(loggedInUser)) {
//			loggedInUser = Arrays.asList(loggedInUser.split("\\s")).stream().collect(Collectors.joining());
//		}
//		return loggedInUser;
//	}
	
	@JsonInclude(value = Include.NON_NULL)
	static private class BulkActivationWrapper {
		private Integer userId;
		private Boolean isActivate;
		
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		public Boolean getIsActivate() {
			return isActivate;
		}
		public void setIsActivate(Boolean isActivate) {
			this.isActivate = isActivate;
		}
	}
}
