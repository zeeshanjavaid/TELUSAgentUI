package com.fico.core.services;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fico.core.util.CommonUtils;
import com.fico.core.util.QueueSortMetaModel;
import com.fico.dmp.telusagentuidb.Queue;
import com.fico.dmp.telusagentuidb.QueueGroup;
import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.models.query.QueryGetDomainValueByIdResponse;
import com.fico.dmp.telusagentuidb.models.query.QueryGetQueuesByUserIdResponse;
import com.fico.dmp.telusagentuidb.service.QueueGroupService;
import com.fico.dmp.telusagentuidb.service.QueueService;
import com.fico.dmp.telusagentuidb.service.TELUSAgentUIDBQueryExecutorService;
import com.fico.ps.model.queue.FilterCriteria;
import com.fico.ps.model.queue.FilterGroup;
import com.fico.ps.model.queue.QueueReportResponse;
import com.fico.ps.model.queue.QueueResponse;
import com.fico.ps.model.queue.QueueResponseWrapper;
import com.fico.pscomponent.handlers.UserManagementHandler;
import com.fico.qb.query.SqlQueryBuilderFactory;
import com.fico.qb.query.builder.support.builder.SqlBuilder;
import com.fico.qb.query.builder.support.model.result.SqlQueryResult;

@Service("facade.QueueServiceBS")
public class QueueServiceBS {

	@Autowired
	private QueueService queueService;

	@Autowired
	private QueueGroupService queueGroupService;
	
	@Autowired
	private UserManagementHandler userManagementHandler;
	
//	@Autowired
//	@Qualifier("CoreWMManagedDataSource")
//	private DataSource dataSource;
	
	@Autowired
	@Qualifier("facade.DomainValueServiceBS")
	private DomainValueServiceBS domainValueServiceBS;
	
	@Autowired
	private TELUSAgentUIDBQueryExecutorService telusAgentUIDBQueryExecutorService;
	
	@Autowired
	@Qualifier("core.CommonUtils")
	private CommonUtils commonUtils;
	
	@Autowired
	@Qualifier("coreJDBCTemplate")
	private JdbcTemplate jdbcTemplate;

	private ObjectMapper mapper = new ObjectMapper();

	private static final Logger logger = LoggerFactory.getLogger(QueueServiceBS.class);

	private static final String QUEUE_WRAPPER_QUERY = QueueQueryKeywords.SELECT.getKeyword() + " APPLICATION.id AS applicationId, APPLICATION.applicationNumber AS applicationNumber, AppStatusDVDsc.description AS applicationStatus,"
				+ " CRTypeDVDsc.description AS caseReviewType, CaseReview.reviewLevel AS caseReviewLevel, AppChannelDVDsc.description AS applicationChannel, CONCAT(PERSON.firstName, ' ', PERSON.lastName) AS customerName,"
				+ " PERSON.dateOfBirth AS dateOfBirth, ProvinceDVDsc.description AS province, ADDRESS.postalCode AS postalCode, "
				+ " (SELECT GROUP_CONCAT(DISTINCT CONCAT(PHONE.phoneNumber, ' (', LOWER(SUBSTRING(PHONE.personPhoneType, 1, 1)), ')') SEPARATOR '|') FROM PHONE WHERE PHONE.partyId = PARTY.id) AS phoneNumber,"
				+ " PERSON.motherMaidenName AS motherMadName, Employment.employerName AS employer, EmpJobCodeDVDsc.description AS empJobCode, EMAIL.email AS emailId, APPLICATION_DETAILS.iPAddress AS IPAddress,"
				+ "	APPLICATION_DETAILS.createdOn AS appCreatedOn, APPLICATION_DETAILS.updatedOn AS appUpdatedOn, CONCAT(AppCreatedUser.firstName, ' ', AppCreatedUser.lastName) AS createdByUser, CONCAT(AppUpdatedUser.firstName, ' ', AppUpdatedUser.lastName) AS updatedByUser,"
				+ "	DATEDIFF(CURDATE(), DATE(APPLICATION_DETAILS.createdOn)) AS appCreatedSince, DATEDIFF(CURDATE(), DATE(APPLICATION_DETAILS.ownedOn)) AS appOwnedSince, APPLICATION_DETAILS.onHoldUntilDate AS appHoldUntil,"
				+ "	APPLICATION_DETAILS.onHoldReason AS appHoldReason, CONCAT(AppLockUser.firstName, ' ', AppLockUser.lastName) AS lockedByUser "
				+ " " + QueueQueryKeywords.FROM.getKeyword() + " "
				+ "    APPLICATION LEFT JOIN APPLICATION_DETAILS ON APPLICATION.id = APPLICATION_DETAILS.applicationId "
				+ "    LEFT JOIN DomainValue AS AppStatusDV ON APPLICATION_DETAILS.applicationStatus = AppStatusDV.id "
				+ "    LEFT JOIN DomainValueDescription AS AppStatusDVDsc ON AppStatusDV.id = AppStatusDVDsc.DomainValueId "
				+ "    LEFT JOIN DomainValue AS AppChannelDV ON APPLICATION_DETAILS.channel = AppChannelDV.id "
				+ "    LEFT JOIN DomainValueDescription AS AppChannelDVDsc ON AppChannelDV.id = AppChannelDVDsc.DomainValueId "
				+ "    LEFT JOIN DomainValue AS AppLangDV ON APPLICATION_DETAILS.languagecode = AppLangDV.id "
				+ "    LEFT JOIN DomainValueDescription AS AppLangDVDsc ON AppLangDV.id = AppLangDVDsc.DomainValueId "
				+ "    LEFT JOIN ApplicationLock ON APPLICATION.id = ApplicationLock.applicationId "
				+ "    LEFT JOIN PARTY ON APPLICATION.id = PARTY.ApplicationId "
				+ "    LEFT JOIN PERSON ON PARTY.id = PERSON.partyId "
				+ "	   LEFT JOIN CalculatedValues ON PARTY.ID = CalculatedValues.partyId "
				+ "	   LEFT JOIN ADDRESS ON PARTY.ID = ADDRESS.partyId "
				+ "    LEFT JOIN DomainValue AS ProvinceDV ON ADDRESS.state = ProvinceDV.id "
				+ "    LEFT JOIN DomainValueDescription AS ProvinceDVDsc ON ProvinceDV.id = ProvinceDVDsc.DomainValueId "
				+ "	   LEFT JOIN Employment ON PERSON.partyId = Employment.personId "
				+ "    LEFT JOIN DomainValue As EmpJobCodeDV ON Employment.jobCode = EmpJobCodeDV.id "
				+ "    LEFT JOIN DomainValueDescription AS EmpJobCodeDVDsc ON EmpJobCodeDV.id = EmpJobCodeDVDsc.DomainValueId "
				+ "    LEFT JOIN EMAIL ON PARTY.id = EMAIL.partyId "
				+ "    LEFT JOIN PRODUCT AS AppProduct ON APPLICATION_DETAILS.requestedProduct = AppProduct.id "
				+ "    LEFT JOIN USER AS AppCreatedUser ON APPLICATION_DETAILS.createdBy = AppCreatedUser.id "
				+ "    LEFT JOIN USER AS AppUpdatedUser ON APPLICATION_DETAILS.updatedBy = AppUpdatedUser.id "
				+ "    LEFT JOIN USER AS AppLockUser ON ApplicationLock.lockedBy = AppLockUser.id "
				+ "    LEFT JOIN CaseReview ON APPLICATION.id = CaseReview.applicationId "
				+ "    LEFT JOIN DomainValue AS CRTypeDV ON CaseReview.reviewType = CRTypeDV.id "
				+ "    LEFT JOIN DomainValueDescription AS CRTypeDVDsc ON CRTypeDV.id = CRTypeDVDsc.DomainValueId "
				+ "	   LEFT JOIN DomainValue AS CRStatusDV ON CaseReview.status = CRStatusDV.id "
				+ "	   LEFT JOIN DomainValueDescription AS CRStatusDVDsc ON CRStatusDV.id = CRStatusDVDsc.DomainValueId "
				+ " " + QueueQueryKeywords.WHERE.getKeyword() + " "
				+ "    (CASE WHEN" + "        APPLICATION_DETAILS.applicationStatus IS NULL THEN TRUE "
				+ "    ELSE" + "        CASE WHEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "        END" + "    END AND " + "    CASE WHEN"
				+ "        APPLICATION_DETAILS.channel IS NULL THEN TRUE " + "    ELSE" + "        CASE WHEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "        END END) AND "
				+ "	   (CASE WHEN APPLICATION_DETAILS.languagecode IS NULL THEN TRUE "
				+ "    ELSE  CASE WHEN "
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "    END END) AND "
				+ "	   (CASE WHEN Employment.jobCode IS NULL THEN TRUE "
				+ "    ELSE  CASE WHEN "
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "    END END) AND "
				+ "	   (CASE WHEN ADDRESS.state IS NULL THEN TRUE "
				+ "    ELSE  CASE WHEN "
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "    END END) AND "
				+ "    (CASE WHEN CaseReview.id IS NULL THEN TRUE "
				+ "    ELSE "
				+ "	   	(CASE WHEN CaseReview.reviewType IS NULL THEN TRUE "
				+ "    	ELSE "
				+ "    	   CASE WHEN (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "    END END) END) AND "
				+ " CaseReview.id = (SELECT CaseReview.id FROM CaseReview WHERE CaseReview.applicationId = APPLICATION.id ORDER BY CaseReview.id DESC LIMIT 0, 1) AND "
				+ " ADDRESS.id = (SELECT ADDRESS.id FROM ADDRESS WHERE ADDRESS.partyId = PARTY.id AND ADDRESS.addressType = '1') AND "
				/* Line 177 actually maps the ADDRESS.addressType to DomainValue ADDRESS_TYPE, but persistence & UI has static value mappings (NOTE: enable this once DV relationship is established) */
//				+ " ADDRESS.id = (SELECT ADDRESS.id FROM ADDRESS LEFT JOIN DomainValue AS ATDV ON ADDRESS.addressType = ATDV.id WHERE ADDRESS.partyId = PARTY.id AND ATDV.code = 'ADDRESS_TYPE_PHYSICAL') AND "
				+ " APPLICATION.id = " + QueueQueryKeywords.WRAPPER_QUERY_APPLICATION_ID.getKeyword();

	private static final String QUEUE_DYNAMIC_FILTER_QUERY = QueueQueryKeywords.SELECT.getKeyword() + " APPLICATION.id AS applicationId "
				+ " " + QueueQueryKeywords.FROM.getKeyword() + " "
				+ "    APPLICATION LEFT JOIN APPLICATION_DETAILS ON APPLICATION.id = APPLICATION_DETAILS.applicationId "
				+ "    LEFT JOIN DomainValue AS AppStatusDV ON APPLICATION_DETAILS.applicationStatus = AppStatusDV.id "
				+ "    LEFT JOIN DomainValueDescription AS AppStatusDVDsc ON AppStatusDV.id = AppStatusDVDsc.DomainValueId "
				+ "    LEFT JOIN DomainValue AS AppChannelDV ON APPLICATION_DETAILS.channel = AppChannelDV.id "
				+ "    LEFT JOIN DomainValueDescription AS AppChannelDVDsc ON AppChannelDV.id = AppChannelDVDsc.DomainValueId "
				+ "    LEFT JOIN DomainValue AS AppLangDV ON APPLICATION_DETAILS.languagecode = AppLangDV.id "
				+ "    LEFT JOIN DomainValueDescription AS AppLangDVDsc ON AppLangDV.id = AppLangDVDsc.DomainValueId "
				+ "    LEFT JOIN ApplicationLock ON APPLICATION.id = ApplicationLock.applicationId "
				+ "    LEFT JOIN PARTY ON APPLICATION.id = PARTY.ApplicationId "
				+ "    LEFT JOIN PERSON ON PARTY.id = PERSON.partyId "
				+ "	   LEFT JOIN CalculatedValues ON PARTY.ID = CalculatedValues.partyId "
				+ "	   LEFT JOIN ADDRESS ON PARTY.ID = ADDRESS.partyId "
				+ "    LEFT JOIN DomainValue AS ProvinceDV ON ADDRESS.state = ProvinceDV.id "
				+ "    LEFT JOIN DomainValueDescription AS ProvinceDVDsc ON ProvinceDV.id = ProvinceDVDsc.DomainValueId "
				+ "	   LEFT JOIN Employment ON PERSON.partyId = Employment.personId "
				+ "    LEFT JOIN DomainValue As EmpJobCodeDV ON Employment.jobCode = EmpJobCodeDV.id "
				+ "    LEFT JOIN DomainValueDescription AS EmpJobCodeDVDsc ON EmpJobCodeDV.id = EmpJobCodeDVDsc.DomainValueId "
				+ "    LEFT JOIN PHONE ON PARTY.id = PHONE.partyId "
				+ "    LEFT JOIN EMAIL ON PARTY.id = EMAIL.partyId "
				+ "    LEFT JOIN PRODUCT AS AppProduct ON APPLICATION_DETAILS.requestedProduct = AppProduct.id "
				+ "    LEFT JOIN USER AS AppCreatedUser ON APPLICATION_DETAILS.createdBy = AppCreatedUser.id "
				+ "    LEFT JOIN USER AS AppUpdatedUser ON APPLICATION_DETAILS.updatedBy = AppUpdatedUser.id "
				+ "    LEFT JOIN USER AS AppLockUser ON ApplicationLock.lockedBy = AppLockUser.id "
				+ "    LEFT JOIN CaseReview ON APPLICATION.id = CaseReview.applicationId "
				+ "    LEFT JOIN DomainValue AS CRTypeDV ON CaseReview.reviewType = CRTypeDV.id "
				+ "    LEFT JOIN DomainValueDescription AS CRTypeDVDsc ON CRTypeDV.id = CRTypeDVDsc.DomainValueId "
				+ "	   LEFT JOIN DomainValue AS CRStatusDV ON CaseReview.status = CRStatusDV.id "
				+ "	   LEFT JOIN DomainValueDescription AS CRStatusDVDsc ON CRStatusDV.id = CRStatusDVDsc.DomainValueId "	
				+ " " + QueueQueryKeywords.WHERE.getKeyword() + " "
				+ "    CASE WHEN" + "        APPLICATION_DETAILS.applicationStatus IS NULL THEN TRUE "
				+ "    ELSE" + "        CASE WHEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "        END" + "    END AND " + "    CASE WHEN"
				+ "        APPLICATION_DETAILS.channel IS NULL THEN TRUE " + "    ELSE" + "        CASE WHEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "        END" + "    END AND "
				+ "	   CASE WHEN APPLICATION_DETAILS.languagecode IS NULL THEN TRUE "
				+ "    ELSE  CASE WHEN "
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "    END END AND "
				+ "	   CASE WHEN Employment.jobCode IS NULL THEN TRUE "
				+ "    ELSE  CASE WHEN "
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "    END END AND "
				+ "	   CASE WHEN ADDRESS.state IS NULL THEN TRUE "
				+ "    ELSE  CASE WHEN "
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "    END END AND "
				+ "    CASE WHEN CaseReview.id IS NULL THEN TRUE "
				+ "    ELSE "
				+ "	   	CASE WHEN CaseReview.reviewType IS NULL THEN TRUE "
				+ "    ELSE "
				+ "        CASE WHEN  (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = 'en')"
				+ "        ELSE"
				+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
				+ "        END END END  " 
				+ " " + QueueQueryKeywords.QUEUE_WHERE_FILTER_CONDITION.getKeyword() + QueueQueryKeywords.PERSONAL_QUEUE_WHERE_FILTER_CONDITION.getKeyword() 
				+ " " + QueueQueryKeywords.GROUP_BY.getKeyword() + " " + " APPLICATION.id";
	
	private static final String QUEUE_REPORT_QUERY = "SELECT AppSinceSubQuery.appCreatedSince AS appPeriod, COUNT(AppSinceSubQuery.applicationId) AS appCount FROM ("
			+ " " + QueueQueryKeywords.SELECT.getKeyword() + " APPLICATION.id AS applicationId, DATEDIFF(CURRENT_DATE(), DATE(APPLICATION_DETAILS.createdOn)) AS appCreatedSince "
			+ " " + QueueQueryKeywords.FROM.getKeyword()
			+ "    APPLICATION LEFT JOIN APPLICATION_DETAILS ON APPLICATION.id = APPLICATION_DETAILS.applicationId "
			+ "    LEFT JOIN DomainValue AS AppStatusDV ON APPLICATION_DETAILS.applicationStatus = AppStatusDV.id "
			+ "    LEFT JOIN DomainValueDescription AS AppStatusDVDsc ON AppStatusDV.id = AppStatusDVDsc.DomainValueId "
			+ "    LEFT JOIN DomainValue AS AppChannelDV ON APPLICATION_DETAILS.channel = AppChannelDV.id "
			+ "    LEFT JOIN DomainValueDescription AS AppChannelDVDsc ON AppChannelDV.id = AppChannelDVDsc.DomainValueId "
			+ "    LEFT JOIN DomainValue AS AppLangDV ON APPLICATION_DETAILS.languagecode = AppLangDV.id "
			+ "    LEFT JOIN DomainValueDescription AS AppLangDVDsc ON AppLangDV.id = AppLangDVDsc.DomainValueId "
			+ "    LEFT JOIN ApplicationLock ON APPLICATION.id = ApplicationLock.applicationId "
			+ "    LEFT JOIN PARTY ON APPLICATION.id = PARTY.ApplicationId "
			+ "    LEFT JOIN PERSON ON PARTY.id = PERSON.partyId "
			+ "	   LEFT JOIN CalculatedValues ON PARTY.ID = CalculatedValues.partyId "
			+ "	   LEFT JOIN ADDRESS ON PARTY.ID = ADDRESS.partyId "
			+ "    LEFT JOIN DomainValue AS ProvinceDV ON ADDRESS.state = ProvinceDV.id "
			+ "    LEFT JOIN DomainValueDescription AS ProvinceDVDsc ON ProvinceDV.id = ProvinceDVDsc.DomainValueId "
			+ "	   LEFT JOIN Employment ON PERSON.partyId = Employment.personId "
			+ "    LEFT JOIN DomainValue As EmpJobCodeDV ON Employment.jobCode = EmpJobCodeDV.id "
			+ "    LEFT JOIN DomainValueDescription AS EmpJobCodeDVDsc ON EmpJobCodeDV.id = EmpJobCodeDVDsc.DomainValueId "
			+ "    LEFT JOIN PHONE ON PARTY.id = PHONE.partyId "
			+ "    LEFT JOIN EMAIL ON PARTY.id = EMAIL.partyId "
			+ "    LEFT JOIN PRODUCT AS AppProduct ON APPLICATION_DETAILS.requestedProduct = AppProduct.id "
			+ "    LEFT JOIN USER AS AppCreatedUser ON APPLICATION_DETAILS.createdBy = AppCreatedUser.id "
			+ "    LEFT JOIN USER AS AppUpdatedUser ON APPLICATION_DETAILS.updatedBy = AppUpdatedUser.id "
			+ "    LEFT JOIN USER AS AppLockUser ON ApplicationLock.lockedBy = AppLockUser.id "
			+ "    LEFT JOIN CaseReview ON APPLICATION.id = CaseReview.applicationId "
			+ "    LEFT JOIN DomainValue AS CRTypeDV ON CaseReview.reviewType = CRTypeDV.id "
			+ "    LEFT JOIN DomainValueDescription AS CRTypeDVDsc ON CRTypeDV.id = CRTypeDVDsc.DomainValueId "
			+ "	   LEFT JOIN DomainValue AS CRStatusDV ON CaseReview.status = CRStatusDV.id "
			+ "	   LEFT JOIN DomainValueDescription AS CRStatusDVDsc ON CRStatusDV.id = CRStatusDVDsc.DomainValueId "	
			+ " " + QueueQueryKeywords.WHERE.getKeyword() + " "
			+ "    CASE WHEN" + "        APPLICATION_DETAILS.applicationStatus IS NULL THEN TRUE "
			+ "    ELSE" + "        CASE WHEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = 'en')"
			+ "        ELSE"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppStatusDV.id AND DomainValueDescription.id = AppStatusDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
			+ "        END" + "    END AND " + "    CASE WHEN"
			+ "        APPLICATION_DETAILS.channel IS NULL THEN TRUE " + "    ELSE" + "        CASE WHEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = 'en')"
			+ "        ELSE"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppChannelDV.id AND DomainValueDescription.id = AppChannelDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
			+ "        END" + "    END AND "
			+ "	   CASE WHEN APPLICATION_DETAILS.languagecode IS NULL THEN TRUE "
			+ "    ELSE  CASE WHEN "
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = 'en')"
			+ "        ELSE"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = AppLangDV.id AND DomainValueDescription.id = AppLangDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
			+ "    END END AND "
			+ "	   CASE WHEN Employment.jobCode IS NULL THEN TRUE "
			+ "    ELSE  CASE WHEN "
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = 'en')"
			+ "        ELSE"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = EmpJobCodeDV.id AND DomainValueDescription.id = EmpJobCodeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
			+ "    END END AND "
			+ "	   CASE WHEN ADDRESS.state IS NULL THEN TRUE "
			+ "    ELSE  CASE WHEN "
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = 'en')"
			+ "        ELSE"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = ProvinceDV.id AND DomainValueDescription.id = ProvinceDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
			+ "    END END AND "
			+ "    CASE WHEN CaseReview.id IS NULL THEN TRUE "
			+ "    ELSE "
			+ "	   	CASE WHEN CaseReview.reviewType IS NULL THEN TRUE "
			+ "    ELSE "
			+ "        CASE WHEN  (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"') IS NULL THEN"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = 'en')"
			+ "        ELSE"
			+ "            (SELECT DomainValue.id FROM DomainValue INNER JOIN DomainValueDescription ON DomainValue.id = DomainValueDescription.DomainValueId WHERE DomainValue.id = CRTypeDV.id AND DomainValueDescription.id = CRTypeDVDsc.id AND DomainValueDescription.Locale = '"+ QueueQueryKeywords.USER_LOCALE.getKeyword() +"')"
			+ "        END END END  " 
			+ " " + QueueQueryKeywords.QUEUE_WHERE_FILTER_CONDITION.getKeyword() + QueueQueryKeywords.PERSONAL_QUEUE_WHERE_FILTER_CONDITION.getKeyword() 
			+ " " + QueueQueryKeywords.GROUP_BY.getKeyword() + " " + " APPLICATION.id) AS AppSinceSubQuery GROUP BY AppSinceSubQuery.appCreatedSince ORDER BY AppSinceSubQuery.appCreatedSince";
	
//	@PostConstruct
//	public void init() {
//		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
//	}

	/**
	 * Method to create/update a queue into the system
	 * 
	 * @param queue
	 * @param listGroups
	 * @return
	 * @throws Exception
	 */
	public Queue createUpdateDynamicQuery(Queue queue, Integer... listGroups) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'createUpdateDynamicQueue'");
		if (queue == null) {
			if(logger.isErrorEnabled())
				logger.error("Queue data is missing!");
			throw new Exception("Queue data is missing from the request!");
		}

		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		Page<Queue> existingQueues = queueService.findAll("", pageable);
		if (existingQueues.hasContent()) {
			List<Queue> existingQueuesList = existingQueues.getContent();
			for (Queue existinQueue : existingQueuesList) {
				if (existinQueue.getRank() == queue.getRank() && existinQueue.getId() != queue.getId()) {
					throw new IOException("Queue rank should be unique");
				}
			}
			for (Queue existinQueue : existingQueuesList) {
				if (existinQueue.getIsLocked() != null && existinQueue.getId() == queue.getId()) {
					if (existinQueue.getIsLocked() && existinQueue.getLockedBy() != null) {
						if (existinQueue.getLockedBy() != queue.getUpdatedBy()) {
							throw new IOException("Queue is locked, please try later");
						}
					} else if (existinQueue.getIsLocked() && existinQueue.getLockedBy() == null) {
						throw new IOException("Queue is locked, please try later");
					}
				}
			}
		}

		// dynamic where clause generation + (conditional where clause if any)
		queue.setFilterCriteriaQuery(buildDynamicSQLQuery(queue, false));
		
		// Escaping special characters encoding in queue description
        if(queue.getDescription() != null && queue.getDescription().length() > 0){
		    queue.setDescription(StringEscapeUtils.unescapeHtml4(queue.getDescription()));
		}

		boolean newRecord = true;
		if (queue.getId() != null && queue.getId() > 0) {
			queue = queueService.update(queue);
			newRecord = false;
		} else {
			queue = queueService.create(queue);
		}

		// for groups assignment
		if (listGroups != null && listGroups.length > 0) {
			final Queue Q_Copy = queue;
			if(newRecord) {
				//save the new associations
				Arrays.asList(listGroups).forEach((groupId) -> {
					QueueGroup queueGroupDTO = new QueueGroup();
					queueGroupDTO.setQueueId(Q_Copy.getId());
					queueGroupDTO.setGroupId(groupId);
					queueGroupService.create(queueGroupDTO);
				});
			}
			else {
				//find list of existing Queue_Group records
				List<QueueGroup> existingQueueGroups = queueGroupService.
						findAll("queueId=" + queue.getId(), PageRequest.of(0, Integer.MAX_VALUE)).toList();
				
				//delete the older Queue_Group entries if existing
				if(existingQueueGroups != null && !existingQueueGroups.isEmpty()) {
					existingQueueGroups.forEach((qGrp) -> {
						queueGroupService.delete(qGrp.getId());
					});
				}
				
				//save the new associations
				Arrays.asList(listGroups).forEach((groupId) -> {
					QueueGroup queueGroupDTO = new QueueGroup();
					queueGroupDTO.setQueueId(Q_Copy.getId());
					queueGroupDTO.setGroupId(groupId);
					queueGroupService.create(queueGroupDTO);
				});
			}
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'createUpdateDynamicQueue'. Queue data saved");
		return queue;
	}

	/**
	 * Executes report query for each active Queue within the system
	 * @param reportId
	 * The report Id against which the report is getting generated
	 * @param loggedInUser
	 * The current logged in User
	 * @param dateFilterFields
	 * The list of filters fields that are of type 'date'
	 * @return
	 * The list of {@link QueueReportResponse} wrapping each Queue report as one record
	 * @throws Exception
	 */
	public List<QueueReportResponse> executeQueueReport(String reportId, User loggedInUser, String dateFilterFields) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeQueueReport'");
		
		List<QueueReportResponse> queueReportResponse = null;
		List<Queue> allActiveQueues = queueService.findAll("isActive=1",
				PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"))).toList();
		
		if(allActiveQueues != null && !allActiveQueues.isEmpty()) {
			queueReportResponse = new ArrayList<>();
			
			//for each queue
			for(Queue queue : allActiveQueues) {
				//the query
				String adjusted_SQLClause = buildReportQueryForQueue(queue, loggedInUser);
				
				//tag the received user locale
				adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.USER_LOCALE.getKeyword(), "en");
				
				//adjust filters for 'date' type fields
				adjusted_SQLClause = assignTruncForDateTimeFields(adjusted_SQLClause, dateFilterFields);
				
				//replace the SQL template keywords
				adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.SELECT.getKeyword(), "SELECT ").
						replace(QueueQueryKeywords.FROM.getKeyword(), " FROM ").replace(QueueQueryKeywords.WHERE.getKeyword(), " WHERE ").
						replace(QueueQueryKeywords.GROUP_BY.getKeyword(), " GROUP BY ").replace(QueueQueryKeywords.ORDER_BY.getKeyword(), " ORDER BY ");
				
				//for boolean equal operators
				adjusted_SQLClause = adjusted_SQLClause.replace("= 'false'", "= false").replace("= 'true'", "= true");
				
				if(logger.isInfoEnabled())
					logger.info("#----- The final report SQL clause for Queue: "+ queue.getName() + " \n "
						+ "["+ adjusted_SQLClause +"] \n-----#");
				
				/*
				 * 1. Execute count query to set the total number of records (required for paginated view)
				 */
				List<QueueReportQueryResponseWrapper> reportReponseWrapper = null;
				reportReponseWrapper = jdbcTemplate.query(adjusted_SQLClause, new PreparedStatementSetter() {
					
					public void setValues(PreparedStatement preparedStatement) throws SQLException {
						
						}
					},
					new QueueReportMapper());
				
				//process the report query response
				if(reportReponseWrapper != null) {
					QueueReportResponse qRepResponse = new QueueReportResponse();
					qRepResponse.setQueueName(queue.getName());
					qRepResponse.setReportId(reportId);
					
					if(reportReponseWrapper == null || reportReponseWrapper.isEmpty());
					else {
						//Period 0
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 0)).count() > 0) {
							qRepResponse.setPeriod_0(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 0)).
									findFirst().get().getApplicationCount());
						}
						//Period 1
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 1)).count() > 0) {
							qRepResponse.setPeriod_1(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 1)).
									findFirst().get().getApplicationCount());
						}
						//Period 2
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 2)).count() > 0) {
							qRepResponse.setPeriod_2(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 2)).
									findFirst().get().getApplicationCount());
						}
						//Period 3
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 3)).count() > 0) {
							qRepResponse.setPeriod_3(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 3)).
									findFirst().get().getApplicationCount());
						}
						//Period 4
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 4)).count() > 0) {
							qRepResponse.setPeriod_4(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 4)).
									findFirst().get().getApplicationCount());
						}
						//Period 5
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 5)).count() > 0) {
							qRepResponse.setPeriod_5(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 5)).
									findFirst().get().getApplicationCount());
						}
						//Period 6-10
						if(reportReponseWrapper.stream().filter(repQueryRes -> (repQueryRes.getApplicationPeriod() >= 6 &&
								repQueryRes.getApplicationPeriod() <= 10)).count() > 0) {
							final List<QueueReportQueryResponseWrapper> subList = reportReponseWrapper.stream().
									filter(repQueryRes -> (repQueryRes.getApplicationPeriod() >= 6 &&
									repQueryRes.getApplicationPeriod() <= 10)).collect(Collectors.toList());
							long sumCount = 0;
							
							//for each sub-period matched
							for(QueueReportQueryResponseWrapper subMatchPeriod : subList) {
								sumCount = sumCount + subMatchPeriod.getApplicationCount();
							}
							
							qRepResponse.setPeriod_6_10(sumCount);
						}
						//Period 10+
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod() > 10).count() > 0) {
							final List<QueueReportQueryResponseWrapper> subList = reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod() > 10).collect(Collectors.toList());
							long sumCount = 0;
							
							//for each sub-period matched
							for(QueueReportQueryResponseWrapper subMatchPeriod : subList) {
								sumCount = sumCount + subMatchPeriod.getApplicationCount();
							}
							
							qRepResponse.setPeriod_10_plus(sumCount);
						}
					} //check if the Queue has applications
					
					//add the Queue report response to output list
					queueReportResponse.add(qRepResponse);
				}	
			} //Queue loop end
		}
		
		return queueReportResponse;
	}
	
	/**
	 * Executes report query for each active Queue within the system
	 * @param reportId
	 * The report Id against which the report is getting generated
	 * @param onlyPersonalQueues
	 * Set this to <b><i>TRUE</i></b> to fetch report data only for personal queues in the system
	 * @param loggedInUser
	 * The current logged in User
	 * @param dateFilterFields
	 * The list of filters fields that are of type 'date'
	 * @return
	 * The list of {@link QueueReportResponse} wrapping each Queue report as one record
	 * @throws Exception
	 */
	public List<QueueReportResponse> executeQueueReport(String reportId, Boolean onlyPersonalQueues, User loggedInUser, String dateFilterFields) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeQueueReport'");
		List<QueueReportResponse> queueReportResponse = null;
		List<Queue> allActiveQueues = null;
		
		//get all non-personal queues from the system
		if(onlyPersonalQueues == null || onlyPersonalQueues.booleanValue() == false) {
			allActiveQueues = queueService.findAll(("isActive=1 AND (isPersonalQueue IS NULL OR isPersonalQueue=0)"),
					PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"))).toList();
		}
		//otherwise only get personal queues from the system
		else if(onlyPersonalQueues.booleanValue() == true && loggedInUser != null) {
			allActiveQueues = queueService.findAll(("isActive=1 AND isPersonalQueue IS NOT NULL AND isPersonalQueue=1"),
					PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"))).toList();
		}
		else {
			if(logger.isWarnEnabled())
				logger.warn("-----Inside method 'executeQueueReport'. Parameter 'onlyPersonalQueues' set to TRUE but no loggedIn USER details provided."
					+ " No report will be generated for any available Personal Queues within the system");
			return null;
		}
		
		if(allActiveQueues != null && !allActiveQueues.isEmpty()) {
			queueReportResponse = new ArrayList<>();
			
			//for each queue
			for(Queue queue : allActiveQueues) {
				//the query
				String adjusted_SQLClause = buildReportQueryForQueue(queue, loggedInUser);
				
				//tag the received user locale
				adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.USER_LOCALE.getKeyword(), "en");
				
				//adjust filters for 'date' type fields
				adjusted_SQLClause = assignTruncForDateTimeFields(adjusted_SQLClause, dateFilterFields);
				
				//replace the SQL template keywords
				adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.SELECT.getKeyword(), "SELECT ").
						replace(QueueQueryKeywords.FROM.getKeyword(), " FROM ").replace(QueueQueryKeywords.WHERE.getKeyword(), " WHERE ").
						replace(QueueQueryKeywords.GROUP_BY.getKeyword(), " GROUP BY ").replace(QueueQueryKeywords.ORDER_BY.getKeyword(), " ORDER BY ");
				
				//for boolean equal operators
				adjusted_SQLClause = adjusted_SQLClause.replace("= 'false'", "= false").replace("= 'true'", "= true");
				
				if(logger.isInfoEnabled())
					logger.info("#----- The final report SQL clause for Queue: "+ queue.getName() + " \n "
						+ "["+ adjusted_SQLClause +"] \n-----#");
				
				/*
				 * 1. Execute count query to set the total number of records (required for paginated view)
				 */
				List<QueueReportQueryResponseWrapper> reportReponseWrapper = null;
				reportReponseWrapper = jdbcTemplate.query(adjusted_SQLClause, new PreparedStatementSetter() {
					
					public void setValues(PreparedStatement preparedStatement) throws SQLException {
						
						}
					},
					new QueueReportMapper());
				
				//process the report query response
				if(reportReponseWrapper != null) {
					QueueReportResponse qRepResponse = new QueueReportResponse();
					qRepResponse.setQueueName(queue.getName());
					qRepResponse.setReportId(reportId);
					
					if(reportReponseWrapper == null || reportReponseWrapper.isEmpty());
					else {
						//Period 0
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 0)).count() > 0) {
							qRepResponse.setPeriod_0(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 0)).
									findFirst().get().getApplicationCount());
						}
						//Period 1
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 1)).count() > 0) {
							qRepResponse.setPeriod_1(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 1)).
									findFirst().get().getApplicationCount());
						}
						//Period 2
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 2)).count() > 0) {
							qRepResponse.setPeriod_2(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 2)).
									findFirst().get().getApplicationCount());
						}
						//Period 3
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 3)).count() > 0) {
							qRepResponse.setPeriod_3(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 3)).
									findFirst().get().getApplicationCount());
						}
						//Period 4
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 4)).count() > 0) {
							qRepResponse.setPeriod_4(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 4)).
									findFirst().get().getApplicationCount());
						}
						//Period 5
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 5)).count() > 0) {
							qRepResponse.setPeriod_5(reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod().equals((long) 5)).
									findFirst().get().getApplicationCount());
						}
						//Period 6-10
						if(reportReponseWrapper.stream().filter(repQueryRes -> (repQueryRes.getApplicationPeriod() >= 6 &&
								repQueryRes.getApplicationPeriod() <= 10)).count() > 0) {
							final List<QueueReportQueryResponseWrapper> subList = reportReponseWrapper.stream().
									filter(repQueryRes -> (repQueryRes.getApplicationPeriod() >= 6 &&
									repQueryRes.getApplicationPeriod() <= 10)).collect(Collectors.toList());
							long sumCount = 0;
							
							//for each sub-period matched
							for(QueueReportQueryResponseWrapper subMatchPeriod : subList) {
								sumCount = sumCount + subMatchPeriod.getApplicationCount();
							}
							
							qRepResponse.setPeriod_6_10(sumCount);
						}
						//Period 10+
						if(reportReponseWrapper.stream().filter(repQueryRes -> repQueryRes.getApplicationPeriod() > 10).count() > 0) {
							final List<QueueReportQueryResponseWrapper> subList = reportReponseWrapper.stream().
									filter(repQueryRes -> repQueryRes.getApplicationPeriod() > 10).collect(Collectors.toList());
							long sumCount = 0;
							
							//for each sub-period matched
							for(QueueReportQueryResponseWrapper subMatchPeriod : subList) {
								sumCount = sumCount + subMatchPeriod.getApplicationCount();
							}
							
							qRepResponse.setPeriod_10_plus(sumCount);
						}
					} //check if the Queue has applications
					
					//add the Queue report response to output list
					queueReportResponse.add(qRepResponse);
				}	
			} //Queue loop end
		}
		
		return queueReportResponse;
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
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getQueueData'");
		
		if(queueId == null) {
			if(logger.isErrorEnabled())
				logger.error("QueueId is not provided");
			throw new Exception("QueueId is missing from the request!");
		}
		
		final Queue queue = queueService.findById(queueId);
		if(queue == null)
			throw new Exception("No Queue was found with given Id: "+ queueId);
		
		//the queries
		final String initialCountSQL = buildDynamicSQLQuery(queue, true);
		String adjusted_CountSQLClauseTemp = adjustQueueSQLForUIFilters(queue, initialCountSQL, searchFilter);
		String adjusted_CountSQLClause = adjusted_CountSQLClauseTemp.split("#!#")[0];
		String filterCase = adjusted_CountSQLClauseTemp.split("#!#")[1];
		final String initialSQL = buildDynamicSQLQuery(queue, false);
		String adjusted_SQLClauseTemp = adjustQueueSQLForUIFilters(queue, initialSQL, searchFilter);
		String adjusted_SQLClause = adjusted_SQLClauseTemp.split("#!#")[0];
		String filterCaseClause = adjusted_SQLClauseTemp.split("#!#")[1];
		if(logger.isInfoEnabled())
			logger.info("#-----filterCaseClause-----# '"+filterCaseClause);
		//tag the received user locale
		userLocale = (userLocale == null || userLocale.isEmpty()) ? "en" : userLocale;
		adjusted_CountSQLClause = adjusted_CountSQLClause.replace(QueueQueryKeywords.USER_LOCALE.getKeyword(), userLocale);
		adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.USER_LOCALE.getKeyword(), userLocale);
		
		//adjust filters for 'date' type fields
		adjusted_CountSQLClause = assignTruncForDateTimeFields(adjusted_CountSQLClause, dateFilterFields);
		adjusted_SQLClause = assignTruncForDateTimeFields(adjusted_SQLClause, dateFilterFields);
		
		//replace the SQL template keywords
		adjusted_CountSQLClause = adjusted_CountSQLClause.replace(QueueQueryKeywords.SELECT.getKeyword(), "SELECT ").
				replace(QueueQueryKeywords.FROM.getKeyword(), " FROM ").replace(QueueQueryKeywords.WHERE.getKeyword(), " WHERE ").
				replace(QueueQueryKeywords.GROUP_BY.getKeyword(), " GROUP BY ").replace(QueueQueryKeywords.ORDER_BY.getKeyword(), " ORDER BY ");
		
		adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.SELECT.getKeyword(), "SELECT ").
				replace(QueueQueryKeywords.FROM.getKeyword(), " FROM ").replace(QueueQueryKeywords.WHERE.getKeyword(), " WHERE ").
				replace(QueueQueryKeywords.GROUP_BY.getKeyword(), " GROUP BY ").replace(QueueQueryKeywords.ORDER_BY.getKeyword(), " ORDER BY ");
		
		//for boolean equal operators
		adjusted_CountSQLClause = adjusted_CountSQLClause.replace("= 'false'", "= false").replace("= 'true'", "= true");
		adjusted_SQLClause = adjusted_SQLClause.replace("= 'false'", "= false").replace("= 'true'", "= true");
		
		if(logger.isInfoEnabled())
			logger.info("#----- The SQL count clause for Queue Id: "+ queueId + " \n "
				+ "["+ adjusted_CountSQLClause +"] \n-----#");
		
		//build dynamic UI sort clause if any needed
		String sortClause = null;
		if(sortField == null)
			sortClause = buildSortClauseForQueue(queue.getSortField(), queue.getSortOrder());
		else
			sortClause = buildDynmicUISortCriteria(sortField, sortOrder);
		
		//query with appended ORDER BY clause
		if(sortClause != null && !sortClause.isEmpty())
			adjusted_SQLClause = adjusted_SQLClause.concat(" ORDER BY " + sortClause);
		
		//add the pagination clause
		pageSize = (pageSize == null) ? 10 : pageSize;
		pageNumber = (pageNumber == null) ? 0 : pageNumber;
		final Integer derivedOffset = (pageNumber * pageSize);
		
		adjusted_SQLClause = adjusted_SQLClause.concat(" LIMIT " + derivedOffset + ", " + pageSize);
		if(logger.isInfoEnabled())
			logger.info("#----- The final SQL clause for Queue Id: "+ queueId + " \n "
				+ "["+ adjusted_SQLClause +"] \n-----#");
		
		
		//creating queue response wrapper instance for passing data to UI
		QueueResponseWrapper queueResponseWrapper = new QueueResponseWrapper();
		queueResponseWrapper.setPageNumber(pageNumber);
		queueResponseWrapper.setPageSize(pageSize);
		
		/*
		 * 1. Execute count query to set the total number of records (required for paginated view)
		 */
		List<Long> applicationCount =  jdbcTemplate.query(adjusted_CountSQLClause, new PreparedStatementSetter() {
			
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				if(searchFilter != null)
				{
					int counter = 0;
					switch (filterCase)
					{
						case "DEFAULT":
							if(queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue())
								counter = 22;
							else
								counter =18;							 
						break;
						case "MANAGER":
								counter = 12;
						break;
						case "FRAUD":
							if(queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue())
								counter = 23;
							else
								counter =19;
						break;
							
					}
					for(int i=1; i <=counter; i++)
					{                
		                if(i == 6)
		                	preparedStatement.setString(i, "%" + searchFilter + "%");
		                else
		                	preparedStatement.setString(i, searchFilter + "%");
					}
				}
			}
		},
		 new QueueResultAppCountMapper());
		
		queueResponseWrapper.setTotalRecords((applicationCount != null && !applicationCount.isEmpty()) ? applicationCount.get(0) : Long.valueOf(0));
		
		/*
		 * 2. Execute the queue dynamic SQL group by query to get matching applications
		 */
		if(logger.isDebugEnabled())
			logger.debug("#----- Preparing to execute application group query for Queue Id: "+ queueId +" -----#");
		List<Integer> applicationIdList = jdbcTemplate.query(adjusted_SQLClause, new PreparedStatementSetter() {
			
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				if(searchFilter != null)
				{
					int counter = 0;
					switch (filterCaseClause)
					{
						case "DEFAULT":
							if(queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue())
								counter = 22;
							else
								counter =18;							 
						break;
						case "MANAGER":
								counter = 12;
						break;
						case "FRAUD":
							if(queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue())
								counter = 23;
							else
								counter =19;
						break;
							
					}
					for(int i=1; i <=counter; i++)
					{                
		                if(i == 6)
		                	preparedStatement.setString(i, "%" + searchFilter + "%");
		                else
		                	preparedStatement.setString(i, searchFilter + "%");
					}
				}
             }
		},
		new QueueResultAppIdMapper());
		
		/*
		 * 3. Execute the wrapper query per application Id as received via the group query
		 */
		Integer previousApplicationId = -1;
		List<QueueResponse> finalQueueResponse = null;
		
		if(applicationIdList != null && !applicationIdList.isEmpty()) {
			
			finalQueueResponse = new ArrayList<>();
			
			//call the wrapper query to get full data per application
			for(Integer applicationId : applicationIdList) {
				String queueWrapperQuery = QUEUE_WRAPPER_QUERY.replace(QueueQueryKeywords.SELECT.getKeyword(), "SELECT ").
						replace(QueueQueryKeywords.FROM.getKeyword(), " FROM ").replace(QueueQueryKeywords.WHERE.getKeyword(), " WHERE ").
						replace(QueueQueryKeywords.GROUP_BY.getKeyword(), " GROUP BY ").replace(QueueQueryKeywords.ORDER_BY.getKeyword(), " ORDER BY ").
						replace(QueueQueryKeywords.WRAPPER_QUERY_APPLICATION_ID.getKeyword(), " ? ");
				if(logger.isInfoEnabled())
				{
					logger.info("#---- Inside method 'getQueueData'. Final SQL wrapper query for applicationId ["+ applicationId +"] on Queue: ["+ queue.getName() +"] ###");
					logger.info("[\n#\n#\n"+ queueWrapperQuery +"\n#\n#\n]");
				}
					List<QueueResponse> applicationDataResponse = jdbcTemplate.query(queueWrapperQuery, new PreparedStatementSetter() {
						
						public void setValues(PreparedStatement preparedStatement) throws SQLException {
			                preparedStatement.setString(1, applicationId.toString());
			             }
					},
					new QueueResultMapper());
									
				if(logger.isDebugEnabled())
					logger.debug("#----- Per application data query for Queue Id: "+ queueId +" executed successfully -----#");
				
				//for the current application's received dataset
				if(applicationDataResponse != null && !applicationDataResponse.isEmpty()) {
					for(QueueResponse rowData : applicationDataResponse) {
						rowData.setToDisplay(!previousApplicationId.equals(rowData.getApplicationId()));
						finalQueueResponse.add(rowData);
						previousApplicationId = rowData.getApplicationId();
					}  //for each rows in context of the current application
				}
				
				applicationDataResponse = null;
				
			}  //loop for per-application as a result of group query
		}
		
		finalQueueResponse = commonUtils.setNullToEmptyForStringProperties(QueueResponse.class, finalQueueResponse);
		queueResponseWrapper.setQueueData(finalQueueResponse);
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getQueueData'. Query processing for Queue Id: "+ 
				queueId +" completed. Total applications in queue: " + queueResponseWrapper.getTotalRecords());
		
		return queueResponseWrapper;
	}
	
	/**
	 * Returns the count of applications in the specified queue as per the filters
	 * @param queue
	 * @param userLocale
	 * @param dateFilterFields
	 * @return
	 * @throws Exception
	 */
	public Long getDynamicQueryResultCount(Queue queue, String userLocale, String dateFilterFields) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getDynamicQueryResultCount'");
		
		Long recordCount = Long.valueOf(0);
		if(queue == null || queue.getJsonFilterString() == null || queue.getJsonFilterString().isEmpty())
			throw new Exception("Either the Queue data or JSON filter condition is missing");
		
		//if the queue is not yet added/the SQL query is not present as part of the Queue info
		String adjusted_CountSQLClause = null;
		String initialQueueSQL = buildDynamicSQLQuery(queue, true);
		String adjusted_CountSQLClauseTemp = adjustQueueSQLForUIFilters(queue, initialQueueSQL, null);
		adjusted_CountSQLClause = adjusted_CountSQLClauseTemp.split("#!#")[0];
		String filterCase = adjusted_CountSQLClauseTemp.split("#!#")[1];
		
		//tag the received user locale
		userLocale = (userLocale == null || userLocale.isEmpty()) ? "en" : userLocale;
		adjusted_CountSQLClause = adjusted_CountSQLClause.replace(QueueQueryKeywords.USER_LOCALE.getKeyword(), userLocale);
		
		//adjust filters for 'date' type fields
		adjusted_CountSQLClause = assignTruncForDateTimeFields(adjusted_CountSQLClause, dateFilterFields);
		
		//replace the SQL template keywords
		adjusted_CountSQLClause = adjusted_CountSQLClause.replace(QueueQueryKeywords.SELECT.getKeyword(), "SELECT ").
				replace(QueueQueryKeywords.FROM.getKeyword(), " FROM ").replace(QueueQueryKeywords.WHERE.getKeyword(), " WHERE ").
				replace(QueueQueryKeywords.GROUP_BY.getKeyword(), " GROUP BY ").replace(QueueQueryKeywords.ORDER_BY.getKeyword(), " ORDER BY ");
		
		//for boolean equal operators
		adjusted_CountSQLClause = adjusted_CountSQLClause.replace("= 'false'", "= false").replace("= 'true'", "= true");
		if(logger.isInfoEnabled())
			logger.info("#----- The SQL count clause for this Queue: \n "
				+ "["+ adjusted_CountSQLClause +"] \n-----#");
		
		/*
		 * 1. Execute count query to set the total number of records (required for paginated view)
		 */
		if(logger.isDebugEnabled())
			logger.debug("#----- Preparing to execute count query for Queue -----#");
			List<Long> applicationCount = jdbcTemplate.query(adjusted_CountSQLClause, new PreparedStatementSetter() {
			
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				}
		},
		 new QueueResultAppCountMapper());
			
		if(logger.isInfoEnabled())
			logger.info("#----- Count query for Queue was executed successfully -----#");
		recordCount = ((applicationCount != null && !applicationCount.isEmpty()) ? new Long(applicationCount.get(0)) : new Long(0));

		
		return recordCount;
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
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getApplicationsInQueue'");
		
		if(queueId == null) {
			if(logger.isErrorEnabled())
				logger.error("QueueId is not provided");
			throw new Exception("QueueId is missing from the request!");
		}
		
		final Queue queue = queueService.findById(queueId);
		if(queue == null)
			throw new Exception("No Queue was found with given Id: "+ queueId);
		
		//the initial query
		final String initialSQL = buildDynamicSQLQuery(queue, false);
		String adjusted_SQLClause = adjustQueueSQLForUIFilters(queue, initialSQL, null);
		
		//tag the received user locale
		userLocale = (userLocale == null || userLocale.isEmpty()) ? "en" : userLocale;
		adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.USER_LOCALE.getKeyword(), userLocale);
		
		//adjust filters for 'date' type fields
		adjusted_SQLClause = assignTruncForDateTimeFields(adjusted_SQLClause, dateFilterFields);
		
		//replace the SQL template keywords
		adjusted_SQLClause = adjusted_SQLClause.replace(QueueQueryKeywords.SELECT.getKeyword(), "SELECT ").
				replace(QueueQueryKeywords.FROM.getKeyword(), " FROM ").replace(QueueQueryKeywords.WHERE.getKeyword(), " WHERE ").
				replace(QueueQueryKeywords.GROUP_BY.getKeyword(), " GROUP BY ").replace(QueueQueryKeywords.ORDER_BY.getKeyword(), " ORDER BY ");
		
		//for boolean equal operators
		adjusted_SQLClause = adjusted_SQLClause.replace("= 'false'", "= false").replace("= 'true'", "= true");
		
		//adding the ORDER BY clause (from QUEUE's definition)
		String sortClause = queue.getSortField() + " " +
				((queue.getSortOrder() == null || queue.getSortOrder().isEmpty()) ? "ASC" : queue.getSortOrder().toUpperCase());

		//query with appended ORDER BY clause
		if(sortClause != null && !sortClause.isEmpty())
			adjusted_SQLClause = adjusted_SQLClause.concat(" ORDER BY " + sortClause);
		
		//add the pagination clause
		pageSize = (pageSize == null) ? 10 : pageSize;
		pageNumber = (pageNumber == null) ? 0 : pageNumber;
		final Integer derivedOffset = (pageNumber * pageSize);
		
		adjusted_SQLClause = adjusted_SQLClause.concat(" LIMIT " + derivedOffset + ", " + pageSize);
		if(logger.isInfoEnabled())
			logger.info("#----- The final SQL clause for Queue Id: "+ queueId + " \n "
				+ "["+ adjusted_SQLClause +"] \n-----#");
		
		/*
		 * 1. Execute the queue dynamic SQL group by query to get matching applications
		 */
		List<Integer> applicationIds = jdbcTemplate.query(adjusted_SQLClause, new PreparedStatementSetter() {
			
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				
             }
		},
		new QueueResultAppIdMapper());
		
		if(logger.isInfoEnabled())
			logger.info("#----- Application group query for Queue Id: "+ queueId +" executed successfully -----#");
		
		return applicationIds;
	}
	
    /**
	 * Fetches the list of all Queues from the system that the current logged in USER has access to
	 * @return
	 * @throws Exception
	 */
	public List<Queue> getQueuesByLoggedinUser() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getQueuesByLoggedinUser'");
		
		List<Queue> userQueues = new ArrayList<>();
		final User loggedInUser = userManagementHandler.getLoggedInUser();
		
		//if User exists & is active
		if(loggedInUser != null && loggedInUser.isActive()) {
			List<QueryGetQueuesByUserIdResponse> queryResult = telusAgentUIDBQueryExecutorService.
					executeQuery_GetQueuesByUserId(loggedInUser.getId(), PageRequest.of(0, Integer.MAX_VALUE)).toList();
			
			if(queryResult != null && !queryResult.isEmpty()) {
				for(QueryGetQueuesByUserIdResponse queueData : queryResult) {
					Queue queue = queueService.findById(queueData.getQueueId().intValue());
					
					//if Queue exists & is active
					if(queue != null && queue.getIsActive() != null && queue.getIsActive().booleanValue())
						userQueues.add(queue);
				} //fetch Queue information per queue
			}
		}
		
		return userQueues;
	}
	
	/**
	 * Builds a dynamic SQL where clause from the Jquery's condition builder JSON
	 * configuration
	 * 
	 * @param conditionBuilderJSON
	 * @return
	 * @throws Exception
	 */
	public String prepareSQLWhereClauseFromJSONFilterString(String conditionBuilderJSON) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'prepareSQLWhereClauseFromJSONFilterString'");

		if (conditionBuilderJSON == null || conditionBuilderJSON.isEmpty()) {
			return "";
		}

		SqlQueryBuilderFactory sqlQueryBuilderFactory = new SqlQueryBuilderFactory();
		SqlBuilder sqlBuilder = sqlQueryBuilderFactory.builder();
		String filter = conditionBuilderJSON.replaceAll("&quot;", "\"");
		SqlQueryResult sqlQueryResult = sqlBuilder.build(prepareQuerybuilderFormattedJson(filter));

		String dynamicWhereClause = null;
		dynamicWhereClause = sqlQueryResult.getQuery().replace("?", "'%s'");
		dynamicWhereClause = String.format(dynamicWhereClause, sqlQueryResult.getParams().toArray());
		if(logger.isInfoEnabled())
			logger.info("Dyamic where clause prepared from SQLQueryBuilder is: [" + dynamicWhereClause + "]");
		return dynamicWhereClause;
	}

	/**
	 * Builds a dynamic SQL query for report generation of a particular queue
	 * @param queue
	 * The Queue instance
	 * @param loggedInUser
	 * The current logged in User
	 * @return
	 * @throws Exception
	 */
	private String buildReportQueryForQueue(Queue queue, User loggedInUser) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildReportQueryForQueue'");
		
		String sqlSelectClause = "";
		if (queue == null) {
			if(logger.isErrorEnabled())
				logger.error("Unable to build SQL select clause. Queue data is missing!");
			throw new Exception("Unable to build SQL select clause. Queue data is missing!");
		}

		String personalQueueWhereFilter = "";
		String dynamicWhereFilter = "";
		
		//check if QUEUE is a personal queue
		if(queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue()) {
			QueryGetDomainValueByIdResponse personalQueueFilterField = 
					domainValueServiceBS.getDomainValueByDomainValueId(queue.getPersonalQueueField(), null);
			if(personalQueueFilterField == null);
			else {
				final String personalQueueFilter = personalQueueFilterField.getCode();
				switch (personalQueueFilter) {
					case "APP_CREATED_BY": 
						personalQueueWhereFilter = " AND APPLICATION_DETAILS.createdBy = " + loggedInUser.getId();
					break;
					case "APP_LOCKED_BY":
						personalQueueWhereFilter = " AND ApplicationLock.lockedBy = " + loggedInUser.getId();
					break;
					case "APP_UPDATED_BY":
						personalQueueWhereFilter = " AND APPLICATION_DETAILS.updatedBy = " + loggedInUser.getId();
					break;
					case "CUR_OWNER":
						personalQueueWhereFilter = " AND APPLICATION_DETAILS.owningOfficer = '" + loggedInUser.getUserId() + "'";  //quotes enclosed -> as the DB field is of type string
					break;
				}
			}
		}
		
		//get the queue dynamic filters
		dynamicWhereFilter = prepareSQLWhereClauseFromJSONFilterString(queue.getJsonFilterString());
		sqlSelectClause = QUEUE_REPORT_QUERY.
				replace(QueueQueryKeywords.QUEUE_WHERE_FILTER_CONDITION.getKeyword(), (" AND " + dynamicWhereFilter)).
				replace(QueueQueryKeywords.PERSONAL_QUEUE_WHERE_FILTER_CONDITION.getKeyword(), personalQueueWhereFilter);
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildReportQueryForQueue'. Generated dynamic SQL query is: ["+ sqlSelectClause +"]");
		return sqlSelectClause;
	}
	
	/**
	 * Builds a dynamic SQL query for a particular queue
	 * @param queue
	 * The Queue instance
	 * @param isCountQuery
	 * Flag to indicate whether a count query needs to be generated (if set to <b>TRUE</b>)
	 * @return
	 * @throws Exception
	 */
	private String buildDynamicSQLQuery(Queue queue, boolean isCountQuery) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildDynamicSQLQuery'");
		
		String sqlSelectClause = "";
		if (queue == null) {
			if(logger.isErrorEnabled())
				logger.error("Unable to build SQL select clause. Queue data is missing!");
			throw new Exception("Unable to build SQL select clause. Queue data is missing!");
		}

		String personalQueueWhereFilter = "";
		String dynamicWhereFilter = "";
		
		//check if QUEUE is a personal queue
		if(queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue()) {
			QueryGetDomainValueByIdResponse personalQueueFilterField = 
					domainValueServiceBS.getDomainValueByDomainValueId(queue.getPersonalQueueField(), null);
			if(personalQueueFilterField == null);
			else {
				final String personalQueueFilter = personalQueueFilterField.getCode();
				switch (personalQueueFilter) {
					case "APP_CREATED_BY": 
						personalQueueWhereFilter = " AND APPLICATION_DETAILS.createdBy = " + userManagementHandler.getLoggedInUser().getId();
					break;
					case "APP_LOCKED_BY":
						personalQueueWhereFilter = " AND ApplicationLock.lockedBy = " + userManagementHandler.getLoggedInUser().getId();
					break;
					case "APP_UPDATED_BY":
						personalQueueWhereFilter = " AND APPLICATION_DETAILS.updatedBy = " + userManagementHandler.getLoggedInUser().getId();
					break;
					case "CUR_OWNER":
						personalQueueWhereFilter = " AND APPLICATION_DETAILS.owningOfficer = '" + userManagementHandler.getLoggedInUser().getUserId() + "'";  //quotes enclosed -> as the DB field is of type string
					break;
				}
			}
		}
		
		//get the queue dynamic filters
		dynamicWhereFilter = prepareSQLWhereClauseFromJSONFilterString(queue.getJsonFilterString());
		sqlSelectClause = QUEUE_DYNAMIC_FILTER_QUERY.
				replace(QueueQueryKeywords.QUEUE_WHERE_FILTER_CONDITION.getKeyword(), (" AND " + dynamicWhereFilter)).
				replace(QueueQueryKeywords.PERSONAL_QUEUE_WHERE_FILTER_CONDITION.getKeyword(), personalQueueWhereFilter);
		
		//check which query to generate
		if(isCountQuery) {
			sqlSelectClause = QueueQueryKeywords.SELECT.getKeyword() + " COUNT(*) AS applicationCount " + QueueQueryKeywords.FROM.getKeyword()
				+ "(" + sqlSelectClause + ") AS GroupSubQuery";
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildDynamicSQLQuery'. Generated dynamic SQL query is: ["+ sqlSelectClause +"]");
		return sqlSelectClause;
	}

	/**
	 * Adjusts the dynamic SQL clause for any queue with the necessary UI search filters as per the Queue's
	 * search page
	 * @param queue
	 * The Queue instance
	 * @param queueSQLQuery
	 * The initial SQL query for this Queue
	 * @param searchFilter
	 * UI search filters
	 * @return
	 * @throws Exception
	 */
	private String adjustQueueSQLForUIFilters(Queue queue, String queueSQLQuery, String searchFilter) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'adjustQueueSQLForUIFilters'");
		
		if(queue == null)
			throw new Exception("Queue data is missing!");
		if (queueSQLQuery == null || queueSQLQuery.isEmpty())
			throw new Exception("Queue SQL clause is missing!");
		
		String filterCase = "XXX";
		String adjustedSQL = "";
		adjustedSQL = queueSQLQuery;
		final int groupByIndex = adjustedSQL.indexOf(QueueQueryKeywords.GROUP_BY.getKeyword());
		
		if(searchFilter == null || searchFilter.isEmpty());
		else {
			String UI_whereFilters = "";
			QueryGetDomainValueByIdResponse queueSearchPageDV = 
					domainValueServiceBS.getDomainValueByDomainValueId(queue.getQueueResultPage(), null);
		
			if(queueSearchPageDV != null) {
				filterCase = queueSearchPageDV.getCode();
				//build UI level where filters based on queue search page
				switch (queueSearchPageDV.getCode()) {
					case "DEFAULT":
						UI_whereFilters = "(APPLICATION.applicationNumber LIKE ? OR "
								+ "AppStatusDVDsc.description LIKE ? OR "
								+ "AppChannelDVDsc.description LIKE ? OR "
								+ "PERSON.firstName LIKE ? OR "
								+ "PERSON.lastName LIKE ? OR "
								+ "DATE_FORMAT(PERSON.dateOfBirth, \"%Y%m%d\") LIKE ? OR "	//TD Spec
								+ "ADDRESS.postalCode LIKE ? OR "	//TD Spec
								+ "PHONE.phoneNumber LIKE ? OR "
								+ "PERSON.motherMaidenName LIKE ? OR "	//TD spec
								+ "Employment.employerName LIKE ? OR "	//TD spec
								+ "EmpJobCodeDVDsc.description LIKE ? OR "	//TD spec
								+ "EMAIL.email LIKE ? OR "
								+ "AppCreatedUser.firstName LIKE ? OR "	//TD spec
								+ "AppCreatedUser.lastName LIKE ? OR "	//TD spec
								+ "AppUpdatedUser.firstName LIKE ? OR "
								+ "AppUpdatedUser.lastName LIKE ? OR "
								+ "AppLockUser.firstName LIKE ? OR "
								+ "AppLockUser.lastName LIKE ? "
								//TD Spec
								+ ((queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue()) ?
										("OR ProvinceDVDsc.description LIKE ? OR "
										+ "APPLICATION_DETAILS.onHoldReason LIKE ? OR "
										+ "DATEDIFF(CURDATE(), DATE(APPLICATION_DETAILS.createdOn)) LIKE ? OR "
										+ "DATEDIFF(CURDATE(), DATE(APPLICATION_DETAILS.ownedOn)) LIKE ?)") :
										")");
						break;
					case "MANAGER":
						UI_whereFilters = "(APPLICATION.applicationNumber LIKE ? OR "
								+ "AppStatusDVDsc.description LIKE ? OR "
								+ "CRTypeDVDsc.description LIKE ? OR "
								+ "AppChannelDVDsc.description LIKE ? OR "
								+ "PERSON.firstName LIKE ? OR "
								+ "PERSON.lastName LIKE ? OR "
								+ "PHONE.phoneNumber LIKE ? OR "
								+ "EMAIL.email LIKE ? OR "	
								+ "AppUpdatedUser.firstName LIKE ? OR "
								+ "AppUpdatedUser.lastName LIKE ? OR "
								+ "AppLockUser.firstName LIKE ? OR "
								+ "AppLockUser.lastName LIKE ?)";
						break;
					case "FRAUD":
						UI_whereFilters = "(APPLICATION.applicationNumber LIKE ? OR "
								+ "AppStatusDVDsc.description LIKE ? OR "
								+ "AppChannelDVDsc.description LIKE ? OR "
								+ "PERSON.firstName LIKE ? OR "
								+ "PERSON.lastName LIKE ? OR "
								+ "DATE_FORMAT(PERSON.dateOfBirth, \"%Y%m%d\") LIKE ? OR "	//TD Spec
								+ "ADDRESS.postalCode LIKE ? OR "	//TD Spec
								+ "PHONE.phoneNumber LIKE ? OR "
								+ "PERSON.motherMaidenName LIKE ? OR "	//TD spec
								+ "Employment.employerName LIKE ? OR "	//TD spec
								+ "EmpJobCodeDVDsc.description LIKE ? OR "	//TD spec
								+ "EMAIL.email LIKE ? OR "
								+ "APPLICATION_DETAILS.iPAddress LIKE ? OR "
								+ "AppCreatedUser.firstName LIKE ? OR "	//TD spec
								+ "AppCreatedUser.lastName LIKE ? OR "	//TD spec
								+ "AppUpdatedUser.firstName LIKE ? OR "
								+ "AppUpdatedUser.lastName LIKE ? OR "
								+ "AppLockUser.firstName LIKE ? OR "
								+ "AppLockUser.lastName LIKE ?' "
								//TD Spec
								+ ((queue.getIsPersonalQueue() != null && queue.getIsPersonalQueue().booleanValue()) ?
										("OR ProvinceDVDsc.description LIKE ? OR "
										+ "APPLICATION_DETAILS.onHoldReason LIKE ? OR "
										+ "DATEDIFF(CURDATE(), DATE(APPLICATION_DETAILS.createdOn)) LIKE ? OR "
										+ "DATEDIFF(CURDATE(), DATE(APPLICATION_DETAILS.ownedOn)) LIKE ?)") :
										")");
						break;
				}
				
				//if where condition is already present
				if(adjustedSQL.contains(QueueQueryKeywords.WHERE.getKeyword())) {
					adjustedSQL = adjustedSQL.substring(0, groupByIndex).concat(" AND " + UI_whereFilters + " ")
							.concat(adjustedSQL.substring(groupByIndex));
				}
				else {
					adjustedSQL = adjustedSQL.substring(0, groupByIndex).concat(" " + QueueQueryKeywords.WHERE.getKeyword() + " " + UI_whereFilters + " ")
							.concat(adjustedSQL.substring(groupByIndex));
				}
			} //if the queue search result page is available
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'adjustQueueSQLForUIFilters'. The adjusted SQL after applying the UI level search filters: ["+ adjustedSQL +"]");
		return adjustedSQL+"#!#"+filterCase;
	}
	
	/**
	 * Builds a dynamic sort criteria to be used in the Queue SQL query as per the UI selected sortable columns
	 * @param sortFieldLabel
	 * @param sortOrder
	 * @return
	 */
	private String buildDynmicUISortCriteria(String sortFieldLabel, String sortOrder) {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildDynmicUISortCriteria'");
	    
		String sortCriteria = "";
		
		if(sortFieldLabel == null)
			return null; //will use Queue's default sort criteria
		else {
			if(sortOrder == null || sortOrder.isEmpty())
				sortOrder = "ASC";
			
			switch (sortFieldLabel) {
				case "applicationId": sortCriteria = " APPLICATION.id " + sortOrder;
					break;
				case "applicationNumber": sortCriteria = " APPLICATION.applicationNumber " + sortOrder;
					break;
				case "applicationStatus": sortCriteria = " AppStatusDVDsc.description " + sortOrder;
					break;
				case "applicationChannel": sortCriteria = " AppChannelDVDsc.description " + sortOrder;
					break;
				case "caseReviewType": sortCriteria = " CRTypeDVDsc.description " + sortOrder;
					break;
				case "caseReviewLevel": sortCriteria = " CaseReview.reviewLevel " + sortOrder;
					break;
				case "customerName": sortCriteria = " PERSON.firstName " + sortOrder + ", PERSON.lastName "  + sortOrder;
					break;
				//TD spec
				case "dateOfBirth": sortCriteria = " PERSON.dateOfBirth " + sortOrder;
					break;
				//TD spec
				case "postalCode": sortCriteria = " ADDRESS.postalCode " + sortOrder;
					break;
				//TD spec
				case "province": sortCriteria = " ProvinceDVDsc.description " + sortOrder;
					break;
				case "phoneNumber": sortCriteria = " PHONE.phoneNumber " + sortOrder;
					break;
				//TD spec
				case "mothersMaidenName": sortCriteria = " PERSON.motherMaidenName " + sortOrder;
					break;
				//TD spec
				case "employerName": sortCriteria = " Employment.employerName " + sortOrder;
					break;
				//TD spec
				case "employmentJobcode": sortCriteria = " EmpJobCodeDVDsc.description " + sortOrder;
					break;
				case "emailId": sortCriteria = " EMAIL.email " + sortOrder;
					break;
				case "ipAddress": sortCriteria = " APPLICATION_DETAILS.iPAddress " + sortOrder;
					break;
				case "applicationCreatedOn": sortCriteria = " APPLICATION_DETAILS.createdOn " + sortOrder;
					break;
				//TD spec
				case "applicationUpdatedOn": sortCriteria = " APPLICATION_DETAILS.updatedOn " + sortOrder;
					break;
				//TD spec
				case "applicationCreatedBy": sortCriteria = " AppCreatedUser.firstName " + sortOrder + ", AppCreatedUser.lastName " + sortOrder;
					break;
				case "applicationUpdatedBy": sortCriteria = " AppUpdatedUser.firstName " + sortOrder + ", AppUpdatedUser.lastName " + sortOrder;
					break;
				//TD Spec
				case "appHoldUntil": sortCriteria = " APPLICATION_DETAILS.onHoldUntilDate " + sortOrder;
					break;
				//TD Spec
				case "appHoldReason": sortCriteria = " APPLICATION_DETAILS.onHoldReason " + sortOrder;
					break;
				case "applicationLockedBy": sortCriteria = " AppLockUser.firstName " + sortOrder + ", AppLockUser.lastName " + sortOrder;
					break;
			}
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildDynmicUISortCriteria'. The generated ORDER BY clause: ["+ sortCriteria +"]");
		return sortCriteria;
	}
	
	/**
	 * Builds the sort criteria for a particular Queue
	 * @param sortInformation
	 * The sort information for a Queue (either stored as JSON or as a trivial single field)
	 * @param sortOrder
	 * The direction for sort (optional & only needed if trivial way of sort information is stored)
	 * @return
	 * The order by clause
	 * @throws Exception
	 */
	private String buildSortClauseForQueue(String sortInformation, String sortOrder) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildSortClauseForQueue'");
	    
		String orderClause = "";
		
		if(sortInformation != null) {
			boolean isNonJSONSortInfo = false;  //keeping this so as to make it backward compatible with the previous implementation
			QueueSortMetaModel[] sortMetaInfo = null;
			final String joinOnlyClausePart = QUEUE_DYNAMIC_FILTER_QUERY.substring(
					(QUEUE_DYNAMIC_FILTER_QUERY.indexOf(QueueQueryKeywords.FROM.getKeyword()) + 5), 
					QUEUE_DYNAMIC_FILTER_QUERY.indexOf(QueueQueryKeywords.WHERE.getKeyword()));
			
			//checking for the type of sort information stored in DB
			try {
				sortMetaInfo =  this.mapper.readValue(sortInformation.replaceAll("&quot;", "\""), QueueSortMetaModel[].class);
				isNonJSONSortInfo = false;
			}
			catch (JsonParseException e) {
				if(logger.isErrorEnabled())
					logger.error("Sort falling back to older implementation, considering only single field");
				isNonJSONSortInfo = true;
				orderClause = sortInformation + " " +
						((sortOrder == null || sortOrder.isEmpty()) ? "ASC" : sortOrder.toUpperCase());
			}
			
			//processing continue if JSON sort information present
			if(!isNonJSONSortInfo && sortMetaInfo != null && sortMetaInfo.length > 0) {
				//sorting based on rank-order such that order selected on UI matches order in clause
				List<QueueSortMetaModel> sortMetaInfoAsList = Arrays.asList(sortMetaInfo);
				sortMetaInfoAsList.sort(new Comparator<QueueSortMetaModel>() {

					@Override
					public int compare(QueueSortMetaModel o1, QueueSortMetaModel o2) {
						return o1.getRankOrder() - o2.getRankOrder();
					}
				});
				
				for(QueueSortMetaModel sortMetaField : sortMetaInfoAsList) {
					final String DBtableName = sortMetaField.getFieldId().substring(0, sortMetaField.getFieldId().lastIndexOf("."));
					final String columnName = sortMetaField.getFieldId().substring(sortMetaField.getFieldId().lastIndexOf(".") + 1);
					
					final Class<?> parentClass = commonUtils.isForeignColumnReference(DBtableName, columnName, "com.fico.dmp.core", Entity.class);
							
					if(parentClass != null) {
						final String className = parentClass.getSimpleName();
						final String equivalentDBTableName = AnnotationUtils.findAnnotation(parentClass, Table.class).name().
								replace("`", "").replace("`", "");
						String regexPattern = ""; Matcher matcher = null;
						
						switch (className) {
						case "DomainValue":
							if(logger.isInfoEnabled())
								logger.info("updating sort clause for DomainValue table");
							//checking for possible variations on the SQL clause
							regexPattern = "(LEFT|RIGHT|INNER|OUTER) JOIN "+ equivalentDBTableName +" AS \\S+ ON "+ sortMetaField.getFieldId() +" = \\S+";
							matcher = Pattern.compile(regexPattern).matcher(joinOnlyClausePart);
							if(matcher.find()) {
								final String matchedRegion = joinOnlyClausePart.substring(matcher.start(), matcher.end());
								final String tableAlias = matchedRegion.substring(matchedRegion.indexOf("AS") + 2,
										matchedRegion.indexOf("ON")).trim();
								orderClause = orderClause.equals("") ? (tableAlias + "Dsc.description " + sortMetaField.getDirection().toUpperCase())
										: (orderClause + ", " + tableAlias + "Dsc.description " + sortMetaField.getDirection().toUpperCase());
								continue;
							}
							
							regexPattern = "(LEFT|RIGHT|INNER|OUTER) JOIN "+ equivalentDBTableName +" AS \\S+ ON \\S+ = "+ sortMetaField.getFieldId();
							matcher = Pattern.compile(regexPattern).matcher(joinOnlyClausePart);
							if(matcher.find()) {
								final String matchedRegion = joinOnlyClausePart.substring(matcher.start(), matcher.end());
								final String tableAlias = matchedRegion.substring(matchedRegion.indexOf("AS") + 2,
										matchedRegion.indexOf("ON")).trim();
								orderClause = orderClause.equals("") ? (tableAlias + "Dsc.description " + sortMetaField.getDirection().toUpperCase())
										: (orderClause + ", " + tableAlias + "Dsc.description " + sortMetaField.getDirection().toUpperCase());
								continue;
							}
							
							else {
								orderClause = orderClause.equals("") ? (sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase())
										: (orderClause + ", " + sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase());
							}
							
							break;
						case "Product":
							//checking for possible variations on the SQL clause
							regexPattern = "(LEFT|RIGHT|INNER|OUTER) JOIN "+ equivalentDBTableName +" AS \\S+ ON "+ sortMetaField.getFieldId() +" = \\S+";
							matcher = Pattern.compile(regexPattern).matcher(joinOnlyClausePart);
							if(matcher.find()) {
								final String matchedRegion = joinOnlyClausePart.substring(matcher.start(), matcher.end());
								final String tableAlias = matchedRegion.substring(matchedRegion.indexOf("AS") + 2,
										matchedRegion.indexOf("ON")).trim();
								orderClause = orderClause.equals("") ? (tableAlias + ".name " + sortMetaField.getDirection().toUpperCase())
										: (orderClause + ", " + tableAlias + ".name " + sortMetaField.getDirection().toUpperCase());
								continue;
							}
							
							regexPattern = "(LEFT|RIGHT|INNER|OUTER) JOIN "+ equivalentDBTableName +" AS \\S+ ON \\S+ = "+ sortMetaField.getFieldId();
							matcher = Pattern.compile(regexPattern).matcher(joinOnlyClausePart);
							if(matcher.find()) {
								final String matchedRegion = joinOnlyClausePart.substring(matcher.start(), matcher.end());
								final String tableAlias = matchedRegion.substring(matchedRegion.indexOf("AS") + 2,
										matchedRegion.indexOf("ON")).trim();
								orderClause = orderClause.equals("") ? (tableAlias + ".name " + sortMetaField.getDirection().toUpperCase())
										: (orderClause + ", " + tableAlias + ".name " + sortMetaField.getDirection().toUpperCase());
								continue;
							}
							
							else {
								orderClause = orderClause.equals("") ? (sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase())
										: (orderClause + ", " + sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase());
							}
							
							break;
						case "User":
							//checking for possible variations on the SQL clause
							regexPattern = "(LEFT|RIGHT|INNER|OUTER) JOIN "+ equivalentDBTableName +" AS \\S+ ON "+ sortMetaField.getFieldId() +" = \\S+";
							matcher = Pattern.compile(regexPattern).matcher(joinOnlyClausePart);
							if(matcher.find()) {
								final String matchedRegion = joinOnlyClausePart.substring(matcher.start(), matcher.end());
								final String tableAlias = matchedRegion.substring(matchedRegion.indexOf("AS") + 2,
										matchedRegion.indexOf("ON")).trim();
								orderClause = orderClause.equals("") ? (tableAlias + ".firstName " + sortMetaField.getDirection().toUpperCase() + ", " +
												tableAlias + ".lastName " + sortMetaField.getDirection().toUpperCase())
												: (orderClause + ", " + tableAlias + ".firstName " + sortMetaField.getDirection().toUpperCase() +
													tableAlias + ".lastName " + sortMetaField.getDirection().toUpperCase());
								continue;
							}
							
							regexPattern = "(LEFT|RIGHT|INNER|OUTER) JOIN "+ equivalentDBTableName +" AS \\S+ ON \\S+ = "+ sortMetaField.getFieldId();
							matcher = Pattern.compile(regexPattern).matcher(joinOnlyClausePart);
							if(matcher.find()) {
								final String matchedRegion = joinOnlyClausePart.substring(matcher.start(), matcher.end());
								final String tableAlias = matchedRegion.substring(matchedRegion.indexOf("AS") + 2,
										matchedRegion.indexOf("ON")).trim();
								orderClause = orderClause.equals("") ? (tableAlias + ".firstName " + sortMetaField.getDirection().toUpperCase() + ", " +
												tableAlias + ".lastName " + sortMetaField.getDirection().toUpperCase())
												: (orderClause + ", " + tableAlias + ".firstName " + sortMetaField.getDirection().toUpperCase() +
													tableAlias + ".lastName " + sortMetaField.getDirection().toUpperCase());
								continue;
							}
							
							else {
								orderClause = orderClause.equals("") ? (sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase())
										: (orderClause + ", " + sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase());
							}
							
							break;
						}
					}
					else {
						orderClause = orderClause.equals("") ? (sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase())
								: (orderClause + ", " + sortMetaField.getFieldId() + " " + sortMetaField.getDirection().toUpperCase());
					}
					
				} //iterate over each sort fields
			}
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildSortClauseForQueue'. The generated ORDER BY clause: ["+ orderClause +"]");
		return orderClause;
	}
	
	/**
	 * This method accepts the list of filter fields <b>(that are of type 'date')</b> that are part of the Queue filter's configuration and
	 * updates the SQL WHERE clause such that the date filters are always using a truncated match (i.e., not using the time part in the match)
	 * @param queue_SQLStatement
	 * The Queue's dynamic SQL statement
	 * @param DB_dateFields
	 * Comma separated list of filter fields that are of type 'date'
	 * @return
	 * The adjusted SQL clause to run with modified date filters matching
	 * @throws Exception
	 */
	private String assignTruncForDateTimeFields(String queue_SQLStatement, String DB_dateFields) throws Exception {
		if(DB_dateFields == null || DB_dateFields.isEmpty());
		else {
			final List<String> db_dateTimeFields = Arrays.asList(DB_dateFields.split(","));
			final String SQLClause_before_WHERE = queue_SQLStatement.substring(0, queue_SQLStatement.indexOf(QueueQueryKeywords.WHERE.getKeyword()));
			final String SQL_WHERE_Clause = queue_SQLStatement.substring(queue_SQLStatement.indexOf(QueueQueryKeywords.WHERE.getKeyword()), queue_SQLStatement.indexOf(QueueQueryKeywords.GROUP_BY.getKeyword()));
			final String SQLClause_after_WHERE = queue_SQLStatement.substring(queue_SQLStatement.indexOf(QueueQueryKeywords.GROUP_BY.getKeyword()));
			
			//adjust each date time fields to use 'DATE()'
			String adjustedWhereClause = SQL_WHERE_Clause;
			for(String DBFieldId : db_dateTimeFields) {
				adjustedWhereClause = adjustedWhereClause.replace(DBFieldId, "DATE("+ DBFieldId +")");
			}
			
			queue_SQLStatement = SQLClause_before_WHERE + " " + adjustedWhereClause + " " + SQLClause_after_WHERE;
		}
		
		return queue_SQLStatement;
	}
	
	/**
	 * Method that converts FAWB prefab's condition builder JSON to SQLQueryBuilder
	 * compatible rules JSON
	 * 
	 * @param json
	 * @return
	 * @throws JsonProcessingException
	 */
	private String prepareQuerybuilderFormattedJson(String json) throws JsonProcessingException {
		FilterGroup filterGroupRule = mapper.readValue(json, FilterGroup.class);
		ObjectNode rootQueryNode = mapper.createObjectNode();

		// setting root level node
		rootQueryNode.put("condition", filterGroupRule.getCondition());
		rootQueryNode.set("rules", mapFilterRules(filterGroupRule.getFilterCriteria()));

		if (!filterGroupRule.getFilterGroup().isEmpty()) {
			createFilterGroupJsonString(filterGroupRule.getFilterGroup(), rootQueryNode);
		}
		if(logger.isDebugEnabled())
			logger.debug("Builder compatible JSON: \n"
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootQueryNode));
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootQueryNode);

	}

	/**
	 * Method converting simple rule filters to corresponding rules object supported
	 * by SQLQueryBuilder
	 * 
	 * @param filterCriteriaList
	 * @return
	 */
	private ArrayNode mapFilterRules(List<FilterCriteria> filterCriteriaList) {
		ObjectNode rule;
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode rules = mapper.createArrayNode();
		if (!filterCriteriaList.isEmpty()) {
			for (FilterCriteria filterCriteria : filterCriteriaList) {
				rule = mapper.createObjectNode();
				rule.put("id", filterCriteria.getId());
				rule.put("field", filterCriteria.getField());
				rule.put("type", filterCriteria.getType());
				rule.put("input", filterCriteria.getInput());
				rule.put("operator", filterCriteria.getOperator());
				
				if(filterCriteria.getValue() instanceof List) {
					ArrayNode filterValsArrayNode = mapper.createArrayNode();
					for(Object rawVal : (List<Object>) filterCriteria.getValue()) {
						filterValsArrayNode.add(rawVal != null ? rawVal.toString() : null);
					}
//					logger.info("Adding value node to 'rules' as Array node");
					rule.set("value", filterValsArrayNode);
				}
				else
					rule.put("value", (filterCriteria.getValue() != null ? filterCriteria.getValue().toString() : null));
					
				rules.add(rule);
			}

		}
		return rules;
	}

	/**
	 * Recursive method to convert filter groups & its nested counterparts to
	 * SQLQueryBuilder supported version
	 * 
	 * @param filterGroups
	 * @param rootQueryJSONString
	 * @param childQueryJSONString
	 * @return
	 * @throws JsonProcessingException
	 */
	private ObjectNode createFilterGroupJsonString(List<FilterGroup> filterGroups, ObjectNode parentNode)
			throws JsonProcessingException {
		ArrayNode parentRulesNode = (ArrayNode) parentNode.get("rules");

		// for nested & so on filter groups
		for (FilterGroup filterGroup : filterGroups) {
			ObjectNode childNode = mapper.createObjectNode();
			childNode.put("condition", filterGroup.getCondition());
			childNode.set("rules", mapFilterRules(filterGroup.getFilterCriteria()));
			parentRulesNode.add(childNode);
			parentNode.set("rules", parentRulesNode);

			if (!filterGroup.getFilterGroup().isEmpty()) {
				createFilterGroupJsonString(filterGroup.getFilterGroup(), childNode);
			}
		}

		return parentNode;
	}

	/**
	 * Enums describing the specific keywords that are kind of templates in the
	 * generated query, that needs to be replaced by their original values
	 */
	private static enum QueueQueryKeywords {
		WRAPPER_QUERY_APPLICATION_ID(":applicationId"), QUEUE_WHERE_FILTER_CONDITION(":queue_filterCondition"),
		PERSONAL_QUEUE_WHERE_FILTER_CONDITION(":queue_personalQFilter"), USER_LOCALE(":userLocale"),
		SELECT(":SELECT"), FROM(":FROM"), WHERE(":WHERE"), GROUP_BY(":GROUP_BY"), ORDER_BY(":ORDER_BY"); 

		private String value;

		private QueueQueryKeywords(String keyWordValue) {
			this.value = keyWordValue;
		}

		/**
		 * This returns the associated keyword to be replaced within the queue runtime
		 * query
		 * 
		 * @return
		 */
		public String getKeyword() {
			return this.value;
		}
	}

	/**
	 * Utility wrapper class for wrapping the results of the Queue report generation
	 * query to be used for further processing 
	 */
	private class QueueReportQueryResponseWrapper {
		private Long applicationPeriod;
		private Long applicationCount;
		
		public Long getApplicationPeriod() {
			return applicationPeriod;
		}
		public void setApplicationPeriod(Long applicationPeriod) {
			this.applicationPeriod = applicationPeriod;
		}
		public Long getApplicationCount() {
			return applicationCount;
		}
		public void setApplicationCount(Long applicationCount) {
			this.applicationCount = applicationCount;
		}
		
	}
	
	private class QueueResultMapper implements RowMapper<QueueResponse>{		
		 		@Override
		 		public QueueResponse mapRow(ResultSet rs, int map) throws SQLException {
		 			if(logger.isInfoEnabled())
		 				logger.info("#------QueueResultMapper Starting-----#");
					QueueResponse queueResponseRec = new QueueResponse();
					queueResponseRec.setApplicationId(rs.getInt("applicationId"));
					queueResponseRec.setApplicationNumber(rs.getString("applicationNumber"));
					queueResponseRec.setApplicationStatus(rs.getString("applicationStatus"));
					queueResponseRec.setCaseReviewType(rs.getString("caseReviewType"));
					queueResponseRec.setCaseReviewLevel(rs.getInt("caseReviewLevel"));
					queueResponseRec.setApplicationChannel(rs.getString("applicationChannel"));
					queueResponseRec.setCustomerName(rs.getString("customerName"));
					queueResponseRec.setDateOfBirth(rs.getTimestamp("dateOfBirth"));	//TD Spec
					queueResponseRec.setProvince(rs.getString("province"));		//TD Spec
					queueResponseRec.setPostalCode(rs.getString("postalCode"));		//TD Spec
					queueResponseRec.setPhoneNumber(rs.getString("phoneNumber"));
					queueResponseRec.setMothersMaidenName(rs.getString("motherMadName"));  //TD spec
					queueResponseRec.setEmployerName(rs.getString("employer"));  //TD spec
					queueResponseRec.setEmploymentJobcode(rs.getString("empJobCode"));  //TD spec
					queueResponseRec.setEmailId(rs.getString("emailId"));
					queueResponseRec.setIpAddress(rs.getString("IPAddress"));
					queueResponseRec.setApplicationCreatedOn(rs.getTimestamp("appCreatedOn"));
					queueResponseRec.setApplicationUpdatedOn(rs.getTimestamp("appUpdatedOn"));	//TD spec
					queueResponseRec.setApplicationCreatedBy(rs.getString("createdByUser"));	//TD spec
					queueResponseRec.setApplicationUpdatedBy(rs.getString("updatedByUser"));
					queueResponseRec.setAppCreatedSinceDays(rs.getObject("appCreatedSince") != null ? rs.getInt("appCreatedSince") : -1);	//TD Spec
					queueResponseRec.setAppOwnedSinceDays(rs.getObject("appOwnedSince") != null ? rs.getInt("appOwnedSince") : -1);		//TD Spec
					queueResponseRec.setAppHoldUntil(rs.getTimestamp("appHoldUntil"));	//TD Spec
					queueResponseRec.setAppHoldReason(rs.getString("appHoldReason"));	//TD Spec
					queueResponseRec.setApplicationLockedBy(rs.getString("lockedByUser"));
					if(logger.isInfoEnabled())
						logger.info("#----QueueResultMapper Ending ----#");
					return queueResponseRec;
				}
	}
	
	private class QueueResultAppIdMapper implements RowMapper<Integer>{		
 		@Override
 		public Integer mapRow(ResultSet rs, int map) throws SQLException {
 			if(logger.isInfoEnabled())
 				logger.info("#-----QueueResultAppIdMapper Starting-----#");
	 		Integer applicationId = rs.getInt(1);
	 		if(logger.isInfoEnabled())
	 			logger.info("#----QueueResultAppIdMapper Ending ----#");
			return applicationId;
 		}
	}
	
	private class QueueResultAppCountMapper implements RowMapper<Long>{		
 		@Override
 		public Long mapRow(ResultSet rs, int map) throws SQLException {
 			if(logger.isInfoEnabled())
 				logger.info("#----QueueResultAppCountMapper Starting----#");
	 		Long applicationId = rs.getLong(1);
	 		if(logger.isInfoEnabled())
	 			logger.info("#-----QueueResultAppCountMapper Ending -----# "+applicationId);
			return applicationId;
 		}
	}
	
	private class QueueReportMapper implements RowMapper<QueueReportQueryResponseWrapper>{		
 		@Override
 		public QueueReportQueryResponseWrapper mapRow(ResultSet rs, int map) throws SQLException {
 			QueueReportQueryResponseWrapper wrapper = new QueueReportQueryResponseWrapper();
 			if(logger.isInfoEnabled())
 				logger.info("#------QueueReportMapper Starting-----#");
	 		if(rs.getObject("appPeriod") != null && rs.getObject("appCount") != null) {				
				wrapper.setApplicationPeriod(rs.getLong("appPeriod"));
				wrapper.setApplicationCount(rs.getLong("appCount"));
			}
	 		if(logger.isInfoEnabled())
	 			logger.info("#-----QueueReportMapper Ending -----# ");
			return wrapper;
 		}
	}
}