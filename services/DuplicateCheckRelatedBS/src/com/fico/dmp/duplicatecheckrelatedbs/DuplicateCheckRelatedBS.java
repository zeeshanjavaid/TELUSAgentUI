/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.duplicatecheckrelatedbs;

import com.wavemaker.runtime.service.annotations.ExposeToClient;

//import com.fico.dmp.duplicatecheckrelatedbs.model.*;

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
public class DuplicateCheckRelatedBS {

/*    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DuplicateCheckRelatedBS.class.getName());

    @Autowired
    private SecurityService securityService;
    
    @Autowired
   	private DuplicateResolutionServiceBS duplicateResolutionServiceBS;


    
    
    
    
    public DuplicateResolutionReviewResponseWrapper getDuplicateCheckReviewData(Integer applicationId)
    {
    	
    	DuplicateResolutionReviewResponseWrapper result= null;
    	try {
    		result = duplicateResolutionServiceBS.getApplicationMatchDataset(applicationId);
		} catch (Exception e) {
		    if(logger.isErrorEnabled())
			    logger.error("Unexpected error occurred while fetching duplicate check review data", e);
		}
    	
    	return result;
    	
    	
    }
    
    
    
*/
    

}