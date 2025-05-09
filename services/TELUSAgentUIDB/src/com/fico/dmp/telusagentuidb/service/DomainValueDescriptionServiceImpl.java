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

import com.fico.dmp.telusagentuidb.DomainValueDescription;


/**
 * ServiceImpl object for domain model class DomainValueDescription.
 *
 * @see DomainValueDescription
 */
@Service("TELUSAgentUIDB.DomainValueDescriptionService")
@Validated
public class DomainValueDescriptionServiceImpl implements DomainValueDescriptionService {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DomainValueDescriptionServiceImpl.class.getName());


    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueDescriptionDao")
    private WMGenericDao<DomainValueDescription, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<DomainValueDescription, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueDescription create(DomainValueDescription domainValueDescription) {
        LOGGER.debug("Creating a new DomainValueDescription with information: {}", domainValueDescription);

        DomainValueDescription domainValueDescriptionCreated = this.wmGenericDao.create(domainValueDescription);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(domainValueDescriptionCreated);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueDescription getById(Integer domainvaluedescriptionId) {
        LOGGER.debug("Finding DomainValueDescription by id: {}", domainvaluedescriptionId);
        return this.wmGenericDao.findById(domainvaluedescriptionId);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueDescription findById(Integer domainvaluedescriptionId) {
        LOGGER.debug("Finding DomainValueDescription by id: {}", domainvaluedescriptionId);
        try {
            return this.wmGenericDao.findById(domainvaluedescriptionId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No DomainValueDescription found with id: {}", domainvaluedescriptionId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public List<DomainValueDescription> findByMultipleIds(List<Integer> domainvaluedescriptionIds, boolean orderedReturn) {
        LOGGER.debug("Finding DomainValueDescriptions by ids: {}", domainvaluedescriptionIds);

        return this.wmGenericDao.findByMultipleIds(domainvaluedescriptionIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueDescription update(DomainValueDescription domainValueDescription) {
        LOGGER.debug("Updating DomainValueDescription with information: {}", domainValueDescription);

        this.wmGenericDao.update(domainValueDescription);
        this.wmGenericDao.refresh(domainValueDescription);

        return domainValueDescription;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueDescription delete(Integer domainvaluedescriptionId) {
        LOGGER.debug("Deleting DomainValueDescription with id: {}", domainvaluedescriptionId);
        DomainValueDescription deleted = this.wmGenericDao.findById(domainvaluedescriptionId);
        if (deleted == null) {
            LOGGER.debug("No DomainValueDescription found with id: {}", domainvaluedescriptionId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), DomainValueDescription.class.getSimpleName(), domainvaluedescriptionId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void delete(DomainValueDescription domainValueDescription) {
        LOGGER.debug("Deleting DomainValueDescription with {}", domainValueDescription);
        this.wmGenericDao.delete(domainValueDescription);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueDescription> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all DomainValueDescriptions");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueDescription> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all DomainValueDescriptions");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table DomainValueDescription to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table DomainValueDescription to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void importData(MultipartFile file) {
        LOGGER.debug("importing data in the service TELUSAgentUIDB for table DomainValueDescription");
        this.wmGenericDao.importData(file, "TELUSAgentUIDB", "DomainValueDescription");
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