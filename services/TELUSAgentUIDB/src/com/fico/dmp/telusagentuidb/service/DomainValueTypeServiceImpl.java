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

import com.fico.dmp.telusagentuidb.DomainValue;
import com.fico.dmp.telusagentuidb.DomainValueType;
import com.fico.dmp.telusagentuidb.DomainValueTypeRelationship;


/**
 * ServiceImpl object for domain model class DomainValueType.
 *
 * @see DomainValueType
 */
@Service("TELUSAgentUIDB.DomainValueTypeService")
@Validated
public class DomainValueTypeServiceImpl implements DomainValueTypeService {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DomainValueTypeServiceImpl.class.getName());

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueService")
    private DomainValueService domainValueService;

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueTypeRelationshipService")
    private DomainValueTypeRelationshipService domainValueTypeRelationshipService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueTypeDao")
    private WMGenericDao<DomainValueType, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<DomainValueType, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueType create(DomainValueType domainValueType) {
        LOGGER.debug("Creating a new DomainValueType with information: {}", domainValueType);

        DomainValueType domainValueTypeCreated = this.wmGenericDao.create(domainValueType);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(domainValueTypeCreated);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueType getById(Integer domainvaluetypeId) {
        LOGGER.debug("Finding DomainValueType by id: {}", domainvaluetypeId);
        return this.wmGenericDao.findById(domainvaluetypeId);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueType findById(Integer domainvaluetypeId) {
        LOGGER.debug("Finding DomainValueType by id: {}", domainvaluetypeId);
        try {
            return this.wmGenericDao.findById(domainvaluetypeId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No DomainValueType found with id: {}", domainvaluetypeId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public List<DomainValueType> findByMultipleIds(List<Integer> domainvaluetypeIds, boolean orderedReturn) {
        LOGGER.debug("Finding DomainValueTypes by ids: {}", domainvaluetypeIds);

        return this.wmGenericDao.findByMultipleIds(domainvaluetypeIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueType update(DomainValueType domainValueType) {
        LOGGER.debug("Updating DomainValueType with information: {}", domainValueType);

        this.wmGenericDao.update(domainValueType);
        this.wmGenericDao.refresh(domainValueType);

        return domainValueType;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValueType delete(Integer domainvaluetypeId) {
        LOGGER.debug("Deleting DomainValueType with id: {}", domainvaluetypeId);
        DomainValueType deleted = this.wmGenericDao.findById(domainvaluetypeId);
        if (deleted == null) {
            LOGGER.debug("No DomainValueType found with id: {}", domainvaluetypeId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), DomainValueType.class.getSimpleName(), domainvaluetypeId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void delete(DomainValueType domainValueType) {
        LOGGER.debug("Deleting DomainValueType with {}", domainValueType);
        this.wmGenericDao.delete(domainValueType);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueType> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all DomainValueTypes");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueType> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all DomainValueTypes");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table DomainValueType to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table DomainValueType to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void importData(MultipartFile file) {
        LOGGER.debug("importing data in the service TELUSAgentUIDB for table DomainValueType");
        this.wmGenericDao.importData(file, "TELUSAgentUIDB", "DomainValueType");
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
    public Page<DomainValue> findAssociatedDomainValues(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValues");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueTypeByDomainValueType.id = '" + id + "'");

        return domainValueService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueTypeRelationship> findAssociatedDomainValueTypeRelationshipsForDomainValueTypeId(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValueTypeRelationshipsForDomainValueTypeId");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueTypeByDomainValueTypeId.id = '" + id + "'");

        return domainValueTypeRelationshipService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueTypeRelationship> findAssociatedDomainValueTypeRelationshipsForParentDomainValueTypeId2(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValueTypeRelationshipsForParentDomainValueTypeId2");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueTypeByParentDomainValueTypeId2.id = '" + id + "'");

        return domainValueTypeRelationshipService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueTypeRelationship> findAssociatedDomainValueTypeRelationshipsForParentDomainValueTypeId1(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValueTypeRelationshipsForParentDomainValueTypeId1");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueTypeByParentDomainValueTypeId1.id = '" + id + "'");

        return domainValueTypeRelationshipService.findAll(queryBuilder.toString(), pageable);
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service DomainValueService instance
     */
    protected void setDomainValueService(DomainValueService service) {
        this.domainValueService = service;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service DomainValueTypeRelationshipService instance
     */
    protected void setDomainValueTypeRelationshipService(DomainValueTypeRelationshipService service) {
        this.domainValueTypeRelationshipService = service;
    }

}