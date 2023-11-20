/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.commonutilityservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Date;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import io.swagger.client.model.CollectionBillingAccountRef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fico.dmp.collectionentityservice.CollectionEntityService;
import com.fico.dmp.telusagentuidb.Team;
import com.fico.dmp.telusagentuidb.TeamUser;
import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.models.query.GetTeamNameByEmplIdResponse;
import com.fico.dmp.telusagentuidb.models.query.GetUserListByTeamIdResponse;
import com.fico.dmp.telusagentuidb.service.TELUSAgentUIDBQueryExecutorService;
import com.fico.dmp.telusagentuidb.service.TeamService;
import com.fico.dmp.telusagentuidb.service.TeamUserService;
import com.fico.dmp.telusagentuidb.service.UserService;
import com.fico.telus.model.AssignedTeamModel;
import com.fico.telus.model.AssignedUserModel;
import com.fico.telus.model.BillingAccountModel;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import com.fico.dmp.telusagentuidb.TeamManager;
import com.fico.dmp.telusagentuidb.service.*;
import java.sql.Timestamp;
import java.util.ArrayList;

//import com.fico.dmp.commonutilityservice.model.*;

/**
 * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
 * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
 * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
 * Complex Types/Objects will become part of the Request body in the generated API.
 */
@ExposeToClient
public class CommonUtilityService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(CommonUtilityService.class.getName());

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private TeamUserService teamUserService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TeamService	teamService;
    
    @Autowired
    private CollectionEntityService collectionEntityService;
    
    
	@Autowired
	TELUSAgentUIDBQueryExecutorService telusAgentUIDBQueryExecutorService;
	
	 @Autowired
    private TeamManagerService teamManagerService;

    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     */
    public String getLoggedInUserTeamId(Integer userId) {
        logger.info("Inside CommonUtilityService#getLoggerInUserTeamId " + userId);
        
        String query = "userId="+ userId + " order by updatedOn desc";
       // Pageable pageable = 
        Pageable pageable = PageRequest.of(0, 1);
        Page<TeamUser> teamUserPageList = teamUserService.findAll(query, pageable);
        TeamUser teamUser = null;
        String teamId = null;
        if(!teamUserPageList.isEmpty()) {
        	teamUser = teamUserPageList.getContent().get(0);
        	teamId = teamUser.getTeam().getTeamId();
        }
       
        logger.info("teamId-----"+teamId);
        return teamId;
    }
    
    public List<AssignedUserModel> getAssignedPersonInActionManagement(){
    	Pageable pageable = PageRequest.of(0, 10000);
        Page<User> userPageList = userService.findAll("active = true ORDER BY firstName asc", pageable);
        List<AssignedUserModel> assignedUserModelList = new ArrayList<AssignedUserModel>();
        AssignedUserModel assignedUser = new AssignedUserModel();
        assignedUser.setFirstName("NULL");
        assignedUser.setLastName("NULL");
        assignedUser.setEmpId("NULL");
        assignedUserModelList.add(assignedUser);
        if(userPageList.hasContent()) {
        userPageList.stream().forEach( user -> {
        	AssignedUserModel assignedUserModel = new AssignedUserModel();
        	assignedUserModel.setFirstName(StringEscapeUtils.unescapeHtml4(user.getFirstName()));
        	assignedUserModel.setLastName(StringEscapeUtils.unescapeHtml4(user.getLastName()));
        	assignedUserModel.setEmpId(user.getEmplId());
        	assignedUserModelList.add(assignedUserModel);
        });
        }
    	return assignedUserModelList;
    	   // 	return assignedUserModelList.stream().sorted((o1, o2) -> (o1.getFirstName().compareTo(o2.getFirstName()))).collect(Collectors.toList());

    }
    
    
    public List<AssignedTeamModel> getTeamListInActionManagement(){
    	Pageable pageable = PageRequest.of(0, 10000);
    	String query = null;
        Page<Team> teamPageList = teamService.findAll(query, pageable);
        List<AssignedTeamModel> assignedTeamModelList = new ArrayList<AssignedTeamModel>();
        AssignedTeamModel assignedTeam = new AssignedTeamModel();
        assignedTeam.setTeamId("NULL");
        assignedTeam.setTeamName("NULL");
        assignedTeamModelList.add(assignedTeam);
        if(teamPageList.hasContent()) {
        	teamPageList.stream().forEach( team -> {
        		AssignedTeamModel assignedTeamModel = new AssignedTeamModel();
        		assignedTeamModel.setTeamId(team.getTeamId());
        		assignedTeamModel.setTeamName(team.getTeamName());
        		assignedTeamModelList.add(assignedTeamModel);
        });
        }
    	return assignedTeamModelList;
    }
    
    
    public List<AssignedUserModel> getUserListByTeamId(String teamId){
    	Pageable pageable = PageRequest.of(0, 1000);
    	Page<GetUserListByTeamIdResponse> pageableUserListByTeamIdResponse = telusAgentUIDBQueryExecutorService.executeGetUserListByTeamId(teamId, pageable);
        List<AssignedUserModel> assignedUserModelList = new ArrayList<AssignedUserModel>();
        if(pageableUserListByTeamIdResponse.hasContent()) {
        	pageableUserListByTeamIdResponse.stream().forEach( user -> {
        	AssignedUserModel assignedUserModel = new AssignedUserModel();
        	assignedUserModel.setFirstName(user.getFirstName());
        	assignedUserModel.setLastName(user.getLastName());
        	assignedUserModel.setEmpId(user.getEmplId());
        	assignedUserModelList.add(assignedUserModel);
        });
        }
    	return assignedUserModelList;
    }
    
    
    
    public List<BillingAccountModel> getBillingAccountUsingBillingAccountReferenceIds(String billingAccountRefIds) {
    	
    	//String billingAcctRefIdsInListAsString = billingAccountRefIds.stream().map(String::valueOf).collect(Collectors.joining(","));
     	String fields = "id,billingAccount.id,billingAccount.name";
    	//String idInQuery = "in:"+billingAccountRefIds;
    	List<BillingAccountModel> billingAccountModelList = new ArrayList<BillingAccountModel>();
    	try {
    		//collectionEntityService.getBillingAccountRef(fields, offset, limit, ban, entityId, id)
			List<CollectionBillingAccountRef> collectionBillingAccountRefList =  collectionEntityService.getBillingAccountRef(fields, null, null, null, null, billingAccountRefIds);
			for (CollectionBillingAccountRef collectionBillingAccountRef : collectionBillingAccountRefList) {
				BillingAccountModel billingAccountModel = new BillingAccountModel();
				billingAccountModel.setBillingAccountId(collectionBillingAccountRef.getBillingAccount().getId());
				billingAccountModel.setBillingAccountName(collectionBillingAccountRef.getBillingAccount().getName());
				billingAccountModelList.add(billingAccountModel);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return billingAccountModelList;
    }
    
        
    public String getNameUsingEmpId(String empId) {
    	String query = "emplId='" + empId + "' and active = true";
    	Pageable pageable = PageRequest.of(0, 1000);
    	Page<User> userPageList = userService.findAll(query, pageable);
    	String firstAndLastNameCombined = null;
    	String firstName = null;
    	String lastName = null;
    	if(userPageList.hasContent()) {
          for (User user : userPageList) {
        	  firstName = user.getFirstName();
        	  lastName = user.getLastName();
		}
         }
    	
    	if(lastName != null) {
    		firstAndLastNameCombined = firstName + " "+ lastName;
    	}else {
    		firstAndLastNameCombined = firstName;
    	}
    	return firstAndLastNameCombined;
    }
    
   
    public String getTeamIdUsingEmpId(String empId) {
    	Pageable pageable = PageRequest.of(0, 1);
    	Page<GetTeamNameByEmplIdResponse> pageableGetTeamNameByEmplIdResponse = telusAgentUIDBQueryExecutorService.executeGetTeamNameByEmplId(empId, pageable);
        String teamId = null;
        if(pageableGetTeamNameByEmplIdResponse.hasContent()) {
        	teamId = pageableGetTeamNameByEmplIdResponse.getContent().get(0).getTeamId();
        }
    	return teamId;
    }
    
    
     public String deleteTeamManagerOnUpdate(String teamId)
    {
        telusAgentUIDBQueryExecutorService.executeDeleteTeamManager(teamId);

        return "Team manager deleted successfully";
    }
    
        public String saveManagerOnTeamCreate(Integer teamId, String managerIdListString, Integer createdBy, Integer updatedBy, Date createdOn, Date updatedOn)
    {
        
         logger.info("managerListString", managerIdListString);
                List<Integer> managerIdList =Arrays.asList(managerIdListString.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
                
                logger.info("managerListint", managerIdList);

        if(!managerIdList.isEmpty())
        {
            for(Integer managerId:managerIdList)
            {

                TeamManager teamManager=new TeamManager();
                teamManager.setTeam(teamService.getById(teamId));
                teamManager.setManagerUserId(managerId);
                teamManager.setCreatedBy(createdBy);
                teamManager.setUpdatedBy(updatedBy);
                teamManager.setCreatedOn(convertDateToTimesStamp(createdOn));
                teamManager.setUpdatedOn(convertDateToTimesStamp(updatedOn));
                teamManagerService.create(teamManager);
            }
        }

        return "Created team Manager successfully";
    }
    
    
    private Timestamp convertDateToTimesStamp(Date date) {


    // getting the object of the Timestamp class
    return new Timestamp(date.getTime());
}
    
    public List<AssignedUserModel> getActiveUserList(){
    	Pageable pageable = PageRequest.of(0, 10000);
        Page<User> userPageList = userService.findAll("active = true ORDER BY firstName asc", pageable);
        List<AssignedUserModel> assignedUserModelList = new ArrayList<AssignedUserModel>();
        if(userPageList.hasContent()) {
        userPageList.stream().forEach( user -> {
        	AssignedUserModel assignedUserModel = new AssignedUserModel();
        	assignedUserModel.setFirstName(StringEscapeUtils.unescapeHtml4(user.getFirstName()));
        	assignedUserModel.setLastName(StringEscapeUtils.unescapeHtml4(user.getLastName()));
        	assignedUserModel.setEmpId(user.getEmplId());
        	assignedUserModelList.add(assignedUserModel);
        });
        }
    	return assignedUserModelList;
    	   // 	return assignedUserModelList.stream().sorted((o1, o2) -> (o1.getFirstName().compareTo(o2.getFirstName()))).collect(Collectors.toList());

    }
    
    public String decodeAccentedCharacters(String name){
    	String decodedStr = StringEscapeUtils.unescapeHtml4(name);
    	return decodedStr;
    }
    

}