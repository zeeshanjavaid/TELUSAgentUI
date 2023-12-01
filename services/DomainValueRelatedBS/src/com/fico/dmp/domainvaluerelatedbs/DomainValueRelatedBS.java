/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.domainvaluerelatedbs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fico.core.services.DomainValueServiceBS;
import com.fico.core.util.CommonUtils;
import com.fico.dmp.telusagentuidb.DomainValue;
import com.fico.dmp.telusagentuidb.DomainValueType;
import com.fico.dmp.telusagentuidb.models.query.DvsearchByCodeAndDescriptionResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetAllDomainValueWithOneParentResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetAllDomainValuesByDvTypeCodeResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetAllDomainValuesTwoParentResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetAllDvsByDvtypeWithActiveFlagResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetDomainValueByCodeAndTypeCodeResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetDomainValueByIdResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetDomainValueByIdWithActiveFlagResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetDvbyCodeAndTypeCodeWithActiveFlagResponse;
import com.fico.ps.model.DomainValueVO;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import org.apache.commons.text.StringEscapeUtils;


//import com.fico.dmp.domainvaluerelatedbs.model.*;

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
public class DomainValueRelatedBS {

    private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DomainValueRelatedBS.class.getName());

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    @Qualifier("facade.DomainValueServiceBS")
    private DomainValueServiceBS domainValueServiceBS;
    
    @Autowired
    @Qualifier("core.CommonUtils")
    private CommonUtils commonUtils;
    
    @Autowired
    private com.wavemaker.runtime.WMAppObjectMapper wmAppObjectMapper;


    
    /**
     * Create/Update {@link DomainValue} instance from {@link DomainValueVO} instance supplied
     * @param domainValueVO
     * @param defaultLocale
     * @param request
     * @return
     */
    public ResponseEntity<Object> domainValueCreateUpdate(DomainValueVO domainValueVO, String defaultLocale, HttpServletRequest request) {    	
    	DomainValueVO return_domainValueVO = null;
    	try {
    	    String domainValeJSON = wmAppObjectMapper.writeValueAsString(domainValueVO);
        	domainValeJSON = StringEscapeUtils.unescapeHtml4(domainValeJSON);
        	domainValueVO = wmAppObjectMapper.readValue(domainValeJSON, DomainValueVO.class);
        	
    		return_domainValueVO = domainValueServiceBS.createOrUpdateDomainValue(domainValueVO, defaultLocale);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while saving DomainValue.",e);
            return new ResponseEntity<Object>(new com.fico.ps.model.Error("WS-GROUP-0000","Unknown error occurred."), HttpStatus.BAD_REQUEST);
		}
   
    	return new ResponseEntity<Object>(return_domainValueVO, HttpStatus.OK);
    }
    
    /**
     * Fetches a {@link DomainValue} instance by supplied domain value Id
     * @param domainValueId
     * @param defaultLocale
     * @param request
     * @return
     */
    public ResponseEntity<Object> findDomainValueById(Integer domainValueId, String defaultLocale, HttpServletRequest request) {
    	DomainValueVO return_domainValueVO = null;
    	
    	try {
			return_domainValueVO = domainValueServiceBS.getDomainValueById(domainValueId, defaultLocale);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue.",e);
            return new ResponseEntity<Object>(new com.fico.ps.model.Error("WS-GROUP-0000","Unknown error occurred."), HttpStatus.BAD_REQUEST);
		}
    	
    	return new ResponseEntity<Object>(return_domainValueVO, HttpStatus.OK);
    }
    
    /**
     * Activate/Deactivate {@link DomainValue} set based on supplied DV IDs
     * @param isToActivate
     * @param ids
     * @return
     */
    public ResponseEntity<Object> activateDeactivateDVByIds(boolean isToActivate, Integer... ids) {
    	try {
    		domainValueServiceBS.activateOrDeactivateDomainValuesById(isToActivate, ids);
    	}
    	catch (Exception e) {
    		if(logger.isErrorEnabled())
    			logger.error("Unexpected error occurred while activating/deactivating DomainValue.",e);
            return new ResponseEntity<Object>(new com.fico.ps.model.Error("WS-GROUP-0000","Unknown error occurred."), HttpStatus.BAD_REQUEST);
		}
    	
    	return new ResponseEntity<Object>(HttpStatus.OK);
    }
    
    /**
     * Returns a paginated view of {@link DomainValue} as a {@link DvsearchByCodeAndDescriptionResponse} list
     * based on supplied filters
     * @param domainValueTypeId
     * @param showAll
     * @param isActiveFlag
     * @param searchValue
     * @param defaultLocale
     * @param currentPage
     * @param pageSize
     * @param sortOrders
     * @return
     */
    public CustomPageData_DVSearchFilters getDVListByFilters(Integer domainValueTypeId, Boolean showAll, 
			Boolean isActiveFlag, String searchValue, String defaultLocale,
			Integer currentPage, Integer pageSize, String sortOrders) {
    	CustomPageData_DVSearchFilters customPageDataWrapper = new CustomPageData_DVSearchFilters();
    	try {
    		Page<DvsearchByCodeAndDescriptionResponse> domainValueList = 
    				domainValueServiceBS.getDVListByFilters(domainValueTypeId, showAll, isActiveFlag, searchValue, defaultLocale, currentPage, pageSize, sortOrders);
    		
    		customPageDataWrapper.setPageNumber(domainValueList.getNumber());
    		customPageDataWrapper.setPageSize(domainValueList.getSize());
    		customPageDataWrapper.setTotalRecords(domainValueList.getTotalElements());
    		
//    		//converting specific page type to Object list
//    		List<Object> pageContentList = new ArrayList<Object>();
//    		pageContentList.addAll(domainValueList.getContent());
    		
    		List<DvsearchByCodeAndDescriptionResponse> modifiedList = domainValueList.toList();
    		modifiedList = commonUtils.setNullToEmptyForStringProperties(DvsearchByCodeAndDescriptionResponse.class, modifiedList);
    		modifiedList.stream().forEach(dmCreatedBy->dmCreatedBy.setCreatedBy(StringEscapeUtils.unescapeHtml4(dmCreatedBy.getCreatedBy())));

    		customPageDataWrapper.setPageContent(modifiedList); 
    	}
    	catch (Exception e) {
    		if(logger.isErrorEnabled())
    			logger.error("Unexpected error occurred while fetching DomainValue(s) with supplied filters.",e);
		}
    	
    	return customPageDataWrapper;
    }
    
    /**
     * Returns the list of DomainValueType as {@link DomainValueType} list based on the domain value type description
     * @param description
     * @param currentPage
     * @param pageSize
     * @param sortOrders
     * @return
     */
    public CustomPageData_DVTypeDescSearchFilters getDomainValueTypeByDescription(String description, Integer currentPage,
			Integer pageSize, String sortOrders) {
    	CustomPageData_DVTypeDescSearchFilters customPageDataWrapper = new CustomPageData_DVTypeDescSearchFilters();
    	try {
			Page<DomainValueType> domainValueTypeList = 
					domainValueServiceBS.getDomainValueTypeByDescription(description, currentPage, pageSize, sortOrders);
			
			customPageDataWrapper.setPageNumber(domainValueTypeList.getNumber());
			customPageDataWrapper.setPageSize(domainValueTypeList.getSize());
			customPageDataWrapper.setTotalRecords(domainValueTypeList.getTotalElements());
			
//			//converting specific page type to Object list
//    		List<Object> pageContentList = new ArrayList<Object>();
//    		pageContentList.addAll(domainValueTypeList.getContent());
    		
    		List<DomainValueType> modifiedList = domainValueTypeList.toList();
    		modifiedList = commonUtils.setNullToEmptyForStringProperties(DomainValueType.class, modifiedList);
    		customPageDataWrapper.setPageContent(modifiedList);
			
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValueType(s) with supplied description.",e);
		}
    	
    	return customPageDataWrapper;
    }
    
    /**
     * Get specific domain value with the given domain value Id and its active status
     * @param domainValueId
     * @param userLocale
     * @param isActiveFlag
     * @param showAll
     * @return
     */
    public QueryGetDomainValueByIdWithActiveFlagResponse getDomainValueByDomainValueIdWithActiveFlag(Integer domainValueId, String userLocale,
    		Boolean isActiveFlag, Boolean showAll) {
    	QueryGetDomainValueByIdWithActiveFlagResponse queryResponse = null;
    	try {
			queryResponse = domainValueServiceBS.getDomainValueByDomainValueIdWithActiveFlag(domainValueId, userLocale, isActiveFlag, showAll);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue with given DV Id",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Get specific domain value with the given domain value Id
     * @param domainValueId
     * @param userLocale
     * @return
     */
    public QueryGetDomainValueByIdResponse getDomainValueByDomainValueId(Integer domainValueId, String userLocale) {
    	QueryGetDomainValueByIdResponse queryResponse = null;
    	try {
			queryResponse = domainValueServiceBS.getDomainValueByDomainValueId(domainValueId, userLocale);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue with given DV Id",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Get specific domain value with given DV CODE and DV type CODE
     * @param domainValueCode
     * @param domainValueTypeCode
     * @param userLocale
     * @return
     */
    public QueryGetDomainValueByCodeAndTypeCodeResponse getDomainValueByCodeAndTypeCode(String domainValueCode,
    		String domainValueTypeCode, String userLocale) {
    	QueryGetDomainValueByCodeAndTypeCodeResponse queryResponse = null;
    	try {
    		queryResponse = domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCode(domainValueCode, domainValueTypeCode, userLocale);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue with CODE and type CODE",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Get all domain values with the given specific DV type CODE
     * @param domainValueTypeCode
     * @param userLocale
     * @param isAlphaSort
     * @return
     */
    public List<QueryGetAllDomainValuesByDvTypeCodeResponse> getAllDomainValuesByTypeCode(String domainValueTypeCode, String userLocale, boolean isAlphaSort) {
    	List<QueryGetAllDomainValuesByDvTypeCodeResponse> queryResponse = new ArrayList<>();
    	try {
			queryResponse.addAll(domainValueServiceBS.getAllDomainValuesByDVTypeCode(domainValueTypeCode, userLocale, isAlphaSort));
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue(s) with supplied type CODE",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Get all domain values with the given specific DV type CODE and parent 1 DV Id
     * @param domainValueTypeCode
     * @param userLocale
     * @param parent1DomainValueId
     * @return
     */
    public List<QueryGetAllDomainValueWithOneParentResponse> getAllDomainValueWithSingleParent(String domainValueTypeCode, String userLocale,
    		Integer parent1DomainValueId, String dvDescription) {
    	List<QueryGetAllDomainValueWithOneParentResponse> queryResponse = new ArrayList<>();
    	try {
			queryResponse.addAll(domainValueServiceBS.getAllDomainValuesWithSingleParent(domainValueTypeCode, userLocale, parent1DomainValueId, dvDescription));
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue(s) with supplied type CODE and parent 1 DV Id",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Get all domain values with the given specific DV type CODE and parent 1 DV Id and parent 2 DV Id
     * @param domainValueTypeCode
     * @param userLocale
     * @param parent1DomainValueId
     * @param parent2DomainValueId
     * @return
     */
    public List<QueryGetAllDomainValuesTwoParentResponse> getAllDomainValueWithTwoParent(String domainValueTypeCode, String userLocale,
    		Integer parent1DomainValueId, Integer parent2DomainValueId, String dvDescription) {
    	List<QueryGetAllDomainValuesTwoParentResponse> queryResponse = new ArrayList<>();
    	try {
			queryResponse.addAll(domainValueServiceBS.getAllDomainValuesWithTwoParent(domainValueTypeCode, userLocale, parent1DomainValueId, parent2DomainValueId, dvDescription));
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue(s) with supplied type CODE, parent 1 DV Id and parent 2 DV Id",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Fetch DomainValue by the domain value CODE and the underlying DomainValue type CODE and locale supplied with
	 * an extra ACTIVE flag status
     * @param domainValueCode
     * @param domainValueTypeCode
     * @param userLocale
     * @param isActiveFlag
     * @param showAll
     * @return
     */
    public QueryGetDvbyCodeAndTypeCodeWithActiveFlagResponse getDomainValueByCodeAndTypeCodeWithActiveFlag(String domainValueCode,
			String domainValueTypeCode, String userLocale, Boolean isActiveFlag, Boolean showAll) {
    	QueryGetDvbyCodeAndTypeCodeWithActiveFlagResponse queryResponse = null;
    	try {
			queryResponse = domainValueServiceBS.getDomainValueByDVCodeAndDVTypeCodeWithActiveStatus(domainValueCode, domainValueTypeCode, userLocale, isActiveFlag, showAll);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue with CODE and type CODE",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Fetch all DomainValues by the given DomainValue type CODE and locale supplied with an extra ACTIVE flag status
     * @param domainValueTypeCode
     * @param userLocale
     * @param isActiveFlag
     * @param showAll
     * @return
     */
    public List<QueryGetAllDvsByDvtypeWithActiveFlagResponse> getAllDomainValuesByTypeCodeWithActiveFlag(String domainValueTypeCode,
			String userLocale, Boolean isActiveFlag, Boolean showAll) {
    	List<QueryGetAllDvsByDvtypeWithActiveFlagResponse> queryResponse = new ArrayList<QueryGetAllDvsByDvtypeWithActiveFlagResponse>();
    	try {
			queryResponse.addAll(domainValueServiceBS.getAllDomainValuesByDVTypeCodeWithActiveStatus(domainValueTypeCode, userLocale, isActiveFlag, showAll));
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("Unexpected error occurred while fetching DomainValue(s) with supplied type CODE",e);
		}
    	
    	return queryResponse;
    }
    
    /**
     * Useful for wrapping paginated contents such as to make useful information like
     * <b>pageNumber, pageSize and totalRecords</b> available on the service response 
     * to the respective consumer (which otherwise is not available to the FAWB service
     * variables when tied to these paginated services)
     * 
     * @apiNote Restricted to {@link DvsearchByCodeAndDescriptionResponse} data only
     * @param <T>
     */
    public class CustomPageData_DVSearchFilters {
    	Integer pageNumber;
    	Integer pageSize;
    	Long totalRecords;
    	List<DvsearchByCodeAndDescriptionResponse> pageContent;
    	
    	public Integer getPageNumber() {
    		return pageNumber;
    	}
    	public void setPageNumber(Integer pageNumber) {
    		this.pageNumber = pageNumber;
    	}
    	public Integer getPageSize() {
    		return pageSize;
    	}
    	public void setPageSize(Integer pageSize) {
    		this.pageSize = pageSize;
    	}
    	public Long getTotalRecords() {
    		return totalRecords;
    	}
    	public void setTotalRecords(Long totalRecords) {
    		this.totalRecords = totalRecords;
    	}
    	public List<DvsearchByCodeAndDescriptionResponse> getPageContent() {
    		return pageContent;
    	}
    	public void setPageContent(List<DvsearchByCodeAndDescriptionResponse> pageContent) {
    		this.pageContent = pageContent;
    	}
    }
    
    /**
     * Useful for wrapping paginated contents such as to make useful information like
     * <b>pageNumber, pageSize and totalRecords</b> available on the service response 
     * to the respective consumer (which otherwise is not available to the FAWB service
     * variables when tied to these paginated services)
     * 
     * @apiNote Restricted to {@link DomainValueType} data only
     * @param <T>
     */
    public class CustomPageData_DVTypeDescSearchFilters {
    	Integer pageNumber;
    	Integer pageSize;
    	Long totalRecords;
    	List<DomainValueType> pageContent;
    	
    	public Integer getPageNumber() {
    		return pageNumber;
    	}
    	public void setPageNumber(Integer pageNumber) {
    		this.pageNumber = pageNumber;
    	}
    	public Integer getPageSize() {
    		return pageSize;
    	}
    	public void setPageSize(Integer pageSize) {
    		this.pageSize = pageSize;
    	}
    	public Long getTotalRecords() {
    		return totalRecords;
    	}
    	public void setTotalRecords(Long totalRecords) {
    		this.totalRecords = totalRecords;
    	}
    	public List<DomainValueType> getPageContent() {
    		return pageContent;
    	}
    	public void setPageContent(List<DomainValueType> pageContent) {
    		this.pageContent = pageContent;
    	}
    }
   
}