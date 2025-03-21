/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.wavemaker.commons.wrapper.StringWrapper;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.manager.ExportedFileManager;
import com.wavemaker.runtime.file.model.DownloadResponse;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wavemaker.runtime.security.xss.XssDisable;
import com.wavemaker.runtime.util.WMMultipartUtils;
import com.wavemaker.runtime.util.WMRuntimeUtils;
import com.wavemaker.runtime.util.logging.FAWBStaticLoggerBinder;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.fico.dmp.telusagentuidb.EntityDocuments;
import com.fico.dmp.telusagentuidb.service.EntityDocumentsService;


/**
 * Controller object for domain model class EntityDocuments.
 * @see EntityDocuments
 */
@RestController("TELUSAgentUIDB.EntityDocumentsController")
@Api(value = "EntityDocumentsController", description = "Exposes APIs to work with EntityDocuments resource.")
@RequestMapping("/TELUSAgentUIDB/EntityDocuments")
public class EntityDocumentsController {

    private static final Logger LOGGER =  FAWBStaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(EntityDocumentsController.class.getName());

    @Autowired
	@Qualifier("TELUSAgentUIDB.EntityDocumentsService")
	private EntityDocumentsService entityDocumentsService;

	@Autowired
	private ExportedFileManager exportedFileManager;

	@ApiOperation(value = "Creates a new EntityDocuments instance.")
    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public EntityDocuments createEntityDocuments(@RequestPart("wm_data_json") EntityDocuments entityDocuments, @RequestPart(value = "document", required = false) MultipartFile _document) {
		LOGGER.debug("Create EntityDocuments with information: {}" , entityDocuments);

    entityDocuments.setDocument(WMMultipartUtils.toByteArray(_document));
		entityDocuments = entityDocumentsService.create(entityDocuments);
		LOGGER.debug("Created EntityDocuments with information: {}" , entityDocuments);

	    return entityDocuments;
	}

    @ApiOperation(value = "Returns the EntityDocuments instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public EntityDocuments getEntityDocuments(@PathVariable("id") Integer id) {
        LOGGER.debug("Getting EntityDocuments with id: {}" , id);

        EntityDocuments foundEntityDocuments = entityDocumentsService.getById(id);
        LOGGER.debug("EntityDocuments details with id: {}" , foundEntityDocuments);

        return foundEntityDocuments;
    }

    @ApiOperation(value = "Retrieves content for the given BLOB field in EntityDocuments instance" )
    @RequestMapping(value = "/{id}/content/{fieldName}", method = RequestMethod.GET, produces="application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public DownloadResponse getEntityDocumentsBLOBContent(@PathVariable("id") Integer id, @PathVariable("fieldName") String fieldName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam(value="download", defaultValue = "false") boolean download) {

        LOGGER.debug("Retrieves content for the given BLOB field {} in EntityDocuments instance" , fieldName);

        if(!WMRuntimeUtils.isLob(EntityDocuments.class, fieldName)) {
            throw new TypeMismatchException("Given field " + fieldName + " is not a valid BLOB type");
        }
        EntityDocuments entityDocuments = entityDocumentsService.getById(id);

        return WMMultipartUtils.buildDownloadResponseForBlob(entityDocuments, fieldName, httpServletRequest, download);
    }

    @ApiOperation(value = "Updates the EntityDocuments instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public EntityDocuments editEntityDocuments(@PathVariable("id") Integer id, @RequestBody EntityDocuments entityDocuments) {
        LOGGER.debug("Editing EntityDocuments with id: {}" , entityDocuments.getId());

        entityDocuments.setId(id);
        entityDocuments = entityDocumentsService.update(entityDocuments);
        LOGGER.debug("EntityDocuments details with id: {}" , entityDocuments);

        return entityDocuments;
    }

    @ApiOperation(value = "Updates the EntityDocuments instance associated with the given id.This API should be used when EntityDocuments instance fields that require multipart data.") 
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public EntityDocuments editEntityDocuments(@PathVariable("id") Integer id, MultipartHttpServletRequest multipartHttpServletRequest) {
        EntityDocuments newEntityDocuments = WMMultipartUtils.toObject(multipartHttpServletRequest, EntityDocuments.class, "TELUSAgentUIDB");
        newEntityDocuments.setId(id);

        EntityDocuments oldEntityDocuments = entityDocumentsService.getById(id);
        WMMultipartUtils.updateLobsContent(oldEntityDocuments, newEntityDocuments);
        LOGGER.debug("Updating EntityDocuments with information: {}" , newEntityDocuments);

        return entityDocumentsService.update(newEntityDocuments);
    }

    @ApiOperation(value = "Deletes the EntityDocuments instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteEntityDocuments(@PathVariable("id") Integer id) {
        LOGGER.debug("Deleting EntityDocuments with id: {}" , id);

        EntityDocuments deletedEntityDocuments = entityDocumentsService.delete(id);

        return deletedEntityDocuments != null;
    }

    /**
     * @deprecated Use {@link #findEntityDocuments(String, Pageable)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of EntityDocuments instances matching the search criteria.")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public Page<EntityDocuments> searchEntityDocumentsByQueryFilters( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering EntityDocuments list by query filter:{}", (Object) queryFilters);
        return entityDocumentsService.findAll(queryFilters, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of EntityDocuments instances matching the optional query (q) request param. If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, sort can be sent as request parameters. The sort value should be a comma separated list of field names & optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @RequestMapping(method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<EntityDocuments> findEntityDocuments(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering EntityDocuments list by filter:", query);
        return entityDocumentsService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of EntityDocuments instances matching the optional query (q) request param. This API should be used only if the query string is too big to fit in GET request with request param. The request has to made in application/x-www-form-urlencoded format.")
    @RequestMapping(value="/filter", method = RequestMethod.POST, consumes= "application/x-www-form-urlencoded")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public Page<EntityDocuments> filterEntityDocuments(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering EntityDocuments list by filter", query);
        return entityDocumentsService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/export/{exportType}", method = {RequestMethod.GET,  RequestMethod.POST}, produces = "application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public Downloadable exportEntityDocuments(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return entityDocumentsService.export(exportType, query, pageable);
    }

    @ApiOperation(value = "Returns a URL to download a file for the data matching the optional query (q) request param and the required fields provided in the Export Options.") 
    @RequestMapping(value = "/export", method = {RequestMethod.POST}, consumes = "application/json")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public StringWrapper exportEntityDocumentsAndGetURL(@RequestBody DataExportOptions exportOptions, Pageable pageable) {
        String exportedFileName = exportOptions.getFileName();
        if(exportedFileName == null || exportedFileName.isEmpty()) {
            exportedFileName = EntityDocuments.class.getSimpleName();
        }
        exportedFileName += exportOptions.getExportType().getExtension();
        String exportedUrl = exportedFileManager.registerAndGetURL(exportedFileName, outputStream -> entityDocumentsService.export(exportOptions, pageable, outputStream));
        return new StringWrapper(exportedUrl);
    }

	@ApiOperation(value = "Returns the total count of EntityDocuments instances matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
	@RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	@XssDisable
	public Long countEntityDocuments( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting EntityDocuments");
		return entityDocumentsService.count(query);
	}

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@RequestMapping(value = "/aggregations", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	@XssDisable
	public Page<Map<String, Object>> getEntityDocumentsAggregatedValues(@RequestBody AggregationInfo aggregationInfo, Pageable pageable) {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return entityDocumentsService.getAggregatedValues(aggregationInfo, pageable);
    }

    @ApiOperation(value = "Consumes and inserts csv data into the table")
	@RequestMapping(value = "/import", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	@XssDisable
	public void importEntityDocumentss(@RequestPart("file") @Valid @NotNull MultipartFile file) {
        LOGGER.debug("Importing EntityDocuments table rows from csv");
        entityDocumentsService.importData(file);
    }


    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service EntityDocumentsService instance
	 */
	protected void setEntityDocumentsService(EntityDocumentsService service) {
		this.entityDocumentsService = service;
	}

}