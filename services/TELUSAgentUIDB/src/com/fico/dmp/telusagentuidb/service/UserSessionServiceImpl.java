/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.wavemaker.commons.MessageResource;
import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;

import com.fico.dmp.telusagentuidb.UserSession;


/**
 * ServiceImpl object for domain model class UserSession.
 *
 * @see UserSession
 */
@Service("TELUSAgentUIDB.UserSessionService")
@Validated
public class UserSessionServiceImpl implements UserSessionService {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(UserSessionServiceImpl.class.getName());


    @Autowired
    @Qualifier("TELUSAgentUIDB.UserSessionDao")
    private WMGenericDao<UserSession, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<UserSession, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public UserSession create(UserSession userSession) {
        LOGGER.debug("Creating a new UserSession with information: {}", userSession);

        UserSession userSessionCreated = this.wmGenericDao.create(userSession);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(userSessionCreated);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public UserSession getById(Integer usersessionId) {
        LOGGER.debug("Finding UserSession by id: {}", usersessionId);
        return this.wmGenericDao.findById(usersessionId);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public UserSession findById(Integer usersessionId) {
        LOGGER.debug("Finding UserSession by id: {}", usersessionId);
        try {
            return this.wmGenericDao.findById(usersessionId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No UserSession found with id: {}", usersessionId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public List<UserSession> findByMultipleIds(List<Integer> usersessionIds, boolean orderedReturn) {
        LOGGER.debug("Finding UserSessions by ids: {}", usersessionIds);

        return this.wmGenericDao.findByMultipleIds(usersessionIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public UserSession update(UserSession userSession) {
        LOGGER.debug("Updating UserSession with information: {}", userSession);

        this.wmGenericDao.update(userSession);
        this.wmGenericDao.refresh(userSession);

        return userSession;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public UserSession delete(Integer usersessionId) {
        LOGGER.debug("Deleting UserSession with id: {}", usersessionId);
        UserSession deleted = this.wmGenericDao.findById(usersessionId);
        if (deleted == null) {
            LOGGER.debug("No UserSession found with id: {}", usersessionId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), UserSession.class.getSimpleName(), usersessionId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void delete(UserSession userSession) {
        LOGGER.debug("Deleting UserSession with {}", userSession);
        this.wmGenericDao.delete(userSession);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<UserSession> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all UserSessions");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<UserSession> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all UserSessions");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table UserSession to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table UserSession to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void importData(MultipartFile file) {
        LOGGER.debug("importing data in the service TELUSAgentUIDB for table UserSession");
        this.wmGenericDao.importData(file, "TELUSAgentUIDB", "USER_SESSION");
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public long count(String query) {
        return this.wmGenericDao.count(query);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return this.wmGenericDao.getAggregatedValues(aggregationInfo, pageable);
    }



}