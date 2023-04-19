package com.fico.telus.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wavemaker.runtime.security.SecurityService;

import io.swagger.client.model.AuditInfo;

@Service
public class CollectionCommonService {
	
	@Autowired
	private SecurityService securityService;
	
	public void setAuditinfo(AuditInfo auditInfo, boolean isCreate) {
		
		if (isCreate) {
			auditInfo.setCreatedDateTime(LocalDateTime.now());
			auditInfo.setCreatedBy(securityService.getUserId());
		} else {
			auditInfo.setLastUpdatedBy(securityService.getUserId());
			auditInfo.setLastUpdatedDateTime(LocalDateTime.now());
		}		
	}
}
