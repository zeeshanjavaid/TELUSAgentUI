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

import com.fico.dmp.telusagentuidb.QrtzFiredTriggers;
import com.fico.dmp.telusagentuidb.QrtzFiredTriggersId;


/**
 * ServiceImpl object for domain model class QrtzFiredTriggers.
 *
 * @see QrtzFiredTriggers
 */
@Service("TELUSAgentUIDB.QrtzFiredTriggersService")
@Validated
public class QrtzFiredTriggersServiceImpl implements QrtzFiredTriggersService {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(QrtzFiredTriggersServiceImpl.class.getName());


    @Autowired
    @Qualifier("TELUSAgentUIDB.QrtzFiredTriggersDao")
    private WMGenericDao<QrtzFiredTriggers, QrtzFiredTriggersId> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<QrtzFiredTriggers, QrtzFiredTriggersId> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public QrtzFiredTriggers create(QrtzFiredTriggers qrtzFiredTriggers) {
        LOGGER.debug("Creating a new QrtzFiredTriggers with information: {}", qrtzFiredTriggers);

        QrtzFiredTriggers qrtzFiredTriggersCreated = this.wmGenericDao.create(qrtzFiredTriggers);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(qrtzFiredTriggersCreated);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public QrtzFiredTriggers getById(QrtzFiredTriggersId qrtzfiredtriggersId) {
        LOGGER.debug("Finding QrtzFiredTriggers by id: {}", qrtzfiredtriggersId);
        return this.wmGenericDao.findById(qrtzfiredtriggersId);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public QrtzFiredTriggers findById(QrtzFiredTriggersId qrtzfiredtriggersId) {
        LOGGER.debug("Finding QrtzFiredTriggers by id: {}", qrtzfiredtriggersId);
        try {
            return this.wmGenericDao.findById(qrtzfiredtriggersId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No QrtzFiredTriggers found with id: {}", qrtzfiredtriggersId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public List<QrtzFiredTriggers> findByMultipleIds(List<QrtzFiredTriggersId> qrtzfiredtriggersIds, boolean orderedReturn) {
        LOGGER.debug("Finding QrtzFiredTriggers by ids: {}", qrtzfiredtriggersIds);

        return this.wmGenericDao.findByMultipleIds(qrtzfiredtriggersIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public QrtzFiredTriggers update(QrtzFiredTriggers qrtzFiredTriggers) {
        LOGGER.debug("Updating QrtzFiredTriggers with information: {}", qrtzFiredTriggers);

        this.wmGenericDao.update(qrtzFiredTriggers);
        this.wmGenericDao.refresh(qrtzFiredTriggers);

        return qrtzFiredTriggers;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public QrtzFiredTriggers delete(QrtzFiredTriggersId qrtzfiredtriggersId) {
        LOGGER.debug("Deleting QrtzFiredTriggers with id: {}", qrtzfiredtriggersId);
        QrtzFiredTriggers deleted = this.wmGenericDao.findById(qrtzfiredtriggersId);
        if (deleted == null) {
            LOGGER.debug("No QrtzFiredTriggers found with id: {}", qrtzfiredtriggersId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), QrtzFiredTriggers.class.getSimpleName(), qrtzfiredtriggersId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void delete(QrtzFiredTriggers qrtzFiredTriggers) {
        LOGGER.debug("Deleting QrtzFiredTriggers with {}", qrtzFiredTriggers);
        this.wmGenericDao.delete(qrtzFiredTriggers);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<QrtzFiredTriggers> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all QrtzFiredTriggers");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<QrtzFiredTriggers> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all QrtzFiredTriggers");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table QrtzFiredTriggers to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table QrtzFiredTriggers to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void importData(MultipartFile file) {
        LOGGER.debug("importing data in the service TELUSAgentUIDB for table QrtzFiredTriggers");
        this.wmGenericDao.importData(file, "TELUSAgentUIDB", "QRTZ_FIRED_TRIGGERS");
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