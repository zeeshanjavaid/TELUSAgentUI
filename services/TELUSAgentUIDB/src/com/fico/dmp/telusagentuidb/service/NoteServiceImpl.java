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

import com.fico.dmp.telusagentuidb.Note;


/**
 * ServiceImpl object for domain model class Note.
 *
 * @see Note
 */
@Service("TELUSAgentUIDB.NoteService")
@Validated
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(NoteServiceImpl.class.getName());


    @Autowired
    @Qualifier("TELUSAgentUIDB.NoteDao")
    private WMGenericDao<Note, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<Note, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Note create(Note note) {
        LOGGER.debug("Creating a new Note with information: {}", note);

        Note noteCreated = this.wmGenericDao.create(note);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(noteCreated);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Note getById(Integer noteId) {
        LOGGER.debug("Finding Note by id: {}", noteId);
        return this.wmGenericDao.findById(noteId);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Note findById(Integer noteId) {
        LOGGER.debug("Finding Note by id: {}", noteId);
        try {
            return this.wmGenericDao.findById(noteId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No Note found with id: {}", noteId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public List<Note> findByMultipleIds(List<Integer> noteIds, boolean orderedReturn) {
        LOGGER.debug("Finding Notes by ids: {}", noteIds);

        return this.wmGenericDao.findByMultipleIds(noteIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Note update(Note note) {
        LOGGER.debug("Updating Note with information: {}", note);

        this.wmGenericDao.update(note);
        this.wmGenericDao.refresh(note);

        return note;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Note delete(Integer noteId) {
        LOGGER.debug("Deleting Note with id: {}", noteId);
        Note deleted = this.wmGenericDao.findById(noteId);
        if (deleted == null) {
            LOGGER.debug("No Note found with id: {}", noteId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), Note.class.getSimpleName(), noteId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void delete(Note note) {
        LOGGER.debug("Deleting Note with {}", note);
        this.wmGenericDao.delete(note);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Note> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all Notes");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public Page<Note> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all Notes");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table Note to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "TELUSAgentUIDBTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service TELUSAgentUIDB for table Note to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "TELUSAgentUIDBTransactionManager")
    @Override
    public void importData(MultipartFile file) {
        LOGGER.debug("importing data in the service TELUSAgentUIDB for table Note");
        this.wmGenericDao.importData(file, "TELUSAgentUIDB", "NOTE");
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