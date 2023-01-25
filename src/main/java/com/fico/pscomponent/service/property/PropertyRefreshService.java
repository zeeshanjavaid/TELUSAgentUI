package com.fico.pscomponent.service.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fico.pscomponent.util.PropertiesUtil;

@Service
public class PropertyRefreshService {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertyRefreshService.class);

	@Autowired
	private FawbPropertySourceRefresher fawbPropertySourceRefresher;
	
	@Autowired
	private PropertiesUtil propertiesUtil;

	public void refreshProperties() {
	    
	    logger.info("test1: value in PropertyRefreshService:::::::::::::::::::::");
		fawbPropertySourceRefresher.refresh();
		
			String test1 = propertiesUtil.getPropertyValue("test1");
		
			logger.info("test1: value :::::::::::::::::::::"+ test1);
		/*
		 * dmpAuthenticationHandler.init(); solutionEditorProcessHandler.init();
		 * s3UploadService.init(); fraudCheckUtil.init();
		 * requestPersistenceHandler.init(); activityLogBOService.init();
		 * responsePersistenceHandler.init(); alertDecisionFlowHandler.init();
		 * extractTableToCsvHandler.init(); alertReviewHandler.init();
		 * fdpHandler.init();
		 */
	}
}
