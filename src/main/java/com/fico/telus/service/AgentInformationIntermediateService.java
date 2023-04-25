package com.fico.telus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fico.dmp.telusagentuidb.models.query.GetActiveAgentListWithWorkCategoryResponse;
import com.fico.dmp.telusagentuidb.service.TELUSAgentUIDBQueryExecutorService;
import com.fico.telus.model.AgentInfo;
import com.fico.telus.model.ErrorInfo;

@Service
public class AgentInformationIntermediateService {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentInformationIntermediateService.class);
	
	@Autowired
	TELUSAgentUIDBQueryExecutorService telusAgentUIDBQueryExecutorService;
	
	
	public ResponseEntity<Object> getActiveAgentInformation() {
		logger.info("inside AgentInformationIntermediateService#getActiveAgentInformation");
		List<GetActiveAgentListWithWorkCategoryResponse> getActiveAgentsListResponseList = new ArrayList<GetActiveAgentListWithWorkCategoryResponse>();
		Pageable pageable = PageRequest.of(0, 1000);
		List<AgentInfo> agentInfoList = null;
		try {
		Page<GetActiveAgentListWithWorkCategoryResponse> activeAgentListPageableResponse = telusAgentUIDBQueryExecutorService.executeGetActiveAgentListWithWorkCategory(pageable);
		while(!activeAgentListPageableResponse.isEmpty()) {
			logger.info("Not empty");
			   pageable = pageable.next();
			   getActiveAgentsListResponseList = activeAgentListPageableResponse.getContent();
			   activeAgentListPageableResponse = telusAgentUIDBQueryExecutorService.executeGetActiveAgentListWithWorkCategory(pageable);
		}
		    agentInfoList = new ArrayList<AgentInfo>();
		for (GetActiveAgentListWithWorkCategoryResponse getActiveAgentListResponse : getActiveAgentsListResponseList) {
			String workCategoryCommaSeparatedStr = getActiveAgentListResponse.getWorkCategory();
			List<String> workCategories = new ArrayList<String>();
			if(workCategoryCommaSeparatedStr != null) {
				workCategories = Stream.of(workCategoryCommaSeparatedStr.split(",", -1)).collect(Collectors.toList());
			}
			AgentInfo agentInfo = new AgentInfo();
			agentInfo.setEmpId(getActiveAgentListResponse.getEmpId());
			agentInfo.setWorkCategory(workCategories);
			agentInfoList.add(agentInfo);
		}
		}
		catch(Exception exception) {
			ErrorInfo errorInfo = new ErrorInfo();
			errorInfo.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			errorInfo.setMessage("Error Occured");
			return new ResponseEntity<Object>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(agentInfoList,HttpStatus.OK);
		}
	
	
	
	}