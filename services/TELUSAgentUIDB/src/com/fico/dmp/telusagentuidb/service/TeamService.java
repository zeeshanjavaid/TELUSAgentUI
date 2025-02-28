/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;

import com.fico.dmp.telusagentuidb.Team;
import com.fico.dmp.telusagentuidb.TeamManager;
import com.fico.dmp.telusagentuidb.TeamUser;

/**
 * Service object for domain model class {@link Team}.
 */
public interface TeamService {

    /**
     * Creates a new Team. It does cascade insert for all the children in a single transaction.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Team if any.
     *
     * @param team Details of the Team to be created; value cannot be null.
     * @return The newly created Team.
     */
    Team create(@Valid Team team);


	/**
     * Returns Team by given id if exists.
     *
     * @param teamIdInstance The id of the Team to get; value cannot be null.
     * @return Team associated with the given teamIdInstance.
	 * @throws EntityNotFoundException If no Team is found.
     */
    Team getById(Integer teamIdInstance);

    /**
     * Find and return the Team by given id if exists, returns null otherwise.
     *
     * @param teamIdInstance The id of the Team to get; value cannot be null.
     * @return Team associated with the given teamIdInstance.
     */
    Team findById(Integer teamIdInstance);

	/**
     * Find and return the list of Teams by given id's.
     *
     * If orderedReturn true, the return List is ordered and positional relative to the incoming ids.
     *
     * In case of unknown entities:
     *
     * If enabled, A null is inserted into the List at the proper position(s).
     * If disabled, the nulls are not put into the return List.
     *
     * @param teamIdInstances The id's of the Team to get; value cannot be null.
     * @param orderedReturn Should the return List be ordered and positional in relation to the incoming ids?
     * @return Teams associated with the given teamIdInstances.
     */
    List<Team> findByMultipleIds(List<Integer> teamIdInstances, boolean orderedReturn);


    /**
     * Updates the details of an existing Team. It replaces all fields of the existing Team with the given team.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Team if any.
     *
     * @param team The details of the Team to be updated; value cannot be null.
     * @return The updated Team.
     * @throws EntityNotFoundException if no Team is found with given input.
     */
    Team update(@Valid Team team);

    /**
     * Deletes an existing Team with the given id.
     *
     * @param teamIdInstance The id of the Team to be deleted; value cannot be null.
     * @return The deleted Team.
     * @throws EntityNotFoundException if no Team found with the given id.
     */
    Team delete(Integer teamIdInstance);

    /**
     * Deletes an existing Team with the given object.
     *
     * @param team The instance of the Team to be deleted; value cannot be null.
     */
    void delete(Team team);

    /**
     * Find all Teams matching the given QueryFilter(s).
     * All the QueryFilter(s) are ANDed to filter the results.
     * This method returns Paginated results.
     *
     * @deprecated Use {@link #findAll(String, Pageable)} instead.
     *
     * @param queryFilters Array of queryFilters to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Teams.
     *
     * @see QueryFilter
     * @see Pageable
     * @see Page
     */
    @Deprecated
    Page<Team> findAll(QueryFilter[] queryFilters, Pageable pageable);

    /**
     * Find all Teams matching the given input query. This method returns Paginated results.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query The query to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Teams.
     *
     * @see Pageable
     * @see Page
     */
    Page<Team> findAll(String query, Pageable pageable);

    /**
     * Exports all Teams matching the given input query to the given exportType format.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param exportType The format in which to export the data; value cannot be null.
     * @param query The query to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @return The Downloadable file in given export type.
     *
     * @see Pageable
     * @see ExportType
     * @see Downloadable
     */
    Downloadable export(ExportType exportType, String query, Pageable pageable);

    /**
     * Exports all Teams matching the given input query to the given exportType format.
     *
     * @param options The export options provided by the user; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @param outputStream The output stream of the file for the exported data to be written to.
     *
     * @see DataExportOptions
     * @see Pageable
     * @see OutputStream
     */
    void export(DataExportOptions options, Pageable pageable, OutputStream outputStream);

    /**
     * Imports all Teams from the csv into the table.
     *
     * @param options The export options provided by the user; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @param outputStream The output stream of the file for the exported data to be written to.
     *
     * @see DataExportOptions
     */
    void importData(MultipartFile file);

    /**
     * Retrieve the count of the Teams in the repository with matching query.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query query to filter results. No filters applied if the input is null/empty.
     * @return The count of the Team.
     */
    long count(String query);

    /**
     * Retrieve aggregated values with matching aggregation info.
     *
     * @param aggregationInfo info related to aggregations.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @return Paginated data with included fields.
     *
     * @see AggregationInfo
     * @see Pageable
     * @see Page
	 */
    Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable);

    /*
     * Returns the associated teamUsers for given Team id.
     *
     * @param id value of id; value cannot be null
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of associated TeamUser instances.
     *
     * @see Pageable
     * @see Page
     */
    Page<TeamUser> findAssociatedTeamUsers(Integer id, Pageable pageable);

    /*
     * Returns the associated teamManagers for given Team id.
     *
     * @param id value of id; value cannot be null
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of associated TeamManager instances.
     *
     * @see Pageable
     * @see Page
     */
    Page<TeamManager> findAssociatedTeamManagers(Integer id, Pageable pageable);

}