package com.fico.telus.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.service.UserService;
import com.wavemaker.runtime.security.SecurityService;

import io.swagger.client.model.AuditInfo;
//import java.time.OffsetDateTime;
import org.threeten.bp.OffsetDateTime;



@Service
public class CollectionCommonService {
	
	private static final Logger logger = LoggerFactory.getLogger(CollectionCommonService.class);
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserService userService;
	
	public AuditInfo UpdateAuditInfo(AuditInfo auditInfo, boolean isCreate) {
		
		User user = getUser();
		String empId = "";
		
		if(user == null) {
			
			empId = "System";
			
		} else {
			empId = user.getEmplId();
		}		
		if (isCreate) {
			auditInfo.setCreatedTimestamp(LocalDateTime.now());
			auditInfo.setCreatedBy(empId);
		} else {
			auditInfo.setLastUpdatedBy(empId);
			auditInfo.setLastUpdatedTimestamp(LocalDateTime.now());
		}
		return auditInfo;
	}
	
	public User getUser() {

		Pageable pageable = PageRequest.of(0, 1);

		Page<User> pageResponse = userService
				.findAll("userId = '" + securityService.getUserId() + "' and active = true", pageable);
		if (pageResponse.hasContent()) {
			
			logger.warn("******************User exists with userID : " + securityService.getUserId());
			return pageResponse.getContent().get(0);
		}
		return null;
	}
}
