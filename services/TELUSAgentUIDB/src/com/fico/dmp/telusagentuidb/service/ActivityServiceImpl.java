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
import org.springframework.context.annotation.Lazy;
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

import com.fico.dmp.telusagentuidb.Activity;
import com.fico.dmp.telusagentuidb.ActivityPayload;


/**
 * ServiceImpl object for domain model class Activity.
 *
 * @see Activity
 */
@Service("TELUSAgentUIDB.ActivityService")
@Validated
public class ActivityServiceImpl implements ActivityService {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(ActivityServiceImpl.class.getName());

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.ActivityPayloadService")
    private ActivityPayloadService activityPayloadService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.ActivityDao")
    private WMGenericDao<Activity, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<Activity, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Activity create(Activity activity) {
        LOGGER.debug("Creating a new Activity with information: {}", activity);

        Activity activityCreated = this.wmGenericDao.create(activity);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(activityCreated);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Activity getById(Integer activityId) {
        LOGGER.debug("Finding Activity by id: {}", activityId);
        return this.wmGenericDao.findById(activityId);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Activity findById(Integer activityId) {
        LOGGER.debug("Finding Activity by id: {}", activityId);
        try {
            return this.wmGenericDao.findById(activityId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No Activity found with id: {}", activityId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public List<Activity> findByMultipleIds(List<Integer> activityIds, boolean orderedReturn) {
        LOGGER.debug("Finding Activities by ids: {}", activityIds);

        return this.wmGenericDao.findByMultipleIds(activityIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Activity update(Activity activity) {
        LOGGER.debug("Updating Activity with information: {}", activity);

        this.wmGenericDao.update(activity);
        this.wmGenericDao.refresh(activity);

        return activity;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Activity delete(Integer activityId) {
        LOGGER.debug("Deleting Activity with id: {}", activityId);
        Activity deleted = this.wmGenericDao.findById(activityId);
        if (deleted == null) {
            LOGGER.debug("No Activity found with id: {}", activityId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), Activity.class.getSimpleName(), activityId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void delete(Activity activity) {
        LOGGER.debug("Deleting Activity with {}", activity);
        this.wmGenericDao.delete(activity);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Activity> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all Activities");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Activity> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all Activities");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table Activity to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table Activity to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void importData(MultipartFile file) {
        LOGGER.debug("importing data in the service TELUSAgentUIDB for table Activity");
        this.wmGenericDao.importData(file, "TELUSAgentUIDB", "ACTIVITY");
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

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<ActivityPayload> findAssociatedActivityPayloads(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated activityPayloads");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("activity.id = '" + id + "'");

        return activityPayloadService.findAll(queryBuilder.toString(), pageable);
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service ActivityPayloadService instance
     */
    protected void setActivityPayloadService(ActivityPayloadService service) {
        this.activityPayloadService = service;
    }

}