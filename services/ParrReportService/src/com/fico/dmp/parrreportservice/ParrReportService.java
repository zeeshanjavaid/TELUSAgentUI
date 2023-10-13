/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.parrreportservice;

import javax.servlet.http.HttpServletRequest;
import com.fico.dmp.telusagentuidb.models.query.GetTeamNameByEmplIdResponse;
import com.fico.dmp.telusagentuidb.service.TELUSAgentUIDBQueryExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;
import java.lang.String;
import java.util.List;
import io.swagger.client.model.CollectionEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import com.fico.dmp.collectionentityservice.CollectionEntityService;
import io.swagger.client.model.CollectionPaymentArrangement;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.fico.telus.model.ParrReports;
import com.fico.qb.query.builder.support.utils.spring.CollectionUtils;
import java.util.stream.Collectors;
import com.fico.dmp.commonutilityservice.CommonUtilityService;
import com.fico.telus.model.ParrResWithHeader;








//import com.fico.dmp.parrreportservice.model.*;

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
public class ParrReportService {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(ParrReportService.class.getName());

    @Autowired
    private SecurityService securityService;
    
     @Autowired
    private CollectionEntityService collectionEntityService;
    
     @Autowired
    private CommonUtilityService commonUtilityService;
    
     @Autowired
   private TELUSAgentUIDBQueryExecutorService telusAgentUIDBQueryExecutorService;

    /**
     * This is sample java operation that accepts an input from the caller and responds with "Hello".
     *
     * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
     * getUserName() and getUserId() etc which returns the information based on the caller context.
     *
     * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
     * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
     */   
   
 public List<ParrReports> getParrReport(String fields, Integer offset, Integer limit, String agentId, String entityId, String entityRisk, String status, String createdFrom, String createdTo, String evaluation, String createdBy, String createdTeam)throws Exception {

        List<String> entityIds=new ArrayList<>();

        List<ParrReports> parrReportsList=new ArrayList<>();

             ParrResWithHeader parrReportList=collectionEntityService.getPaymentArrangementsForParrReport(fields, offset, limit, agentId, entityId, entityRisk, evaluation, status, createdBy, createdFrom, createdTo);


        if(!CollectionUtils.isEmpty(parrReportList.getResponseObjectList()))
        {
          //  entityIds= parrReportList.stream().map(a->a.getCollectionEntity().getId().toString()).collect(Collectors.toList());

            for(CollectionPaymentArrangement cpa:parrReportList.getResponseObjectList())
            {
                
                CollectionEntity collectionEntity  = collectionEntityService.getCollectionEntityById(Integer.valueOf(cpa.getCollectionEntity().getId()),null);

                ParrReports parrReports=new ParrReports();
                parrReports.setTotalNumberOfElement(parrReportList.getTotalNumberOfElement());
                parrReports.setParrId(cpa.getId());
                parrReports.setEntityId(cpa.getCollectionEntity().getId());
                 entityIds.add(cpa.getCollectionEntity().getId());
                parrReports.setParrStatus(cpa.getStatus());
               // parrReports.setParrStatus(cpa.getStatus());
                parrReports.setCreatedTeam(commonUtilityService.getTeamIdUsingEmpId(cpa.getAuditInfo().getCreatedBy()));

                parrReports.setEvaluation(cpa.getEvaluationResult());
                parrReports.setParrAmt("$"+cpa.getAmount());
                parrReports.setStart(cpa.getInstallments().get(0).getDate().toString());
                if(cpa.getInstallments().size()==0) {
                    parrReports.setExpiry(cpa.getInstallments().get(0).getDate().toString());
                }else{
                    parrReports.setExpiry(cpa.getInstallments().get(cpa.getInstallments().size()-1).getDate().toString());
                }
              if(cpa.getReceivedPaymentAmountToDate()==null ||cpa.getExpectedPaymentAmountToDate()==null )
                {
                    parrReports.setPerOfAmtRecieved_Exp("0");

                }else {
                	if(cpa.getExpectedPaymentAmountToDate() != 0) {
                    parrReports.setPerOfAmtRecieved_Exp(cpa.getReceivedPaymentAmountToDate() / cpa.getExpectedPaymentAmountToDate() * 100 + "%");
                	}else {
                		parrReports.setPerOfAmtRecieved_Exp(0+"%");
                	}
                }  
                
                 if(collectionEntity!=null)
              {
                  parrReports.setEntityName(collectionEntity.getName());
                  parrReports.setEntityRisk(collectionEntity.getCustomerRisk());
              }
              
            parrReports.setCreatedBy(commonUtilityService.getNameUsingEmpId(cpa.getAuditInfo().getCreatedBy()));
            
              if(createdTeam==null || createdTeam=="" ){
                  parrReportsList.add(parrReports);
              }else{

                  if(parrReports.getCreatedTeam()!=null && parrReports.getCreatedTeam().equalsIgnoreCase(createdTeam))
                  {
                      parrReportsList.add(parrReports);
                  }
              }

            //  parrReportsList.add(parrReports);
            }

        }

     return parrReportsList;
    }
    
    



}