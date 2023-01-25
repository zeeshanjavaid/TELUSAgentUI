/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.queueloadservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import com.fico.core.services.QueueServiceBS;
import com.fico.dmp.telusagentuidb.Queue;
import com.fico.ps.model.queue.QueueResponseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.fico.qb.query.builder.support.builder.SqlBuilder;
import com.fico.qb.query.builder.support.model.result.SqlQueryResult;
import com.fico.qb.query.SqlQueryBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.fico.qb.query.builder.support.model.JsonRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This is a singleton class with all its public methods exposed as REST APIs
 * via generated controller class. To avoid exposing an API for a particular
 * public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the
 * generated APIs. For example, a method name that starts with delete/remove,
 * will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be
 * exposed as Query Parameters & Complex Types/Objects will become part of the
 * Request body in the generated API.
 */
@ExposeToClient
public class QueueLoadService {
	
	@Autowired
	@Qualifier("facade.QueueServiceBS")
	private QueueServiceBS queueServiceBS;

	private static final Logger logger = FAWBStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(QueueLoadService.class.getName());

	/**
	 * Builds a dynamic SQL where clause from the Jquery's condition builder JSON configuration 
	 * @param conditionBuilderJSON
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<Object> prepareSQLWhereClauseFromJSONFilterString(String conditionBuilderJSON) {
		try {
			return new ResponseEntity<Object>(queueServiceBS.prepareSQLWhereClauseFromJSONFilterString(conditionBuilderJSON), HttpStatus.OK);
		} catch (Exception e) {
		    if(logger.isErrorEnabled())
			    logger.error("Unexpected error occurred while generating dynamic SQL where clause from the ConditionQuery builder JSON", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Method to create/update a queue into the system
	 * @param queue
	 * @param listGroups
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<Object> createUpdateDynamicQuery(Queue queue, Integer... listGroups) {
		try {
			return new ResponseEntity<Object>(queueServiceBS.createUpdateDynamicQuery(queue, listGroups), HttpStatus.OK);
		} catch (Exception e) {
		    if(logger.isErrorEnabled())
			    logger.error("Unexpected error creating/updating queue information", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Method that makes ready all the dynamic SQL query essential to get the data out of the DB as per the filter, sort and
	 * other relevant criteria for a given Queue
	 * @param queueId
	 * The Queue instance
	 * @param userLocale
	 * The user system's current locale (used for matching DV filters as selected in UI)
	 * @param searchFilter
	 * The UI specific search filters
	 * @param sortField
	 * The runtime sort field
	 * @param sortOrder
	 * The runtime sort order (ASC/DESC). Passing NULL will consider order as ASC
	 * @param pageNumber
	 * The current page number to start fetching data from. Passing NULL will fetch first page data
	 * @param pageSize
	 * The total records to be fetched per request. Passing NULL will consider size as 20
	 * @param dateFilterFields
	 * Comma separated filter fields for the queue configuration which are of 'date' type
	 * @return
	 * The Queue's dataset wrapped
	 * @throws Exception
	 */
	public QueueResponseWrapper getQueueData(Integer queueId, String userLocale, String searchFilter, String sortField, String sortOrder,
			Integer pageNumber, Integer pageSize, String dateFilterFields) throws Exception {
		try {
			return queueServiceBS.getQueueData(queueId, userLocale, searchFilter, sortField, sortOrder, pageNumber, pageSize, dateFilterFields);
		}
		catch (Exception e) {
		    if(logger.isErrorEnabled())
			    logger.error("Unexpected error ocurred while fetching applications from queue", e);
			throw e;
		}
	}
	
	/**
	 * Returns the count of applications in the specified queue as per the filters & validates it
	 * @param queue
	 * The Queue instance
	 * @param userLocale
	 * The user's current locale
	 * @param dateFilterFields
	 * Comma separated filter fields for the queue configuration which are of 'date' type
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<Object> getDynamicQueryResultCount(Queue queue, String userLocale, String dateFilterFields) {
		try {
			return new ResponseEntity<Object>(queueServiceBS.getDynamicQueryResultCount(queue, userLocale, dateFilterFields), HttpStatus.OK);
		} catch (Exception e) {
		    if(logger.isErrorEnabled())
			    logger.error("Unexpected error occurred while trying to validate Queue information", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Method to fetch the application Ids for applications matching the supplied Queue
	 * @param queueId
	 * The Queue Id
	 * @param userLocale
	 * The runtime user's system locale
	 * @param pageNumber
	 * The page number to fetch data from
	 * @param pageSize
	 * The limit of number of records to fetch per query/page
	 * @param dateFilterFields
	 * Comma separated list of queue configuration fields that are of type 'date'
	 * @return
	 * The list of application Ids matching the given Queue's filter criteria
	 * @throws Exception
	 */
	public List<Integer> getApplicationsInQueue(Integer queueId, String userLocale, Integer pageNumber, Integer pageSize,
			String dateFilterFields) throws Exception {
		try {
			return queueServiceBS.getApplicationsInQueue(queueId, userLocale, pageNumber, pageSize, dateFilterFields);
		} catch (Exception e) {
		    if(logger.isErrorEnabled())
			    logger.error("Unexpected error occurred while trying to fetch application Ids for the given Queue", e);
			throw e;
		}
	}
	
	/**
	 * Fetches the list of all Queues from the system that the current logged in USER has access to
	 * @return
	 * @throws Exception
	 */
	public List<Queue> getQueuesByLoggedinUser() {
		List<Queue> accessibleQueues = new ArrayList<>();
		try {
			 accessibleQueues.addAll(queueServiceBS.getQueuesByLoggedinUser());
		} catch (Exception e) {
		    if(logger.isErrorEnabled())
			    logger.error("Unexpected error occurred while trying to fetch Queues accessible to the logged in User", e);
		}
		
		return accessibleQueues;
	}

}