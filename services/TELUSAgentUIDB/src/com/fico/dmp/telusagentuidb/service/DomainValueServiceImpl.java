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
import com.fico.dmp.telusagentuidb.Document;
import com.fico.dmp.telusagentuidb.DomainValue;
import com.fico.dmp.telusagentuidb.DomainValueDescription;
import com.fico.dmp.telusagentuidb.DomainValueRelation;
import com.fico.dmp.telusagentuidb.Note;
import com.fico.dmp.telusagentuidb.Party;
import com.fico.dmp.telusagentuidb.Queue;


/**
 * ServiceImpl object for domain model class DomainValue.
 *
 * @see DomainValue
 */
@Service("TELUSAgentUIDB.DomainValueService")
@Validated
public class DomainValueServiceImpl implements DomainValueService {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(DomainValueServiceImpl.class.getName());

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueDescriptionService")
    private DomainValueDescriptionService domainValueDescriptionService;

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.PartyService")
    private PartyService partyService;

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.NoteService")
    private NoteService noteService;

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.DocumentService")
    private DocumentService documentService;

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueRelationService")
    private DomainValueRelationService domainValueRelationService;

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.QueueService")
    private QueueService queueService;

    @Lazy
    @Autowired
    @Qualifier("TELUSAgentUIDB.ActivityService")
    private ActivityService activityService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueDao")
    private WMGenericDao<DomainValue, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<DomainValue, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValue create(DomainValue domainValue) {
        LOGGER.debug("Creating a new DomainValue with information: {}", domainValue);

        DomainValue domainValueCreated = this.wmGenericDao.create(domainValue);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(domainValueCreated);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValue getById(Integer domainvalueId) {
        LOGGER.debug("Finding DomainValue by id: {}", domainvalueId);
        return this.wmGenericDao.findById(domainvalueId);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValue findById(Integer domainvalueId) {
        LOGGER.debug("Finding DomainValue by id: {}", domainvalueId);
        try {
            return this.wmGenericDao.findById(domainvalueId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No DomainValue found with id: {}", domainvalueId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public List<DomainValue> findByMultipleIds(List<Integer> domainvalueIds, boolean orderedReturn) {
        LOGGER.debug("Finding DomainValues by ids: {}", domainvalueIds);

        return this.wmGenericDao.findByMultipleIds(domainvalueIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValue update(DomainValue domainValue) {
        LOGGER.debug("Updating DomainValue with information: {}", domainValue);

        this.wmGenericDao.update(domainValue);
        this.wmGenericDao.refresh(domainValue);

        return domainValue;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public DomainValue delete(Integer domainvalueId) {
        LOGGER.debug("Deleting DomainValue with id: {}", domainvalueId);
        DomainValue deleted = this.wmGenericDao.findById(domainvalueId);
        if (deleted == null) {
            LOGGER.debug("No DomainValue found with id: {}", domainvalueId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), DomainValue.class.getSimpleName(), domainvalueId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void delete(DomainValue domainValue) {
        LOGGER.debug("Deleting DomainValue with {}", domainValue);
        this.wmGenericDao.delete(domainValue);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValue> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all DomainValues");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValue> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all DomainValues");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table DomainValue to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table DomainValue to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void importData(MultipartFile file) {
        LOGGER.debug("importing data in the service TELUSAgentUIDB for table DomainValue");
        this.wmGenericDao.importData(file, "TELUSAgentUIDB", "DomainValue");
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
    public Page<Party> findAssociatedParties(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated parties");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValue.id = '" + id + "'");

        return partyService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Note> findAssociatedNotes(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated notes");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValue.id = '" + id + "'");

        return noteService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Queue> findAssociatedQueuesForPersonalQueueField(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated queuesForPersonalQueueField");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByPersonalQueueField.id = '" + id + "'");

        return queueService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Queue> findAssociatedQueuesForQueueResultPage(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated queuesForQueueResultPage");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByQueueResultPage.id = '" + id + "'");

        return queueService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Document> findAssociatedDocumentsForDocumentLabel(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated documentsForDocumentLabel");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByDocumentLabel.id = '" + id + "'");

        return documentService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Document> findAssociatedDocumentsForType(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated documentsForType");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByType.id = '" + id + "'");

        return documentService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueDescription> findAssociatedDomainValueDescriptions(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValueDescriptions");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValue.id = '" + id + "'");

        return domainValueDescriptionService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueRelation> findAssociatedDomainValueRelationsForDomainValueId(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValueRelationsForDomainValueId");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByDomainValueId.id = '" + id + "'");

        return domainValueRelationService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueRelation> findAssociatedDomainValueRelationsForParentDomainValueId2(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValueRelationsForParentDomainValueId2");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByParentDomainValueId2.id = '" + id + "'");

        return domainValueRelationService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<DomainValueRelation> findAssociatedDomainValueRelationsForParentDomainValueId1(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated domainValueRelationsForParentDomainValueId1");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByParentDomainValueId1.id = '" + id + "'");

        return domainValueRelationService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Activity> findAssociatedActivitiesForSource(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated activitiesForSource");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueBySource.id = '" + id + "'");

        return activityService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Activity> findAssociatedActivitiesForType(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated activitiesForType");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByType.id = '" + id + "'");

        return activityService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Activity> findAssociatedActivitiesForApplicationStatus(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated activitiesForApplicationStatus");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("domainValueByApplicationStatus.id = '" + id + "'");

        return activityService.findAll(queryBuilder.toString(), pageable);
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service DomainValueDescriptionService instance
     */
    protected void setDomainValueDescriptionService(DomainValueDescriptionService service) {
        this.domainValueDescriptionService = service;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service PartyService instance
     */
    protected void setPartyService(PartyService service) {
        this.partyService = service;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service NoteService instance
     */
    protected void setNoteService(NoteService service) {
        this.noteService = service;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service DocumentService instance
     */
    protected void setDocumentService(DocumentService service) {
        this.documentService = service;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service DomainValueRelationService instance
     */
    protected void setDomainValueRelationService(DomainValueRelationService service) {
        this.domainValueRelationService = service;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service QueueService instance
     */
    protected void setQueueService(QueueService service) {
        this.queueService = service;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service ActivityService instance
     */
    protected void setActivityService(ActivityService service) {
        this.activityService = service;
    }

}