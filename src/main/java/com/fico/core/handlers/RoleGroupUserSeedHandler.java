package com.fico.core.handlers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fico.dmp.telusagentuidb.Group;
import com.fico.dmp.telusagentuidb.GroupRole;
import com.fico.dmp.telusagentuidb.Permission;
import com.fico.dmp.telusagentuidb.Role;
import com.fico.dmp.telusagentuidb.RolePermission;
import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.UserGroup;
import com.fico.dmp.telusagentuidb.service.GroupRoleService;
import com.fico.dmp.telusagentuidb.service.GroupService;
import com.fico.dmp.telusagentuidb.service.PermissionService;
import com.fico.dmp.telusagentuidb.service.RolePermissionService;
import com.fico.dmp.telusagentuidb.service.RoleService;
import com.fico.dmp.telusagentuidb.service.UserGroupService;
import com.fico.dmp.telusagentuidb.service.UserService;
import com.fico.ps.exception.FileHandlerException;
import com.fico.pscomponent.handlers.UserManagementHandler;

@Service("handler.RoleGroupUserSeedHandler")
public class RoleGroupUserSeedHandler {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	@Autowired
	private RolePermissionService rolePermissionService;

	@Autowired
	private GroupRoleService groupRoleService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserManagementHandler userManagementHandler;

	private static final Logger logger = LoggerFactory.getLogger(RoleGroupUserSeedHandler.class);

	//this is an upper limit imposed on the total number of rows to be processed on any excel file imports
	private static final int MAX_LOOPS = 100000; 
	
	// Exception message when Excel FILE doesn't contains valid tabs/columns
	private static final String FILE_TAB_COLUMNS_EXCEPTION_MSG = "File uploaded must contain specific tabs and columns";
	// Exception message when Excel FILE doesn't contains specific sheet
	private static final String MISSING_SHEET_EXCEPTION_MSG = "File uploaded is missing required sheet";

	// Workbook sheet names for Role -> Group -> User & related associations seed
	private static final String PERMISSION_SHEET = "Permissions";
	private static final String ROLE_SHEET = "Roles";
	private static final String GROUP_SHEET = "Groups";
	private static final String USER_SHEET = "Users";
	private static final String ROLE_PERMISSION_SHEET = "Role_Permission";
	private static final String GROUP_ROLE_SHEET = "Group_Role";
	private static final String GROUP_USER_SHEET = "Group_User";

	/**
	 * Seeds the <b>ROLE, GROUP, ROLE_PERMISSION, GROUP_ROLE and USER_GROUP</b> data
	 * and relations from the supplied sheets
	 * 
	 * @param permissionSheet
	 * @param roleSheet
	 * @param userSheet
	 * @param rolePermissionSheet
	 * @param groupSheet
	 * @param groupRoleSheet
	 * @param userGroupSheet
	 */
	public void importRoleGroupUserAndAssociations(XSSFSheet permissionSheet, XSSFSheet roleSheet, XSSFSheet userSheet,
			XSSFSheet rolePermissionSheet, XSSFSheet groupSheet, XSSFSheet groupRoleSheet, XSSFSheet userGroupSheet)
			throws Exception {
		if(logger.isInfoEnabled())
			logger.info("### Started seeding of PERMISSION, ROLE, GROUP, USER and associated relations ###");

		seedPermissionData(permissionSheet);
		seedRoleData(roleSheet);
		seedGroupData(groupSheet);
		seedUserData(userSheet);
		seedRolePermissionData(rolePermissionSheet);
		seedGroupRoleData(groupRoleSheet);
		seedGroupUserData(userGroupSheet);
	}

	/**
	 * Exports the <b>PERMISSION, ROLE, GROUP, USER, ROLE_PERMISSION, GROUP_ROLE and
	 * USER_GROUP</b> data and relations from the system
	 * 
	 * @param permissionSheet
	 * @param roleSheet
	 * @param rolePermissionSheet
	 * @param groupSheet
	 * @param groupRoleSheet
	 * @param userGroupSheet
	 * @param userSheet
	 * @throws Exception
	 */
	public void exportRoleGroupUserAndAssociations(XSSFSheet permissionSheet, XSSFSheet roleSheet,
			XSSFSheet rolePermissionSheet, XSSFSheet groupSheet, XSSFSheet groupRoleSheet, XSSFSheet userGroupSheet,
			XSSFSheet userSheet) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportRoleGroupUserAndAssociations'");

		exportPermissions(permissionSheet);
		exportRoleData(roleSheet);
		exportGroupData(groupSheet);
		exportUserData(userSheet);
		exportRolePermissionData(rolePermissionSheet);
		exportGroupRoleData(groupRoleSheet);
		exportUserGroupData(userGroupSheet);
	}

	/**
	 * Seeds PERMISSIONs into the system
	 * 
	 * @param permissionSheet
	 * @throws Exception
	 */
	public void importPermissionsFromSheet(XSSFSheet permissionSheet) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importPermissionsFromSheet'");

		seedPermissionData(permissionSheet);
	}

	/**
	 * Exports PERMISSIONs from the system
	 * 
	 * @param permissionSheet
	 * @throws Exception
	 */
	public void exportPermissionsFromSystem(XSSFSheet permissionSheet) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportPermissionsFromSystem'");

		exportPermissions(permissionSheet);
	}

	/**
	 * Seeds ROLEs and ROLE_PERMISSION into the system
	 * 
	 * @param roleSheet
	 * @param rolePermissionSheet
	 * @throws Exception
	 */
	public void importRoleAndPermissionAssociationFromSheet(XSSFSheet roleSheet, XSSFSheet rolePermissionSheet)
			throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importRoleAndPermissionAssociationFromSheet'");

		seedRoleData(roleSheet);
		seedRolePermissionData(rolePermissionSheet);
	}

	/**
	 * Exports ROLEs and ROLE_PERMISSION into the system
	 * 
	 * @param roleSheet
	 * @param rolePermissionSheet
	 * @throws Exception
	 */
	public void exportRoleAndPermissionAssociationFromSheet(XSSFSheet roleSheet, XSSFSheet rolePermissionSheet)
			throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportRoleAndPermissionAssociationFromSheet'");

		exportRoleData(roleSheet);
		exportRolePermissionData(rolePermissionSheet);
	}

	/**
	 * Seeds USERs into the system <br>
	 * <br>
	 * <b><i>NOTE: USERs are only UPDATED if found, no CREATION is done from
	 * here</i></b>
	 * 
	 * @param userSheet
	 * @throws Exception
	 */
	public void importUsersFromSheet(XSSFSheet userSheet) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importUsersFromSheet'");

		seedUserData(userSheet);
	}

	/**
	 * Exports USERs from the system
	 * 
	 * @param userSheet
	 * @throws Exception
	 */
	public void exportUsersFromSystem(XSSFSheet userSheet) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportUsersFromSystem'");

		exportUserData(userSheet);
	}

	/**
	 * Seeds GROUPs, GROUP_ROLE and USER_GROUP into the system
	 * 
	 * @param groupSheet
	 * @param groupRoleSheet
	 * @param userGroupSheet
	 * @throws Exception
	 */
	public void importGroupAndRelatedAssociationFromSheet(XSSFSheet groupSheet, XSSFSheet groupRoleSheet,
			XSSFSheet userGroupSheet) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'importGroupAndRelatedAssociationFromSheet'");

		seedGroupData(groupSheet);
		seedGroupRoleData(groupRoleSheet);
		seedGroupUserData(userGroupSheet);
	}

	/**
	 * Exports GROUPs, GROUP_ROLE and USER_GROUP into the system
	 * 
	 * @param groupSheet
	 * @param groupRoleSheet
	 * @param userGroupSheet
	 * @throws Exception
	 */
	public void exportGroupAndRelatedAssociationFromSheet(XSSFSheet groupSheet, XSSFSheet groupRoleSheet,
			XSSFSheet userGroupSheet) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'exportGroupAndRelatedAssociationFromSheet'");

		exportGroupData(groupSheet);
		exportGroupRoleData(groupRoleSheet);
		exportUserGroupData(userGroupSheet);
	}

	/**
	 * Seeds ROLE data into the system from the uploaded Excel file
	 * 
	 * @param roleSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean seedRoleData(XSSFSheet roleSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Seeding ROLE data to system ###");

		try {
			final User loggedUser = userManagementHandler.getLoggedInUser();
			if (roleSheet != null) {
				int rowCounter = 1;
				Iterator<Row> rowItr = roleSheet.rowIterator();
				while (rowItr.hasNext() && rowCounter <= MAX_LOOPS) {
					final Row currentRow = rowItr.next();

					if (currentRow.getRowNum() == 0)
						; // skipping the header row
					else {
						final String roleName = (currentRow.getCell(0) != null
								&& !currentRow.getCell(0).getStringCellValue().isEmpty())
										? currentRow.getCell(0).getStringCellValue()
										: null;
						final String roleDesc = (currentRow.getCell(1) != null
								&& !currentRow.getCell(1).getStringCellValue().isEmpty())
										? currentRow.getCell(1).getStringCellValue()
										: null;
						final boolean roleStatus = (currentRow.getCell(2) != null)
								? currentRow.getCell(2).getBooleanCellValue()
								: false;

						// processing only for valid role names
						if (roleName != null) {
							List<Role> roles = roleService.findAll("role='" + roleName + "'", PageRequest.of(0, 1))
									.toList();
							if (roles != null && !roles.isEmpty()) {
								Role role = roles.get(0);
								role.setDescription(roleDesc);
								role.setActive(roleStatus);
								role.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
								role.setUpdatedBy(loggedUser != null ? loggedUser.getId() : null);
								if(logger.isInfoEnabled())
									logger.debug("Role '{}' was updated", roleName);
								roleService.update(role);
							} else {
								Role role = new Role();
								role.setRole(roleName);
								role.setDescription(roleDesc);
								role.setActive(roleStatus);
								role.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
								role.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
								role.setCreatedBy(loggedUser != null ? loggedUser.getId() : null);
								role.setUpdatedBy(loggedUser != null ? loggedUser.getId() : null);
								if(logger.isInfoEnabled())
									logger.debug("Role '{}' was created", roleName);
								roleService.create(role);
							}
						}
					}
					if (currentRow.getRowNum() != 0)
						rowCounter += 1;
				} // processing per row
			}

			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to seed ROLE information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Seeds ROLE_PERMISSION data into the system from the uploaded Excel file
	 * 
	 * @param rolePermissionSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean seedRolePermissionData(XSSFSheet rolePermissionSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Seeding ROLE_PERMISSION data to system ###");

		try {
			if (rolePermissionSheet != null) {
				int rowCounter = 1;
				Iterator<Row> rowItr = rolePermissionSheet.rowIterator();
				while (rowItr.hasNext() && rowCounter <= MAX_LOOPS) {
					final Row currentRow = rowItr.next();

					if (currentRow.getRowNum() == 0)
						; // skipping the header row
					else {
						final String roleName = (currentRow.getCell(0) != null
								&& !currentRow.getCell(0).getStringCellValue().isEmpty())
										? currentRow.getCell(0).getStringCellValue()
										: null;
						final String permissionName = (currentRow.getCell(1) != null
								&& !currentRow.getCell(1).getStringCellValue().isEmpty())
										? currentRow.getCell(1).getStringCellValue()
										: null;

						// processing only for valid group & role names
						if (permissionName != null && roleName != null) {
							List<Permission> permissions = permissionService
									.findAll("name='" + permissionName + "'", PageRequest.of(0, 1)).toList();
							List<Role> roles = roleService.findAll("role='" + roleName + "'", PageRequest.of(0, 1))
									.toList();

							// checking if both GROUP & ROLE exists with the supplied names
							if (permissions != null && !permissions.isEmpty() && roles != null && !roles.isEmpty()) {
								List<RolePermission> permissionRoles = rolePermissionService
										.findAll("permissionId=" + permissions.get(0).getId() + " AND roleId="
												+ roles.get(0).getId(), PageRequest.of(0, 1))
										.toList();

								// create the association record only if not already exists
								if (permissionRoles == null || permissionRoles.isEmpty()) {
									RolePermission rolePermission = new RolePermission();
									rolePermission.setPermissionId(permissions.get(0).getId());
									rolePermission.setRoleId(roles.get(0).getId());
									if(logger.isInfoEnabled())
										logger.debug(
											"Role_Permission association was created for PERMISSION: '{}' and ROLE: '{}'",
											permissionName, roleName);
									rolePermissionService.create(rolePermission);
								}
							}
						}
					}
					if (currentRow.getRowNum() != 0)
						rowCounter += 1;
				} // processing per row
			}
			if(logger.isInfoEnabled())
				logger.info("### ROLE_PERMISSION data seeded to system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException(
					"Failed to seed ROLE_PERMISSION information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Extracts ROLE_PERMISSION data from the system into the supplied sheet
	 * 
	 * @param rolePermissionSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean exportRolePermissionData(XSSFSheet rolePermissionSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Extracting ROLE_PERMISSION data from system ###");

		try {
			int rowNum = 1;
			if (rolePermissionSheet != null) {
				// create the header row
				final Row headerRow = rolePermissionSheet.createRow(0);
				final Cell roleNameCell = headerRow.createCell(0, CellType.STRING);
				roleNameCell.setCellValue("Role");
				final Cell permissionNameCell = headerRow.createCell(1, CellType.STRING);
				permissionNameCell.setCellValue("Permission");
				addCellBackground(roleNameCell);
				addCellBackground(permissionNameCell);

				// fetch all permission roles from system
				List<RolePermission> rolePermissions = rolePermissionService
						.findAll("permissionId != null AND roleId != null", PageRequest.of(0, Integer.MAX_VALUE))
						.toList();

				if (rolePermissions != null && !rolePermissions.isEmpty()) {
					for (RolePermission rp : rolePermissions) {
						// fetch relations only when ROLE is active
						if (rp.getRole().isActive()) {
							Row dataRow = rolePermissionSheet.createRow(rowNum);
							final Cell roleNameDCell = dataRow.createCell(0, CellType.STRING);
							final Cell permissionNameDCell = dataRow.createCell(1, CellType.STRING);
							roleNameDCell.setCellValue(rp.getRole().getRole());
							permissionNameDCell.setCellValue(rp.getPermission().getName());

							rowNum += 1;
						}
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("### ROLE_PERMISSION data extracted from system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException(
					"Failed to extract ROLE_PERMISSION information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Extracts ROLE data from the system into the supplied sheet
	 * 
	 * @param roleSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean exportRoleData(XSSFSheet roleSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Extracting ROLE data from system ###");

		try {
			int rowNum = 1;
			if (roleSheet != null) {
				// create the header row
				final Row headerRow = roleSheet.createRow(0);
				final Cell nameCell = headerRow.createCell(0, CellType.STRING);
				nameCell.setCellValue("Role");
				final Cell descCell = headerRow.createCell(1, CellType.STRING);
				descCell.setCellValue("Description");
				final Cell statusCell = headerRow.createCell(2, CellType.STRING);
				statusCell.setCellValue("IsActive");
				addCellBackground(nameCell);
				addCellBackground(descCell);
				addCellBackground(statusCell);

				// fetch all roles from system
				List<Role> roles = roleService.findAll("active=true",
						PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "role"))).toList();
				if (roles != null && !roles.isEmpty()) {
					for (Role r : roles) {
						Row dataRow = roleSheet.createRow(rowNum);
						final Cell nameDCell = dataRow.createCell(0, CellType.STRING);
						final Cell descDCell = dataRow.createCell(1, CellType.STRING);
						final Cell statusDCell = dataRow.createCell(2, CellType.BOOLEAN);
						nameDCell.setCellValue(r.getRole());
						descDCell.setCellValue(r.getDescription());
						statusDCell.setCellValue(r.isActive());

						rowNum += 1;
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("### ROLE data extracted from system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to extract ROLE information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Seeds GROUP data into the system from the uploaded Excel file
	 * 
	 * @param groupSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean seedGroupData(XSSFSheet groupSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Seeding GROUP data to system ###");

		try {
			final User loggedUser = userManagementHandler.getLoggedInUser();
			if (groupSheet != null) {
				int rowCounter = 1;
				Iterator<Row> rowItr = groupSheet.rowIterator();
				while (rowItr.hasNext() && rowCounter <= MAX_LOOPS) {
					final Row currentRow = rowItr.next();

					if (currentRow.getRowNum() == 0)
						; // skipping the header row
					else {
						final String groupName = (currentRow.getCell(0) != null
								&& !currentRow.getCell(0).getStringCellValue().isEmpty())
										? currentRow.getCell(0).getStringCellValue()
										: null;
						final String groupDesc = (currentRow.getCell(1) != null
								&& !currentRow.getCell(1).getStringCellValue().isEmpty())
										? currentRow.getCell(1).getStringCellValue()
										: null;
						final Double groupLendLimit = currentRow.getCell(2) != null
								? currentRow.getCell(2).getNumericCellValue()
								: null;
						final boolean groupStatus = (currentRow.getCell(3) != null)
								? currentRow.getCell(3).getBooleanCellValue()
								: false;

						// processing only for valid group names
						if (groupName != null) {
							List<Group> groups = groupService.findAll("name='" + groupName + "'", PageRequest.of(0, 1))
									.toList();
							if (groups != null && !groups.isEmpty()) {
								Group group = groups.get(0);
								group.setDescription(groupDesc);
								group.setLendingLimit(groupLendLimit == null ? Double.valueOf(0)
										: new Double(groupLendLimit));
								group.setActive(groupStatus);
								group.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
								group.setUpdatedBy(loggedUser != null ? loggedUser.getId() : null);
								if(logger.isInfoEnabled())
									logger.debug("Group '{}' was updated", groupName);
								groupService.update(group);
							} else {
								Group group = new Group();
								group.setName(groupName);
								group.setDescription(groupDesc);
								group.setLendingLimit(groupLendLimit == null ? Double.valueOf(0)
										: new Double(groupLendLimit));
								group.setActive(groupStatus);
								group.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
								group.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
								group.setCreatedBy(loggedUser != null ? loggedUser.getId() : null);
								group.setUpdatedBy(loggedUser != null ? loggedUser.getId() : null);
								if(logger.isInfoEnabled())
									logger.debug("Group '{}' was created", groupName);
								groupService.create(group);
							}
						}
					}
					if (currentRow.getRowNum() != 0)
						rowCounter += 1;
				} // processing per row
			}
			if(logger.isInfoEnabled())
				logger.info("### GROUP data seeded to system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to seed GROUP information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Extracts GROUP data from the system into the supplied sheet
	 * 
	 * @param groupSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean exportGroupData(XSSFSheet groupSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Extracting GROUP data from system ###");

		try {
			int rowNum = 1;
			if (groupSheet != null) {
				// create the header row
				final Row headerRow = groupSheet.createRow(0);
				final Cell nameCell = headerRow.createCell(0, CellType.STRING);
				nameCell.setCellValue("Name");
				final Cell descCell = headerRow.createCell(1, CellType.STRING);
				descCell.setCellValue("Description");
				final Cell lendLimitCell = headerRow.createCell(2, CellType.STRING);
				lendLimitCell.setCellValue("LendingLimit");
				final Cell statusCell = headerRow.createCell(3, CellType.STRING);
				statusCell.setCellValue("IsActive");
				addCellBackground(nameCell);
				addCellBackground(descCell);
				addCellBackground(lendLimitCell);
				addCellBackground(statusCell);

				// fetch all groups from system
				List<Group> groups = groupService.findAll("active=true",
						PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "name"))).toList();
				if (groups != null && !groups.isEmpty()) {
					for (Group g : groups) {
						Row dataRow = groupSheet.createRow(rowNum);
						final Cell nameDCell = dataRow.createCell(0, CellType.STRING);
						final Cell descDCell = dataRow.createCell(1, CellType.STRING);
						final Cell lendLimitDCell = dataRow.createCell(2, CellType.NUMERIC);
						final Cell statusDCell = dataRow.createCell(3, CellType.BOOLEAN);
						nameDCell.setCellValue(g.getName());
						descDCell.setCellValue(g.getDescription());
						lendLimitDCell
								.setCellValue(g.getLendingLimit() != null ? g.getLendingLimit().doubleValue() : 0);
						statusDCell.setCellValue(g.isActive());

						rowNum += 1;
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("### GROUP data extracted from system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to extract GROUP information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Seeds GROUP_ROLE association data into the system from the uploaded Excel
	 * file
	 * 
	 * @param groupRoleSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean seedGroupRoleData(XSSFSheet groupRoleSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Seeding GROUP_ROLE data to system ###");

		try {
			if (groupRoleSheet != null) {
				int rowCounter = 1;
				Iterator<Row> rowItr = groupRoleSheet.rowIterator();
				while (rowItr.hasNext() && rowCounter <= MAX_LOOPS) {
					final Row currentRow = rowItr.next();

					if (currentRow.getRowNum() == 0)
						; // skipping the header row
					else {
						final String groupName = (currentRow.getCell(0) != null
								&& !currentRow.getCell(0).getStringCellValue().isEmpty())
										? currentRow.getCell(0).getStringCellValue()
										: null;
						final String roleName = (currentRow.getCell(1) != null
								&& !currentRow.getCell(1).getStringCellValue().isEmpty())
										? currentRow.getCell(1).getStringCellValue()
										: null;

						// processing only for valid group & role names
						if (groupName != null && roleName != null) {
							List<Group> groups = groupService.findAll("name='" + groupName + "'", PageRequest.of(0, 1))
									.toList();
							List<Role> roles = roleService.findAll("role='" + roleName + "'", PageRequest.of(0, 1))
									.toList();

							// checking if both GROUP & ROLE exists with the supplied names
							if (groups != null && !groups.isEmpty() && roles != null && !roles.isEmpty()) {
								List<GroupRole> groupRoles = groupRoleService.findAll(
										"groupId=" + groups.get(0).getId() + " AND roleId=" + roles.get(0).getId(),
										PageRequest.of(0, 1)).toList();

								// create the association record only if not already exists
								if (groupRoles == null || groupRoles.isEmpty()) {
									GroupRole groupRole = new GroupRole();
									groupRole.setGroupId(groups.get(0).getId());
									groupRole.setRoleId(roles.get(0).getId());
									if(logger.isInfoEnabled())
										logger.debug("Group_Role association was created for GROUP: '{}' and ROLE: '{}'",
											groupName, roleName);
									groupRoleService.create(groupRole);
								}
							}
						}
					}
					if (currentRow.getRowNum() != 0)
						rowCounter += 1;
				} //processing per row
			}
			if(logger.isInfoEnabled())
				logger.info("### GROUP_ROLE data seeded to system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException(
					"Failed to seed GROUP_ROLE association information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Extracts GROUP_ROLE data from the system into the supplied sheet
	 * 
	 * @param groupRoleSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean exportGroupRoleData(XSSFSheet groupRoleSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Extracting GROUP_ROLE data from system ###");

		try {
			int rowNum = 1;
			if (groupRoleSheet != null) {
				// create the header row
				final Row headerRow = groupRoleSheet.createRow(0);
				final Cell groupNameCell = headerRow.createCell(0, CellType.STRING);
				groupNameCell.setCellValue("Group");
				final Cell roleNameCell = headerRow.createCell(1, CellType.STRING);
				roleNameCell.setCellValue("Role");
				addCellBackground(groupNameCell);
				addCellBackground(roleNameCell);

				// fetch all group roles from system
				List<GroupRole> groupRoles = groupRoleService
						.findAll("groupId != null AND roleId != null", PageRequest.of(0, Integer.MAX_VALUE)).toList();

				if (groupRoles != null && !groupRoles.isEmpty()) {
					for (GroupRole gr : groupRoles) {
						// fetch relation only when both GROUP & ROLE are active
						if (gr.get_group().isActive() && gr.getRole().isActive()) {
							Row dataRow = groupRoleSheet.createRow(rowNum);
							final Cell grpNameDCell = dataRow.createCell(0, CellType.STRING);
							final Cell roleNameDCell = dataRow.createCell(1, CellType.STRING);
							grpNameDCell.setCellValue(gr.get_group().getName());
							roleNameDCell.setCellValue(gr.getRole().getRole());

							rowNum += 1;
						}
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("### GROUP_ROLE data extracted from system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to extract GROUP_ROLE information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Seeds USER_GROUP association data into the system from the uploaded Excel
	 * file
	 * 
	 * @param userGroupSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean seedGroupUserData(XSSFSheet userGroupSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Seeding USER_GROUP data to system ###");

		try {
			if (userGroupSheet != null) {
				int rowCounter = 1;
				Iterator<Row> rowItr = userGroupSheet.rowIterator();
				while (rowItr.hasNext() && rowCounter <= MAX_LOOPS) {
					final Row currentRow = rowItr.next();

					if (currentRow.getRowNum() == 0)
						; // skipping the header row
					else {
						final String groupName = (currentRow.getCell(0) != null
								&& !currentRow.getCell(0).getStringCellValue().isEmpty())
										? currentRow.getCell(0).getStringCellValue()
										: null;
						final String userEmail = (currentRow.getCell(1) != null
								&& !currentRow.getCell(1).getStringCellValue().isEmpty())
										? currentRow.getCell(1).getStringCellValue()
										: null;

						// processing only for valid group & role names
						if (groupName != null && userEmail != null) {
							List<Group> groups = groupService.findAll("name='" + groupName + "'", PageRequest.of(0, 1))
									.toList();
							List<User> users = userService.findAll("email='" + userEmail + "'", PageRequest.of(0, 1))
									.toList();

							// checking if both GROUP & USER exists with the supplied group name, emailId
							if (groups != null && !groups.isEmpty() && users != null && !users.isEmpty()) {
								List<UserGroup> userGroups = userGroupService.findAll(
										"groupId=" + groups.get(0).getId() + " AND userId=" + users.get(0).getId(),
										PageRequest.of(0, 1)).toList();

								// create the association record only if not already exists
								if (userGroups == null || userGroups.isEmpty()) {
									UserGroup userGroup = new UserGroup();
									userGroup.setGroupId(groups.get(0).getId());
									userGroup.setUserId(users.get(0).getId());
									if(logger.isInfoEnabled())
										logger.debug("User_Group association was created for GROUP: '{}' and USER: '{}'",
											groupName, userEmail);
									userGroupService.create(userGroup);
								}
							}
						}
					}
					if (currentRow.getRowNum() != 0)
						rowCounter += 1;
				} //processing per row
			}
			if(logger.isInfoEnabled())
				logger.info("### USER_GROUP data seeded to system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException(
					"Failed to seed USER_GROUP association information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Extracts USER_GROUP data from the system into the supplied sheet
	 * 
	 * @param userGroupSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean exportUserGroupData(XSSFSheet userGroupSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Extracting USER_GROUP data from system ###");

		try {
			int rowNum = 1;
			if (userGroupSheet != null) {
				// create the header row
				final Row headerRow = userGroupSheet.createRow(0);
				final Cell groupNameCell = headerRow.createCell(0, CellType.STRING);
				groupNameCell.setCellValue("Group");
				final Cell userEmailCell = headerRow.createCell(1, CellType.STRING);
				userEmailCell.setCellValue("UserEmail");
				addCellBackground(groupNameCell);
				addCellBackground(userEmailCell);

				// fetch all group users from system
				List<UserGroup> userGroups = userGroupService
						.findAll("groupId != null AND userId != null", PageRequest.of(0, Integer.MAX_VALUE)).toList();

				if (userGroups != null && !userGroups.isEmpty()) {
					for (UserGroup ug : userGroups) {
						// fetch relation only when both USER & GROUP are active
						if (ug.get_group().isActive() && ug.getUserByUserId().isActive()) {
							Row dataRow = userGroupSheet.createRow(rowNum);
							final Cell grpNameDCell = dataRow.createCell(0, CellType.STRING);
							final Cell userEmailDCell = dataRow.createCell(1, CellType.STRING);
							grpNameDCell.setCellValue(ug.get_group().getName());
							userEmailDCell.setCellValue(ug.getUserByUserId().getEmail());

							rowNum += 1;
						}
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("### USER_GROUP data extracted from system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to extract USER_GROUP information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Extracts PERMISSIONS data from the system into the supplied sheet
	 * 
	 * @param permissionSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean exportPermissions(XSSFSheet permissionSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Extracting PERMISSION data from system ###");

		try {
			int rowNum = 1;
			if (permissionSheet != null) {
				// create the header row
				final Row headerRow = permissionSheet.createRow(0);
				final Cell nameCell = headerRow.createCell(0, CellType.STRING);
				nameCell.setCellValue("Name");
				final Cell descCell = headerRow.createCell(1, CellType.STRING);
				descCell.setCellValue("Description");
				addCellBackground(nameCell);
				addCellBackground(descCell);

				// fetch all roles from system
				List<Permission> permissions = permissionService.findAll("name != null",
						PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "name"))).toList();

				if (permissions != null && !permissions.isEmpty()) {
					for (Permission p : permissions) {
						Row dataRow = permissionSheet.createRow(rowNum);
						final Cell nameDCell = dataRow.createCell(0, CellType.STRING);
						final Cell descDCell = dataRow.createCell(1, CellType.STRING);
						nameDCell.setCellValue(p.getName());
						descDCell.setCellValue(p.getDescription());

						rowNum += 1;
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("### PERMISSION data extracted from system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to extract PERMISSION information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Seeds PERMISSION data into the system from the uploaded Excel file
	 * 
	 * @param permissionSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean seedPermissionData(XSSFSheet permissionSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Seeding PERMISSION data to system ###");

		try {
			if (permissionSheet != null) {
				int rowCounter = 1;
				Iterator<Row> rowItr = permissionSheet.rowIterator();
				while (rowItr.hasNext() && rowCounter <= MAX_LOOPS) {
					final Row currentRow = rowItr.next();

					if (currentRow.getRowNum() == 0)
						; // skipping the header row
					else {
						final String permissionName = (currentRow.getCell(0) != null
								&& !currentRow.getCell(0).getStringCellValue().isEmpty())
										? currentRow.getCell(0).getStringCellValue()
										: null;
						final String permissionDesc = (currentRow.getCell(1) != null
								&& !currentRow.getCell(1).getStringCellValue().isEmpty())
										? currentRow.getCell(1).getStringCellValue()
										: null;

						// processing only for valid role names
						if (permissionName != null) {
							List<Permission> permissions = permissionService
									.findAll("name='" + permissionName + "'", PageRequest.of(0, 1)).toList();
							if (permissions != null && !permissions.isEmpty()) {
								Permission permission = permissions.get(0);
								permission.setDescription(permissionDesc);
								if(logger.isInfoEnabled())
									logger.debug("Permission '{}' was updated", permissionName);
								permissionService.update(permission);
							} else {
								Permission permission = new Permission();
								permission.setName(permissionName);
								permission.setDescription(permissionDesc);
								if(logger.isInfoEnabled())
									logger.debug("Permission '{}' was created", permissionName);
								permissionService.create(permission);
							}
						}
					}
					if (currentRow.getRowNum() != 0)
						rowCounter += 1;
				} // processing per row
			}
			if(logger.isInfoEnabled())
				logger.info("### PERMISSION data seeded to system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to seed PERMISSION information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Seeds USER data into the system from the uploaded Excel file
	 * 
	 * @param userSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean seedUserData(XSSFSheet userSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Seeding USER data to system ###");

		try {
			final User loggedUser = userManagementHandler.getLoggedInUser();
			if (userSheet != null) {
				int rowCounter = 1;
				Iterator<Row> rowItr = userSheet.rowIterator();
				while (rowItr.hasNext() && rowCounter <= MAX_LOOPS) {
					final Row currentRow = rowItr.next();

					if (currentRow.getRowNum() == 0)
						; // skipping the header row
					else {
						final String emailId = (currentRow.getCell(0) != null
								&& !currentRow.getCell(0).getStringCellValue().isEmpty())
										? currentRow.getCell(0).getStringCellValue()
										: null;
						final String userId = (currentRow.getCell(1) != null
								&& !currentRow.getCell(1).getStringCellValue().isEmpty())
										? currentRow.getCell(1).getStringCellValue()
										: null;
						final String fname = (currentRow.getCell(2) != null
								&& !currentRow.getCell(2).getStringCellValue().isEmpty())
										? currentRow.getCell(2).getStringCellValue()
										: null;
						final String lname = (currentRow.getCell(3) != null
								&& !currentRow.getCell(3).getStringCellValue().isEmpty())
										? currentRow.getCell(3).getStringCellValue()
										: null;
						final String pNumber = currentRow.getCell(4) != null ?
								 (currentRow.getCell(4).getCellType().equals(CellType.NUMERIC) ?
										 fetchNumericCellValue(currentRow.getCell(4).getNumericCellValue())
										 	: (!currentRow.getCell(4).getStringCellValue().isEmpty()
												 ? currentRow.getCell(4).getStringCellValue() : null))
													: null;
						final String timeZone = (currentRow.getCell(5) != null
								&& !currentRow.getCell(5).getStringCellValue().isEmpty())
										? currentRow.getCell(5).getStringCellValue()
										: null;
						final Double lendLimit = (currentRow.getCell(6) != null)
								? currentRow.getCell(6).getNumericCellValue()
								: null;
						final boolean status = (currentRow.getCell(7) != null)
								? currentRow.getCell(7).getBooleanCellValue()
								: false;

						// processing only for valid user email & userId
						if (emailId != null && userId != null) {
							List<User> users = userService
									.findAll("email='" + emailId + "' AND userId='" + userId + "'",
											PageRequest.of(0, 1))
									.toList();
							if (users != null && !users.isEmpty()) {
								User user = users.get(0);
								user.setFirstName(fname);
								user.setLastName(lname);
								user.setPhoneNumber(pNumber);
								user.setTimeZone(timeZone);
								user.setLendingLimit(lendLimit != null ? Double.valueOf(lendLimit) : null);
								user.setActive(status);
								user.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
								user.setUpdatedBy(loggedUser != null ? loggedUser.getId() : null);
								if(logger.isInfoEnabled())
									logger.debug("User '{}' was updated", emailId);
								userService.update(user);
							}
						}
					}
					if (currentRow.getRowNum() != 0)
						rowCounter += 1;
				} // processing per row
			}
			if(logger.isInfoEnabled())
				logger.info("### USER data seeded to system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to seed USER information. Actual cause: " + e.getMessage());
		}
	}

	/**
	 * Extracts USER data from the system into the supplied sheet
	 * 
	 * @param userSheet
	 * @return
	 * @throws Exception
	 */
	private Boolean exportUserData(XSSFSheet userSheet) throws FileHandlerException {
		if(logger.isInfoEnabled())
			logger.info("### Extracting USER data from system ###");

		try {
			int rowNum = 1;
			if (userSheet != null) {
				// create the header row
				final Row headerRow = userSheet.createRow(0);
				final Cell emailCell = headerRow.createCell(0, CellType.STRING);
				emailCell.setCellValue("EmailId");
				final Cell userIdCell = headerRow.createCell(1, CellType.STRING);
				userIdCell.setCellValue("UserId");
				final Cell fNameCell = headerRow.createCell(2, CellType.STRING);
				fNameCell.setCellValue("FirstName");
				final Cell lNameCell = headerRow.createCell(3, CellType.STRING);
				lNameCell.setCellValue("LastName");
				final Cell phoneCell = headerRow.createCell(4, CellType.STRING);
				phoneCell.setCellValue("PhoneNumber");
				final Cell timeZoneCell = headerRow.createCell(5, CellType.STRING);
				timeZoneCell.setCellValue("TimeZone");
				final Cell lendLimitCell = headerRow.createCell(6, CellType.STRING);
				lendLimitCell.setCellValue("LendingLimit");
				final Cell statusCell = headerRow.createCell(7, CellType.STRING);
				statusCell.setCellValue("IsActive");
				addCellBackground(emailCell);
				addCellBackground(userIdCell);
				addCellBackground(fNameCell);
				addCellBackground(lNameCell);
				addCellBackground(phoneCell);
				addCellBackground(timeZoneCell);
				addCellBackground(lendLimitCell);
				addCellBackground(statusCell);

				// fetch all roles from system
				List<User> users = userService.findAll("active=true",
						PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "email"))).toList();

				if (users != null && !users.isEmpty()) {
					for (User u : users) {
						Row dataRow = userSheet.createRow(rowNum);
						final Cell emailDCell = dataRow.createCell(0, CellType.STRING);
						final Cell userIdDCell = dataRow.createCell(1, CellType.STRING);
						final Cell fNameDCell = dataRow.createCell(2, CellType.STRING);
						final Cell lNameDCell = dataRow.createCell(3, CellType.STRING);
						
						final Object phoneNumberValue = setNumericCellValue(u.getPhoneNumber());
						if(phoneNumberValue != null) {
							final Cell phoneDCell = dataRow.createCell(4, CellType.NUMERIC);
							phoneDCell.setCellValue(phoneNumberValue instanceof Double ? ((Double) phoneNumberValue) : ((Long) phoneNumberValue));
						}
						else {
							final Cell phoneDCell = dataRow.createCell(4, CellType.STRING);
							phoneDCell.setCellValue(u.getPhoneNumber());
						}
						
						final Cell timeZoneDCell = dataRow.createCell(5, CellType.STRING);
						final Cell lendLimitDCell = dataRow.createCell(6, CellType.NUMERIC);
						final Cell statusDCell = dataRow.createCell(7, CellType.BOOLEAN);
						emailDCell.setCellValue(u.getEmail());
						userIdDCell.setCellValue(u.getUserId());
						fNameDCell.setCellValue(u.getFirstName());
						lNameDCell.setCellValue(u.getLastName());
						timeZoneDCell.setCellValue(u.getTimeZone());
						lendLimitDCell
								.setCellValue(u.getLendingLimit() != null ? u.getLendingLimit().doubleValue() : 0);
						statusDCell.setCellValue(u.isActive());

						rowNum += 1;
					}
				}
			}
			if(logger.isInfoEnabled())
				logger.info("### USER data extracted from system ###");
			return true;
		} catch (Exception e) {
			throw new FileHandlerException("Failed to extract USER information. Actual cause: " + e.getMessage());
		}
	}
	
	/**
	 * Checks the validity of the <b>'Permission'</b> upload workbook and
	 * the individual sheets inside it
	 * 
	 * @param workbook
	 * @throws Exception
	 */
	public void checkIfPermissionSeedValid(XSSFWorkbook workbook) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("### Checking if the seed file supplied is valid ###");

		if (workbook == null)
			throw new FileHandlerException("The workbook instance for the 'Permission' seed is null");
		else {
			final XSSFSheet permissionSheet = workbook.getSheet(PERMISSION_SHEET);

			// checking for sheet existence & column validity
			if (permissionSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + PERMISSION_SHEET);
			else {
				// checking individual sheet's cell/column validity
				// 1. Permission sheet
				final Row permissionSheetHeaderRow = permissionSheet.getRow(0);
				if (permissionSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + PERMISSION_SHEET);
				else {
					boolean hasPermissionNameCell = (permissionSheetHeaderRow.getCell(0) != null
							&& !permissionSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& permissionSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Name"));
					boolean hasPermissionDescCell = (permissionSheetHeaderRow.getCell(1) != null
							&& !permissionSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& permissionSheetHeaderRow.getCell(1).getStringCellValue()
									.equalsIgnoreCase("Description"));

					if (!(hasPermissionNameCell && hasPermissionDescCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + PERMISSION_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + PERMISSION_SHEET + " is valid");
			}
		}
	}
	
	/**
	 * Checks the validity of the <b>'Role & associations'</b> upload workbook and
	 * the individual sheets inside it
	 * 
	 * @param workbook
	 * @throws Exception
	 */
	public void checkIfRoleAndAssociationsSeedValid(XSSFWorkbook workbook) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("### Checking if the seed file supplied is valid ###");

		if (workbook == null)
			throw new FileHandlerException("The workbook instance for the 'Role & associations' seed is null");
		else {
			final XSSFSheet roleSheet = workbook.getSheet(ROLE_SHEET);
			final XSSFSheet rolePermissionSheet = workbook.getSheet(ROLE_PERMISSION_SHEET);

			// checking for sheet existence & column validity
			if (roleSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + ROLE_SHEET);
			else if (rolePermissionSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + ROLE_PERMISSION_SHEET);
			else {
				// checking individual sheet's cell/column validity
				// 1. Role sheet
				final Row roleSheetHeaderRow = roleSheet.getRow(0);
				if (roleSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + ROLE_SHEET);
				else {
					boolean hasRoleNameCell = (roleSheetHeaderRow.getCell(0) != null
							&& !roleSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& roleSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Role"));
					boolean hasRoleDescCell = (roleSheetHeaderRow.getCell(1) != null
							&& !roleSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& roleSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("Description"));
					boolean hasRoleActiveCell = (roleSheetHeaderRow.getCell(2) != null
							&& !roleSheetHeaderRow.getCell(2).getStringCellValue().isEmpty()
							&& roleSheetHeaderRow.getCell(2).getStringCellValue().equalsIgnoreCase("IsActive"));

					if (!(hasRoleNameCell && hasRoleDescCell && hasRoleActiveCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + ROLE_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + ROLE_SHEET + " is valid");

				// 2. Role_Permission sheet
				final Row rolePermissionSheetHeaderRow = rolePermissionSheet.getRow(0);
				if (rolePermissionSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + ROLE_PERMISSION_SHEET);
				else {
					boolean hasRoleNameCell = (rolePermissionSheetHeaderRow.getCell(0) != null
							&& !rolePermissionSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& rolePermissionSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Role"));
					boolean hasPermissionNameCell = (rolePermissionSheetHeaderRow.getCell(1) != null
							&& !rolePermissionSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& rolePermissionSheetHeaderRow.getCell(1).getStringCellValue()
									.equalsIgnoreCase("Permission"));

					if (!(hasRoleNameCell && hasPermissionNameCell))
						throw new FileHandlerException(
								FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + ROLE_PERMISSION_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + ROLE_PERMISSION_SHEET + " is valid");
			}
		}
	}

	/**
	 * Checks the validity of the <b>'Group & associations'</b> upload workbook and
	 * the individual sheets inside it
	 * 
	 * @param workbook
	 * @throws Exception
	 */
	public void checkIfGroupAndAssociationsSeedValid(XSSFWorkbook workbook) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("### Checking if the seed file supplied is valid ###");

		if (workbook == null)
			throw new FileHandlerException("The workbook instance for the 'Group & associations' seed is null");
		else {
			final XSSFSheet groupSheet = workbook.getSheet(GROUP_SHEET);
			final XSSFSheet groupRoleSheet = workbook.getSheet(GROUP_ROLE_SHEET);
			final XSSFSheet groupUserSheet = workbook.getSheet(GROUP_USER_SHEET);

			// checking for sheet existence & column validity
			if (groupSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + GROUP_SHEET);
			else if (groupRoleSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + GROUP_ROLE_SHEET);
			else if (groupUserSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + GROUP_USER_SHEET);
			else {
				// checking individual sheet's cell/column validity
				// 1. Group sheet
				final Row groupSheetHeaderRow = groupSheet.getRow(0);
				if (groupSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + GROUP_SHEET);
				else {
					boolean hasGrpNameCell = (groupSheetHeaderRow.getCell(0) != null
							&& !groupSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Name"));
					boolean hasGrpDescCell = (groupSheetHeaderRow.getCell(1) != null
							&& !groupSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("Description"));
					boolean hasGrpLendLimActiveCell = (groupSheetHeaderRow.getCell(2) != null
							&& !groupSheetHeaderRow.getCell(2).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(2).getStringCellValue().equalsIgnoreCase("LendingLimit"));
					boolean hasGrpActiveCell = (groupSheetHeaderRow.getCell(3) != null
							&& !groupSheetHeaderRow.getCell(3).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(3).getStringCellValue().equalsIgnoreCase("IsActive"));

					if (!(hasGrpNameCell && hasGrpDescCell && hasGrpLendLimActiveCell && hasGrpActiveCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + GROUP_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + GROUP_SHEET + " is valid");

				// 2. Group_Role sheet
				final Row grpRoleSheetHeaderRow = groupRoleSheet.getRow(0);
				if (grpRoleSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + GROUP_ROLE_SHEET);
				else {
					boolean hasGrpNameCell = (grpRoleSheetHeaderRow.getCell(0) != null
							&& !grpRoleSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& grpRoleSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Group"));
					boolean hasRoleNameCell = (grpRoleSheetHeaderRow.getCell(1) != null
							&& !grpRoleSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& grpRoleSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("Role"));

					if (!(hasGrpNameCell && hasRoleNameCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + GROUP_ROLE_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + GROUP_ROLE_SHEET + " is valid");

				// 3. Group_User sheet
				final Row grpUserSheetHeaderRow = groupUserSheet.getRow(0);
				if (grpUserSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + GROUP_USER_SHEET);
				else {
					boolean hasGrpNameCell = (grpUserSheetHeaderRow.getCell(0) != null
							&& !grpUserSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& grpUserSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Group"));
					boolean hasUserEmailCell = (grpUserSheetHeaderRow.getCell(1) != null
							&& !grpUserSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& grpUserSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("UserEmail"));

					if (!(hasGrpNameCell && hasUserEmailCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + GROUP_USER_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + GROUP_USER_SHEET + " is valid");
			}
		}
	}
	
	/**
	 * Checks the validity of the <b>'User'</b> upload workbook and
	 * the individual sheets inside it
	 * 
	 * @param workbook
	 * @throws Exception
	 */
	public void checkIfUserSeedValid(XSSFWorkbook workbook) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("### Checking if the seed file supplied is valid ###");

		if (workbook == null)
			throw new FileHandlerException("The workbook instance for the 'User' seed is null");
		else {
			final XSSFSheet userSheet = workbook.getSheet(USER_SHEET);

			// checking for sheet existence & column validity
			if (userSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + USER_SHEET);
			else {
				// checking individual sheet's cell/column validity
				// 1. User sheet
				final Row userSheetHeaderRow = userSheet.getRow(0);
				if (userSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + USER_SHEET);
				else {
					boolean hasEmailCell = (userSheetHeaderRow.getCell(0) != null
							&& !userSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("EmailId"));
					boolean hasUserIdCell = (userSheetHeaderRow.getCell(1) != null
							&& !userSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("UserId"));
					boolean hasFNameCell = (userSheetHeaderRow.getCell(2) != null
							&& !userSheetHeaderRow.getCell(2).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(2).getStringCellValue().equalsIgnoreCase("FirstName"));
					boolean hasLNameCell = (userSheetHeaderRow.getCell(3) != null
							&& !userSheetHeaderRow.getCell(3).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(3).getStringCellValue().equalsIgnoreCase("LastName"));
					boolean hasPNumberCell = (userSheetHeaderRow.getCell(4) != null
							&& !userSheetHeaderRow.getCell(4).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(4).getStringCellValue().equalsIgnoreCase("PhoneNumber"));
					boolean hasTZoneCell = (userSheetHeaderRow.getCell(5) != null
							&& !userSheetHeaderRow.getCell(5).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(5).getStringCellValue().equalsIgnoreCase("TimeZone"));
					boolean hasLendLimitNameCell = (userSheetHeaderRow.getCell(6) != null
							&& !userSheetHeaderRow.getCell(6).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(6).getStringCellValue().equalsIgnoreCase("LendingLimit"));
					boolean hasStatusCell = (userSheetHeaderRow.getCell(7) != null
							&& !userSheetHeaderRow.getCell(7).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(7).getStringCellValue().equalsIgnoreCase("IsActive"));

					if (!(hasEmailCell && hasUserIdCell && hasFNameCell && hasLNameCell && hasPNumberCell
							&& hasTZoneCell && hasLendLimitNameCell && hasStatusCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + USER_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + USER_SHEET + " is valid");
			}
		}
	}
	
	/**
	 * Checks the validity of the <b>'Role -> Group -> User'</b> upload workbook and
	 * the individual sheets inside it
	 * 
	 * @param workbook
	 * @throws Exception
	 */
	public void checkIfRoleGroupUserSeedValid(XSSFWorkbook workbook) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("### Checking if the seed file supplied is valid ###");

		if (workbook == null)
			throw new FileHandlerException("The workbook instance for the 'Role -> Group -> User' seed is null");
		else {
			final XSSFSheet permissionSheet = workbook.getSheet(PERMISSION_SHEET);
			final XSSFSheet roleSheet = workbook.getSheet(ROLE_SHEET);
			final XSSFSheet rolePermissionSheet = workbook.getSheet(ROLE_PERMISSION_SHEET);
			final XSSFSheet groupSheet = workbook.getSheet(GROUP_SHEET);
			final XSSFSheet groupRoleSheet = workbook.getSheet(GROUP_ROLE_SHEET);
			final XSSFSheet groupUserSheet = workbook.getSheet(GROUP_USER_SHEET);
			final XSSFSheet userSheet = workbook.getSheet(USER_SHEET);

			// checking for sheet existence & column validity
			if (permissionSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + PERMISSION_SHEET);
			else if (roleSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + ROLE_SHEET);
			else if (rolePermissionSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + ROLE_PERMISSION_SHEET);
			else if (groupSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + GROUP_SHEET);
			else if (groupRoleSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + GROUP_ROLE_SHEET);
			else if (groupUserSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + GROUP_USER_SHEET);
			else if (userSheet == null)
				throw new FileHandlerException(MISSING_SHEET_EXCEPTION_MSG + ": " + USER_SHEET);
			else {
				// checking individual sheet's cell/column validity
				// 1. Permission sheet
				final Row permissionSheetHeaderRow = permissionSheet.getRow(0);
				if (permissionSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + PERMISSION_SHEET);
				else {
					boolean hasPermissionNameCell = (permissionSheetHeaderRow.getCell(0) != null
							&& !permissionSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& permissionSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Name"));
					boolean hasPermissionDescCell = (permissionSheetHeaderRow.getCell(1) != null
							&& !permissionSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& permissionSheetHeaderRow.getCell(1).getStringCellValue()
									.equalsIgnoreCase("Description"));

					if (!(hasPermissionNameCell && hasPermissionDescCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + PERMISSION_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + PERMISSION_SHEET + " is valid");

				// 2. Role sheet
				final Row roleSheetHeaderRow = roleSheet.getRow(0);
				if (roleSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + ROLE_SHEET);
				else {
					boolean hasRoleNameCell = (roleSheetHeaderRow.getCell(0) != null
							&& !roleSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& roleSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Role"));
					boolean hasRoleDescCell = (roleSheetHeaderRow.getCell(1) != null
							&& !roleSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& roleSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("Description"));
					boolean hasRoleActiveCell = (roleSheetHeaderRow.getCell(2) != null
							&& !roleSheetHeaderRow.getCell(2).getStringCellValue().isEmpty()
							&& roleSheetHeaderRow.getCell(2).getStringCellValue().equalsIgnoreCase("IsActive"));

					if (!(hasRoleNameCell && hasRoleDescCell && hasRoleActiveCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + ROLE_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + ROLE_SHEET + " is valid");

				// 3. Role_Permission sheet
				final Row rolePermissionSheetHeaderRow = rolePermissionSheet.getRow(0);
				if (rolePermissionSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + ROLE_PERMISSION_SHEET);
				else {
					boolean hasRoleNameCell = (rolePermissionSheetHeaderRow.getCell(0) != null
							&& !rolePermissionSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& rolePermissionSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Role"));
					boolean hasPermissionNameCell = (rolePermissionSheetHeaderRow.getCell(1) != null
							&& !rolePermissionSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& rolePermissionSheetHeaderRow.getCell(1).getStringCellValue()
									.equalsIgnoreCase("Permission"));

					if (!(hasRoleNameCell && hasPermissionNameCell))
						throw new FileHandlerException(
								FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + ROLE_PERMISSION_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + ROLE_PERMISSION_SHEET + " is valid");

				// 4. Group sheet
				final Row groupSheetHeaderRow = groupSheet.getRow(0);
				if (groupSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + GROUP_SHEET);
				else {
					boolean hasGrpNameCell = (groupSheetHeaderRow.getCell(0) != null
							&& !groupSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Name"));
					boolean hasGrpDescCell = (groupSheetHeaderRow.getCell(1) != null
							&& !groupSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("Description"));
					boolean hasGrpLendLimActiveCell = (groupSheetHeaderRow.getCell(2) != null
							&& !groupSheetHeaderRow.getCell(2).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(2).getStringCellValue().equalsIgnoreCase("LendingLimit"));
					boolean hasGrpActiveCell = (groupSheetHeaderRow.getCell(3) != null
							&& !groupSheetHeaderRow.getCell(3).getStringCellValue().isEmpty()
							&& groupSheetHeaderRow.getCell(3).getStringCellValue().equalsIgnoreCase("IsActive"));

					if (!(hasGrpNameCell && hasGrpDescCell && hasGrpLendLimActiveCell && hasGrpActiveCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + GROUP_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + GROUP_SHEET + " is valid");

				// 5. Group_Role sheet
				final Row grpRoleSheetHeaderRow = groupRoleSheet.getRow(0);
				if (grpRoleSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + GROUP_ROLE_SHEET);
				else {
					boolean hasGrpNameCell = (grpRoleSheetHeaderRow.getCell(0) != null
							&& !grpRoleSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& grpRoleSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Group"));
					boolean hasRoleNameCell = (grpRoleSheetHeaderRow.getCell(1) != null
							&& !grpRoleSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& grpRoleSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("Role"));

					if (!(hasGrpNameCell && hasRoleNameCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + GROUP_ROLE_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + GROUP_ROLE_SHEET + " is valid");

				// 6. Group_User sheet
				final Row grpUserSheetHeaderRow = groupUserSheet.getRow(0);
				if (grpUserSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + GROUP_USER_SHEET);
				else {
					boolean hasGrpNameCell = (grpUserSheetHeaderRow.getCell(0) != null
							&& !grpUserSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& grpUserSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("Group"));
					boolean hasUserEmailCell = (grpUserSheetHeaderRow.getCell(1) != null
							&& !grpUserSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& grpUserSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("UserEmail"));

					if (!(hasGrpNameCell && hasUserEmailCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + GROUP_USER_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + GROUP_USER_SHEET + " is valid");

				// 7. User sheet
				final Row userSheetHeaderRow = userSheet.getRow(0);
				if (userSheetHeaderRow == null)
					throw new FileHandlerException("Header row is missing from sheet: " + USER_SHEET);
				else {
					boolean hasEmailCell = (userSheetHeaderRow.getCell(0) != null
							&& !userSheetHeaderRow.getCell(0).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(0).getStringCellValue().equalsIgnoreCase("EmailId"));
					boolean hasUserIdCell = (userSheetHeaderRow.getCell(1) != null
							&& !userSheetHeaderRow.getCell(1).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(1).getStringCellValue().equalsIgnoreCase("UserId"));
					boolean hasFNameCell = (userSheetHeaderRow.getCell(2) != null
							&& !userSheetHeaderRow.getCell(2).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(2).getStringCellValue().equalsIgnoreCase("FirstName"));
					boolean hasLNameCell = (userSheetHeaderRow.getCell(3) != null
							&& !userSheetHeaderRow.getCell(3).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(3).getStringCellValue().equalsIgnoreCase("LastName"));
					boolean hasPNumberCell = (userSheetHeaderRow.getCell(4) != null
							&& !userSheetHeaderRow.getCell(4).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(4).getStringCellValue().equalsIgnoreCase("PhoneNumber"));
					boolean hasTZoneCell = (userSheetHeaderRow.getCell(5) != null
							&& !userSheetHeaderRow.getCell(5).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(5).getStringCellValue().equalsIgnoreCase("TimeZone"));
					boolean hasLendLimitNameCell = (userSheetHeaderRow.getCell(6) != null
							&& !userSheetHeaderRow.getCell(6).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(6).getStringCellValue().equalsIgnoreCase("LendingLimit"));
					boolean hasStatusCell = (userSheetHeaderRow.getCell(7) != null
							&& !userSheetHeaderRow.getCell(7).getStringCellValue().isEmpty()
							&& userSheetHeaderRow.getCell(7).getStringCellValue().equalsIgnoreCase("IsActive"));

					if (!(hasEmailCell && hasUserIdCell && hasFNameCell && hasLNameCell && hasPNumberCell
							&& hasTZoneCell && hasLendLimitNameCell && hasStatusCell))
						throw new FileHandlerException(FILE_TAB_COLUMNS_EXCEPTION_MSG + ". Sheet: " + USER_SHEET);
				}
				if(logger.isInfoEnabled())
					logger.info(">> Sheet: " + USER_SHEET + " is valid");
			}
		}
	}

	/**
	 * Adds a specific background color to the cell specified
	 * 
	 * @param cell
	 * @throws Exception
	 */
	private void addCellBackground(Cell cell) throws Exception {
		if (cell != null) {
			CellStyle cellStyle = null;
			if (cell.getSheet().getWorkbook().getNumCellStyles() == 1)
				cellStyle = cell.getSheet().getWorkbook().createCellStyle();
			else
				cellStyle = cell.getSheet().getWorkbook().getCellStyleAt(1);

			cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell.setCellStyle(cellStyle);
		}
	}

	/**
	 * Converts the String value to an NUMERIC cell value. Can be used for sheet cells in case of
	 * export where the stored value in system needs to be exported as Numeric cells
	 * @param value
	 * @return
	 */
	private Object setNumericCellValue(String value) {
		try {
			if(value == null || value.isEmpty())
				return null;
			else {
				final Double doubleValue = Double.valueOf(value);
				final String doubleStr = doubleValue.toString();
				if(doubleStr.contains(".") && !Pattern.compile("\\d+\\.[0]+").matcher(doubleStr).matches())
					return doubleValue;
				else {
					long longValueFromDouble = doubleValue.longValue();
					return Long.valueOf(longValueFromDouble);
				}
			}
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns the correct representation of a NUMERIC cell value
	 * @param doubleValue
	 * @return
	 */
	private String fetchNumericCellValue(double doubleValue) {
		String doubleStr = String.valueOf(doubleValue);
		if(doubleStr.contains(".") && !Pattern.compile("\\d+\\.[0]+").matcher(doubleStr).matches())
			return String.valueOf(doubleValue);
		else {
			long longValueFromDouble = (long) doubleValue;
			return String.valueOf(longValueFromDouble);
		}
	}
	
}
