package com.fico.core.services;

import com.fico.dmp.telusagentuidb.DomainValue;
import com.fico.dmp.telusagentuidb.DomainValueDescription;
import com.fico.dmp.telusagentuidb.DomainValueRelation;
import com.fico.dmp.telusagentuidb.DomainValueType;
import com.fico.dmp.telusagentuidb.DomainValueTypeRelationship;
import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.models.query.*;
import com.fico.dmp.telusagentuidb.service.*;
import com.fico.ps.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service("facade.DomainValueServiceBS")
public class DomainValueServiceBS {

    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueService")
    private DomainValueService domainValueService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueDescriptionService")
    private DomainValueDescriptionService domainValueDescriptionService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueRelationService")
    private DomainValueRelationService domainValueRelationService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueTypeService")
    private DomainValueTypeService domainValueTypeService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.DomainValueTypeRelationshipService")
    private DomainValueTypeRelationshipService domainValueTypeRelationshipService;

    @Autowired
    @Qualifier("TELUSAgentUIDB.UserService")
    private UserService userService;

    @Autowired
    private TELUSAgentUIDBQueryExecutorService telusAgentUIDBQueryExecutorService;

    private static final Logger logger = LoggerFactory.getLogger(DomainValueServiceBS.class);

    /**
     * Validates if the domain value against the supplied domain value ID is <b>ACITVE</b>.
     * If <b>INACTIVE</b> returns an error message that can be shown in UI against the field
     * on which this inactive domain value is set. This serves as a validation for inactive
     * domain values while application submission.
     *
     * @param domainValueId
     * @param userLocale
     * @param UIFieldLabel  The UI field label to which this domain value is bound
     * @return
     */
    public String validateDomainValueActive(Integer domainValueId, String userLocale, String UIFieldLabel) {
        //logger.info("-----Inside method 'validateDomainValueActive'");

        String errorMessage = null;
        try {
            if (domainValueId != null && domainValueId != 0) {
                QueryGetDomainValueByIdResponse dvQueryResponse = getDomainValueByDomainValueId(domainValueId, userLocale);
                if (dvQueryResponse != null) {
                    //logger.info("Domain value check is inactive for: {}. Is DV active: {}", dvQueryResponse.getCode(), dvQueryResponse.getIsActive().booleanValue());
                    //check if the domain value is 'ACTIVE'
                    if (!dvQueryResponse.getIsActive().booleanValue()) {
                        errorMessage = String.format("Field '%s' is set to inactive domain value '%s'", UIFieldLabel, dvQueryResponse.getDescription());
                    }
                }
            }
        } catch (Exception e) {
        	if(logger.isErrorEnabled())
        		logger.error("Exception occured at method 'validateDomainValueActive'", e);
        }

        return errorMessage;
    }

    /**
     * Fetch DomainValue by the domain value CODE and the underlying DomainValue type CODE and locale supplied with
     * an extra ACTIVE flag status
     *
     * @param domainValueCode
     * @param domainValueTypeCode
     * @param userLocale
     * @param isActiveFlag
     * @return
     * @throws Exception
     */
    public QueryGetDvbyCodeAndTypeCodeWithActiveFlagResponse getDomainValueByDVCodeAndDVTypeCodeWithActiveStatus(String domainValueCode,
                                                                                                                 String domainValueTypeCode, String userLocale, Boolean isActiveFlag, Boolean showAll) throws Exception {
        //logger.info("-----Inside method 'getDomainValueByDVCodeAndDVTypeCodeWithActiveStatus'");

        List<QueryGetDvbyCodeAndTypeCodeWithActiveFlagResponse> responseList =
                telusAgentUIDBQueryExecutorService.executeQuery_GetDVByCodeAndTypeCodeWithActiveFlag(userLocale, domainValueCode, domainValueTypeCode, showAll,
                        isActiveFlag, PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return (responseList == null || responseList.isEmpty()) ? null : responseList.get(0);
    }

    /**
     * Fetch all DomainValues by the given DomainValue type CODE and locale supplied with an extra ACTIVE flag status
     *
     * @param domainValueTypeCode
     * @param userLocale
     * @param isActiveFlag
     * @return
     * @throws Exception
     */
    @Cacheable("getAllDomainValuesByDVTypeCodeWithActiveStatus") 
    public List<QueryGetAllDvsByDvtypeWithActiveFlagResponse> getAllDomainValuesByDVTypeCodeWithActiveStatus(String domainValueTypeCode,
                                                                                                             String userLocale, Boolean isActiveFlag, Boolean showAll) throws Exception {
        //logger.info("-----Inside method 'getAllDomainValuesByDVTypeCodeWithActiveStatus'");

        List<QueryGetAllDvsByDvtypeWithActiveFlagResponse> responseList = null;
        responseList = telusAgentUIDBQueryExecutorService.executeQuery_GetAllDVsByDVTypeWithActiveFlag(userLocale, domainValueTypeCode, showAll,
                isActiveFlag, PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return responseList;
    }

    /**
     * Fetch DomainValue by the given DomainValue Id and the locale and Active flag
     *
     * @param domainValueId
     * @param userLocale
     * @param isActiveFlag
     * @return
     * @throws Exception
     */
    public QueryGetDomainValueByIdWithActiveFlagResponse getDomainValueByDomainValueIdWithActiveFlag(Integer domainValueId, String userLocale,
                                                                                                     Boolean isActiveFlag, Boolean showAll) throws Exception {
        //logger.info("-----Inside method 'getDomainValueByDVCodeAndDVTypeCode'");

        List<QueryGetDomainValueByIdWithActiveFlagResponse> responseList =
                telusAgentUIDBQueryExecutorService.executeQuery_GetDomainValueByIdWithActiveFlag(userLocale, domainValueId, showAll, isActiveFlag,
                        PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return (responseList == null || responseList.isEmpty()) ? null : responseList.get(0);
    }

    /**
     * Fetch DomainValue by the given DomainValue Id and the locale
     *
     * @param domainValueId
     * @param userLocale
     * @param isActiveFlag
     * @return
     * @throws Exception
     */
    public QueryGetDomainValueByIdResponse getDomainValueByDomainValueId(Integer domainValueId, String userLocale) throws Exception {
        //logger.info("-----Inside method 'getDomainValueByDVCodeAndDVTypeCode------------'" + domainValueId);

        List<QueryGetDomainValueByIdResponse> responseList =
                telusAgentUIDBQueryExecutorService.executeQuery_GetDomainValueById(userLocale, domainValueId,
                        PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return (responseList == null || responseList.isEmpty()) ? null : responseList.get(0);
    }

    /**
     * Fetch DomainValue by the domain value CODE and the underlying DomainValue type CODE and locale supplied
     *
     * @param domainValueCode
     * @param domainValueTypeCode
     * @param userLocale
     * @return
     * @throws Exception
     */
    @Cacheable("getDomainValueByDVCodeAndDVTypeCode") 
    public QueryGetDomainValueByCodeAndTypeCodeResponse getDomainValueByDVCodeAndDVTypeCode(String domainValueCode,
                                                                                            String domainValueTypeCode, String userLocale) throws Exception {
        //logger.info("-----Inside method 'getDomainValueByDVCodeAndDVTypeCode'");

        List<QueryGetDomainValueByCodeAndTypeCodeResponse> responseList = null;
        responseList = telusAgentUIDBQueryExecutorService.executeQuery_GetDomainValueByCodeAndTypeCode(userLocale, domainValueCode, domainValueTypeCode,
                PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return (responseList == null || responseList.isEmpty()) ? null : responseList.get(0);
    }

    /**
     * Fetch all DomainValues by the given DomainValue type CODE and locale supplied
     *
     * @param domainValueTypeCode
     * @param userLocale
     * @param isAlphaSort
     * @return
     * @throws Exception
     */
    @Cacheable("getAllDomainValuesByTypeCode") 
    public List<QueryGetAllDomainValuesByDvTypeCodeResponse> getAllDomainValuesByDVTypeCode(String domainValueTypeCode,
                                                                                            String userLocale,boolean isAlphaSort) throws Exception {
        //logger.info("-----Inside method 'QueryGetAllDomainValuesByDvTypeCodeResponse'");

        List<QueryGetAllDomainValuesByDvTypeCodeResponse> responseList = null;
        responseList = telusAgentUIDBQueryExecutorService.executeQuery_GetAllDomainValuesByDvTypeCode(userLocale, domainValueTypeCode, isAlphaSort, PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return responseList;
    }

    /**
     * Fetch all DomainValues by the given DomainValue type, locale and with the parent_1 DomainValue Id
     *
     * @param domainValueTypeCode
     * @param userLocale
     * @param parent1DomainValueId
     * @return
     * @throws Exception
     */
    @Cacheable("getAllDomainValueWithSingleParent") 
    public List<QueryGetAllDomainValueWithOneParentResponse> getAllDomainValuesWithSingleParent(String domainValueTypeCode, String userLocale,
                                                                                                Integer parent1DomainValueId, String dvDescription) throws Exception {
        //logger.info("-----Inside method 'getAllDomainValuesWithSingleParent'");

        List<QueryGetAllDomainValueWithOneParentResponse> responseList = null;
        responseList = telusAgentUIDBQueryExecutorService.executeQuery_GetAllDomainValueWithOneParent(userLocale, domainValueTypeCode, parent1DomainValueId,
                dvDescription, PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return responseList;
    }

    /**
     * Fetch all DomainValues by the given DomainValue type, locale and with the parent_1 DomainValue Id and
     * parent_2 DomainValueId
     *
     * @param domainValueTypeCode
     * @param userLocale
     * @param parent1DomainValueId
     * @param parent2DomainValueId
     * @return
     * @throws Exception
     */
    public List<QueryGetAllDomainValuesTwoParentResponse> getAllDomainValuesWithTwoParent(String domainValueTypeCode, String userLocale,
                                                                                          Integer parent1DomainValueId, Integer parent2DomainValueId, String dvDescription) throws Exception {
        //logger.info("-----Inside method 'getAllDomainValuesWithTwoParent'");

        List<QueryGetAllDomainValuesTwoParentResponse> responseList =
                telusAgentUIDBQueryExecutorService.executeQuery_GetAllDomainValuesTwoParent(userLocale, domainValueTypeCode, parent1DomainValueId, parent2DomainValueId, dvDescription,
                        PageRequest.of(0, Integer.MAX_VALUE)).toList();

        return responseList;
    }

    /**
     * Returns the list of DomainValues as {@link DvsearchByCodeAndDescriptionResponse} list based on
     * supplied filters
     *
     * @param domainValueTypeId
     * @param showAll
     * @param isActiveFlag
     * @param searchValue
     * @param defaultLocale
     * @param currentPage
     * @param pageSize
     * @param sortOrders
     * @return
     * @throws Exception
     */
    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public Page<DvsearchByCodeAndDescriptionResponse> getDVListByFilters(Integer domainValueTypeId, Boolean showAll,
                                                                         Boolean isActiveFlag, String searchValue, String defaultLocale,
                                                                         Integer currentPage, Integer pageSize, String sortOrders) throws Exception {
        //logger.info("--------Inside method 'getDVListByFilters'");
        try {
            Page<DvsearchByCodeAndDescriptionResponse> domainValueList = null;
            Pageable pageable = null;

            //set up pagination parameters
            if (currentPage == null && pageSize == null && (sortOrders == null || sortOrders.trim().length() == 0)) {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
            } else {
                List<String> sortOrderList = null;

                if (currentPage == null)
                    currentPage = 0;
                if (pageSize == null)
                    pageSize = Integer.MAX_VALUE;
                if (sortOrders == null || sortOrders.trim().length() == 0) {
                    sortOrderList = new ArrayList<String>(1);
                    //set default sort order on 'code' ASC
                    sortOrderList.add("code ASC");
                }
                if (sortOrders.trim().length() > 0) {
                    sortOrderList = Arrays.asList(sortOrders.split(","));
                }

                //create the pageable instance
                //1. set sort orders and sort
                List<Sort.Order> soList = new ArrayList<Sort.Order>();
                sortOrderList.forEach((sortOrder) -> {
                    final String[] sortMetadata = sortOrder.split(" ");
                    final String propertyName = sortMetadata[0];
                    final String propertyOrder = (sortMetadata.length > 1) ? sortMetadata[1].toUpperCase() : "ASC";

                    soList.add(new Sort.Order(Sort.Direction.fromString(propertyOrder), propertyName));
                });

                //2. set pageable param
                pageable = PageRequest.of(currentPage, pageSize, Sort.by(soList));
            }

            //TODO: remove the double quotes and fix the type
            //call the Core query service
            domainValueList =
                    telusAgentUIDBQueryExecutorService.executeDVSearchByCodeAndDescription(defaultLocale, domainValueTypeId + "", showAll, isActiveFlag, searchValue, pageable);
            if(logger.isInfoEnabled())
            	logger.info("--------QueryService for named query 'DVSearchByCodeAndDescription' returned {} records!", domainValueList.getNumberOfElements());

            return domainValueList;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Returns the list of DomainValueType as {@link DomainValueType} list based on the domain value type description
     *
     * @param description
     * @param currentPage
     * @param pageSize
     * @param sortOrders
     * @return
     * @throws Exception
     */
    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public Page<DomainValueType> getDomainValueTypeByDescription(String description, Integer currentPage,
                                                                 Integer pageSize, String sortOrders) throws Exception {
        //logger.info("--------Inside method 'getDomainValueTypeByDescription'");
        try {
            Page<DomainValueType> domainValueTypeList = null;
            Pageable pageable = null;

            //check for pagination parameters
            if (currentPage == null && pageSize == null && (sortOrders == null || sortOrders.trim().length() == 0)) {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
            } else {
                List<String> sortOrderList = null;
                List<Sort.Order> soList = null;

                if (currentPage == null)
                    currentPage = 0;
                if (pageSize == null)
                    pageSize = Integer.MAX_VALUE;
                if (sortOrders != null && sortOrders.trim().length() > 0) {
                    sortOrderList = Arrays.asList(sortOrders.split(","));

                    //1. set sort orders and sort
                    final List<Sort.Order> temp_SOList = new ArrayList<Sort.Order>();
                    sortOrderList.forEach((sortOrder) -> {
                        final String[] sortMetadata = sortOrder.split(" ");
                        final String propertyName = sortMetadata[0];
                        final String propertyOrder = (sortMetadata.length > 1) ? sortMetadata[1].toUpperCase() : "ASC";

                        temp_SOList.add(new Sort.Order(Sort.Direction.fromString(propertyOrder), propertyName));
                    });

                    //copy the contents of temp sort order to original one
                    soList = new ArrayList<Sort.Order>();
                    soList.addAll(temp_SOList);
                }

                //create the pageable instance
                pageable = PageRequest.of(currentPage, pageSize, (soList == null ? Sort.unsorted() : Sort.by(soList)));
            }

            //call the Core API
            domainValueTypeList = domainValueTypeService.findAll("description like '%" + (description == null ? "" : description) + "%'", pageable);

            return domainValueTypeList;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Activates/Deactivates specific set of domain values by their Ids
     *
     * @param isToActivate
     * @param ids
     * @throws Exception
     */
    @Transactional(value = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void activateOrDeactivateDomainValuesById(boolean isToActivate, Integer... ids) throws Exception {
        //logger.info("--------Inside method 'activateOrDeactivateDomainValuesById'");
        try {
            if (ids == null || ids.length == 0) return;
            else {
                List<Integer> dvIdList = Arrays.asList(ids);
                List<DomainValue> domainValueDTOList = domainValueService.findByMultipleIds(dvIdList, false);

                if (domainValueDTOList == null || domainValueDTOList.isEmpty()) return;
                else {
                    domainValueDTOList.forEach((dvDTO) -> {
                        //set the 'isActive' flag as per input and update
                        dvDTO.setIsActive(isToActivate);
                        dvDTO = domainValueService.update(dvDTO);
                    });
                    if(logger.isInfoEnabled())
                    	logger.info("--------DomainValue with IDs: {} are '{}'", Arrays.deepToString(ids), (isToActivate) ? "activated" : "de-activated");
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Creates/updates the {@link DomainValue} entity
     *
     * @param domainValueVO
     * @return The created/updated {@link DomainValueVO} object
     * @throws Exception
     */
    @Transactional(transactionManager = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public DomainValueVO createOrUpdateDomainValue(DomainValueVO domainValueVO, String defaultLocale) throws Exception {
    	if(logger.isInfoEnabled())
    		logger.info("--------Inside method 'createOrUpdateDomainValue'");
        DomainValueVO returnDomainValueVO = null;
        DomainValue domainValueDTO = null;
        try {
            if (domainValueVO == null)
                throw new Exception("Exception occurred while creating/updating DomainValue as supplied DomainValueVO instance is null");
            else {
                domainValueDTO = new DomainValue();

                boolean isUpdate = !(domainValueVO.getId() == null || domainValueVO.getId() == 0);
                if (isUpdate) {
                    domainValueDTO.setId(domainValueVO.getId());
                    domainValueDTO.setCreatedOn(new Timestamp(domainValueVO.getCreatedOn().getTime()));
                } else {
                    domainValueDTO.setCreatedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
                }

                //domain value properties
                domainValueDTO.setCode(domainValueVO.getCode());
                domainValueDTO.setIsActive(domainValueVO.isActive());
                domainValueDTO.setIsDefault(domainValueVO.isDefault());
                domainValueDTO.setRankOrder(domainValueVO.getRankOrder());
                domainValueDTO.setDomainValueType(domainValueVO.getDomainValueType().getId());

                //create/update the domain value record
                if (isUpdate) {
                    domainValueDTO.setUpdatedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    
                     if (domainValueVO.getCreatedBy() == null || domainValueVO.getCreatedBy().trim().length() == 0
                            || stringToIntegerValue(domainValueVO.getCreatedBy().trim()) == null)
                        domainValueDTO.setCreatedBy(null);
                    else {
                        User createduserDTO = userService.findById(stringToIntegerValue(domainValueVO.getCreatedBy().trim()));
                        if (createduserDTO == null)
                            domainValueDTO.setCreatedBy(null);
                        else
                            domainValueDTO.setCreatedBy(createduserDTO.getId());
                    }
                    //get the user info
                    if (domainValueVO.getUpdatedBy() == null || domainValueVO.getUpdatedBy().trim().length() == 0
                            || stringToIntegerValue(domainValueVO.getUpdatedBy().trim()) == null)
                        domainValueDTO.setUpdatedBy(null);
                    else {
                        User userDTO = userService.findById(stringToIntegerValue(domainValueVO.getUpdatedBy().trim()));
                        if (userDTO == null)
                            domainValueDTO.setUpdatedBy(null);
                        else
                            domainValueDTO.setUpdatedBy(userDTO.getId());
                    }

                    //update DomainValue
                    domainValueDTO = domainValueService.update(domainValueDTO);
                } else {
                    domainValueDTO.setCreatedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));

                    //get the user info
                    if (domainValueVO.getCreatedBy() == null || domainValueVO.getCreatedBy().trim().length() == 0
                            || stringToIntegerValue(domainValueVO.getCreatedBy().trim()) == null)
                        domainValueDTO.setCreatedBy(null);
                    else {
                        User userDTO = userService.findById(stringToIntegerValue(domainValueVO.getCreatedBy().trim()));
                        if (userDTO == null)
                            domainValueDTO.setCreatedBy(null);
                        else
                            domainValueDTO.setCreatedBy(userDTO.getId());
                    }

                    //create DomainValue
                    domainValueDTO = domainValueService.create(domainValueDTO);
                }

                final DomainValue domainValueDTO_Final = domainValueDTO;

                //domain value descriptions
                //1. default locale
                createUpdateDomainValueDescription(domainValueDTO.getId(), domainValueVO.getDefaultLocale());

                //2. other locale
                if (domainValueVO.getDomainValueDescription() != null && !domainValueVO.getDomainValueDescription().isEmpty()) {
                    domainValueVO.getDomainValueDescription().forEach((dvDesc) -> {
                        createUpdateDomainValueDescription(domainValueDTO_Final.getId(), dvDesc);
                    });
                }

                //domain value relations
                if (domainValueVO.getDomainValueRelation() != null && !domainValueVO.getDomainValueRelation().isEmpty()) {
                    domainValueVO.getDomainValueRelation().forEach((dvRelt) -> {
                        createUpdateDomainValueRelation(domainValueDTO_Final.getId(), dvRelt);
                    });
                }
            }

            //fetch the saved domain value
            returnDomainValueVO = getDomainValueById(domainValueDTO.getId(), defaultLocale);
            if(logger.isInfoEnabled())
            	logger.info("--------Created/Updated DomainValue with ID: {}", returnDomainValueVO.getId());

            return returnDomainValueVO;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Fetches the {@link DomainValue} entity as a {@link DomainValueVO} object
     * by domain value id and optional default locale(applied on DV relations)
     *
     * @param domainValueId
     * @param defaultLocale
     * @return
     * @throws Exception
     */
    @Transactional(transactionManager = "TELUSAgentUIDBTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public DomainValueVO getDomainValueById(Integer domainValueId, String defaultLocale) throws Exception {
        //logger.info("--------Inside method 'getDomainValueById'");
        DomainValueVO domainValueVO = null;
        try {
            if (domainValueId == null)
                throw new Exception("Exception occurred while trying to fetch DomainValue by ID as ID supplied is null");

            DomainValue domainValueDTO = domainValueService.findById(domainValueId);
            if (domainValueDTO == null) ;
            else {
                //logger.info("--------DomainValue with ID: {} found!", domainValueId);
                domainValueVO = prepareDomainValueVOFromDTO(domainValueDTO, defaultLocale);
            }

            return domainValueVO;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Prepares {@link DomainValueVO} from supplied {@link DomainValue}
     *
     * @param domainValueDTO
     * @param defaultLocale
     * @return
     */
    private DomainValueVO prepareDomainValueVOFromDTO(DomainValue domainValueDTO, String defaultLocale) {
        //logger.info("--------Inside method 'prepareDomainValueVOFromDTO'");
        DomainValueVO domainValueVO = new DomainValueVO();

        //domain value properties
        domainValueVO.setId(domainValueDTO.getId());
        domainValueVO.setCode(domainValueDTO.getCode());
        domainValueVO.setRankOrder(domainValueDTO.getRankOrder());
        domainValueVO.setActive(domainValueDTO.getIsActive());
        domainValueVO.setDefault(domainValueDTO.getIsDefault());
        domainValueVO.setCreatedBy(String.valueOf(domainValueDTO.getCreatedBy()));
        domainValueVO.setCreatedOn(domainValueDTO.getCreatedOn());
        domainValueVO.setUpdatedBy(String.valueOf(domainValueDTO.getUpdatedBy()));
        domainValueVO.setUpdatedOn(domainValueDTO.getUpdatedOn());

        //get domain value type for this domain value
        DomainValueType domainValueTypeDTO = domainValueTypeService.findById(domainValueDTO.getDomainValueType());
        DomainValueTypeVO domainValueTypeVO = null;

        if (domainValueTypeDTO != null) {
            domainValueTypeVO = new DomainValueTypeVO();
            domainValueTypeVO.setId(domainValueTypeDTO.getId());
            domainValueTypeVO.setCode(domainValueTypeDTO.getCode());
            domainValueTypeVO.setDescription(domainValueTypeDTO.getDescription());
        }

        //attach domain value type to domain value
        domainValueVO.setDomainValueType(domainValueTypeVO);

        //get domain value type parents
        DomainValueTypeRelationVO domainValueTypeRelationVO = null;
        if (domainValueTypeDTO != null) {
            List<DomainValueTypeRelationship> dvRelationTypes =
                    domainValueTypeRelationshipService.findAll("domainValueTypeId=" + domainValueTypeDTO.getId(), null).toList();
            if (dvRelationTypes != null && !dvRelationTypes.isEmpty()) {
                DomainValueTypeRelationship domainValueTypeRelationshipDTO = dvRelationTypes.get(0);
                domainValueTypeRelationVO = new DomainValueTypeRelationVO();
                domainValueTypeRelationVO.setRelationPresent(true);

                //set parent1 DV type
                DomainValueType parent1DomainValueTypeDTO = domainValueTypeService.findById(domainValueTypeRelationshipDTO.getParentDomainValueTypeId1());
                if (parent1DomainValueTypeDTO != null) {
                    DomainValueTypeVO parent1DomainValueTypeVO = new DomainValueTypeVO();
                    parent1DomainValueTypeVO.setId(parent1DomainValueTypeDTO.getId());
                    parent1DomainValueTypeVO.setCode(parent1DomainValueTypeDTO.getCode());
                    parent1DomainValueTypeVO.setDescription(parent1DomainValueTypeDTO.getDescription());
                    domainValueTypeRelationVO.setParentDV1Type(parent1DomainValueTypeVO);
                } else {
                    domainValueTypeRelationVO.setParentDV1Type(null);
                }

                //set parent2 DV type
                DomainValueType parent2DomainValueTypeDTO = domainValueTypeService.findById(domainValueTypeRelationshipDTO.getParentDomainValueTypeId2());
                if (parent2DomainValueTypeDTO != null) {
                    DomainValueTypeVO parent2DomainValueTypeVO = new DomainValueTypeVO();
                    parent2DomainValueTypeVO.setId(parent2DomainValueTypeDTO.getId());
                    parent2DomainValueTypeVO.setCode(parent2DomainValueTypeDTO.getCode());
                    parent2DomainValueTypeVO.setDescription(parent2DomainValueTypeDTO.getDescription());
                    domainValueTypeRelationVO.setParentDV2Type(parent2DomainValueTypeVO);
                } else {
                    domainValueTypeRelationVO.setParentDV2Type(null);
                }
            } else {
                domainValueTypeRelationVO = new DomainValueTypeRelationVO();
                domainValueTypeRelationVO.setRelationPresent(false);
                domainValueTypeRelationVO.setParentDV1Type(null);
                domainValueTypeRelationVO.setParentDV2Type(null);
            }

            //attach domain value relation type to domain value
            domainValueVO.setDomainValueTypeRelation(domainValueTypeRelationVO);
        } else {
            //attach domain value relation type to domain value
            domainValueVO.setDomainValueTypeRelation(domainValueTypeRelationVO);
        }

        //get the domain value relations
        List<DomainValueRelationVO> dvRelationVOList = new ArrayList<DomainValueRelationVO>();
        List<DomainValueRelation> dvRelations =
                domainValueRelationService.findAll("domainValueId=" + domainValueDTO.getId(), null).toList();
        if (dvRelations != null && !dvRelations.isEmpty()) {
            dvRelations.forEach((dvReltn) -> {
                DomainValueRelationVO domainValueRelationVO = new DomainValueRelationVO();
                domainValueRelationVO.setId(dvReltn.getId());
                domainValueRelationVO.setDeleted(false);
                domainValueRelationVO.setParent1DomainValueId(dvReltn.getParentDomainValueId1());
                domainValueRelationVO.setParent2DomainValueId(dvReltn.getParentDomainValueId2());

                //get the respective descriptions for both parents in the domain value relation
                //1. for parent 1
                DomainValue parent1DV_DTO = domainValueService.findById(dvReltn.getParentDomainValueId1());
                if (parent1DV_DTO != null) {
                    domainValueRelationVO.setParent1DomainValueDescription(getDVDescriptionStrByDVIdAndLocale(parent1DV_DTO.getId(), defaultLocale));
                } else {
                    domainValueRelationVO.setParent1DomainValueDescription(null);
                }

                //2. for parent 2
                DomainValue parent2DV_DTO = domainValueService.findById(dvReltn.getParentDomainValueId2());
                if (parent2DV_DTO != null) {
                    domainValueRelationVO.setParent2DomainValueDescription(getDVDescriptionStrByDVIdAndLocale(parent2DV_DTO.getId(), defaultLocale));
                } else {
                    domainValueRelationVO.setParent2DomainValueDescription(null);
                }

                //add this relation to list
                dvRelationVOList.add(domainValueRelationVO);
            });

            //attach this relation list to domain value
            domainValueVO.setDomainValueRelation(dvRelationVOList);
        } else {
            //attach this relation list to domain value
            domainValueVO.setDomainValueRelation(dvRelationVOList);
        }

        //get the domain value descriptions
        DomainValueDescriptionVO defaultLocaleDescriptionVO = new DomainValueDescriptionVO();
        DomainValueDescription domainValueDescriptionDTO = getDVDescriptionByDVIdAndLocale(domainValueDTO.getId(), defaultLocale);
        if (domainValueDescriptionDTO != null) {
            defaultLocaleDescriptionVO.setId(domainValueDescriptionDTO.getId());
            defaultLocaleDescriptionVO.setDeleted(false);
            defaultLocaleDescriptionVO.setDescription(domainValueDescriptionDTO.getDescription());
            defaultLocaleDescriptionVO.setLocale(domainValueDescriptionDTO.getLocale());
        }

        //attach to domain value
        domainValueVO.setDefaultLocale(defaultLocaleDescriptionVO);

        //other locale
        List<DomainValueDescriptionVO> dvDescriptionVOList = new ArrayList<DomainValueDescriptionVO>();
        List<DomainValueDescription> dvDescriptions =
                domainValueDescriptionService.findAll("domainValueId=" + domainValueDTO.getId() + " AND locale<>'" + ((defaultLocale == null || defaultLocale.trim().length() == 0) ? "en" : defaultLocale) + "'", null).toList();
        if (dvDescriptions != null && !dvDescriptions.isEmpty()) {
            dvDescriptions.forEach((dvDesc) -> {
                DomainValueDescriptionVO domainValueDescriptionVO = new DomainValueDescriptionVO();
                domainValueDescriptionVO.setId(dvDesc.getId());
                domainValueDescriptionVO.setDeleted(false);
                domainValueDescriptionVO.setDescription(dvDesc.getDescription());
                domainValueDescriptionVO.setLocale(dvDesc.getLocale());

                //add to description list
                dvDescriptionVOList.add(domainValueDescriptionVO);
            });

            //attach to domain value
            domainValueVO.setDomainValueDescription(dvDescriptionVOList);
        } else {
            //attach to domain value
            domainValueVO.setDomainValueDescription(dvDescriptionVOList);
        }
        if(logger.isInfoEnabled())
        	logger.info("--------DomainValueVO instance from DomainValue object created with DomainValue ID: {}", domainValueDTO.getId());

        return domainValueVO;
    }

    /**
     * Gets the domain value description based on the DV Id & locale supplied
     *
     * @param domainValueId
     * @param locale
     * @return
     */
    public String getDVDescriptionStrByDVIdAndLocale(Integer domainValueId, String locale) {
        //logger.info("--------Inside method 'getDVDescriptionStrByDVIdAndLocale'");
        String description = null;

        if (domainValueId == null || domainValueId == 0) ;
        else {
            locale = (locale == null || locale.trim().length() == 0) ? "en" : locale;
            List<DomainValueDescription> dvDescriptions =
                    domainValueDescriptionService.findAll("domainValueId=" + domainValueId + " AND locale='" + locale + "'", null).toList();
            if (dvDescriptions != null && !dvDescriptions.isEmpty()) {
                DomainValueDescription domainValueDescriptionDTO = dvDescriptions.get(0);
                description = domainValueDescriptionDTO.getDescription();
            } else ;
        }

        return description;
    }

    /**
     * Get the {@link DomainValueDescription} based on the DV Id & locale supplied
     *
     * @param domainValueId
     * @param locale
     * @return
     */
    private DomainValueDescription getDVDescriptionByDVIdAndLocale(Integer domainValueId, String locale) {
        //logger.info("--------Inside method 'getDVDescriptionByDVIdAndLocale'");
        DomainValueDescription domainValueDescriptionDTO = null;

        if (domainValueId == null || domainValueId == 0) ;
        else {
            locale = (locale == null || locale.trim().length() == 0) ? "en" : locale;
            List<DomainValueDescription> dvDescriptions =
                    domainValueDescriptionService.findAll("domainValueId=" + domainValueId + " AND locale='" + locale + "'", null).toList();
            if (dvDescriptions != null && !dvDescriptions.isEmpty()) {
                domainValueDescriptionDTO = dvDescriptions.get(0);
            } else ;
        }

        return domainValueDescriptionDTO;
    }

    /**
     * Creates/updates the {@link DomainValueDescription} entity
     *
     * @param domainValueId
     * @param domainValueDescriptionVO
     * @return
     */
    private DomainValueDescription createUpdateDomainValueDescription(Integer domainValueId, DomainValueDescriptionVO domainValueDescriptionVO) {
    	if(logger.isInfoEnabled())
    		logger.info("--------Inside method 'createUpdateDomainValueDescription'");
        DomainValueDescription domainValueDescriptionDTO = null;

        if ((domainValueDescriptionVO.getId() != null && domainValueDescriptionVO.getId() != 0) &&
                domainValueDescriptionVO.isDeleted() != null && domainValueDescriptionVO.isDeleted().booleanValue()) {
            domainValueDescriptionService.delete(domainValueDescriptionVO.getId());
        } else {
            if (domainValueDescriptionVO.isDeleted() != null && domainValueDescriptionVO.isDeleted().booleanValue()) ;
            else {
                domainValueDescriptionDTO = new DomainValueDescription();
                if (domainValueDescriptionVO.getId() != null && domainValueDescriptionVO.getId() != 0)
                    domainValueDescriptionDTO.setId(domainValueDescriptionVO.getId());

                domainValueDescriptionDTO.setDomainValueId(domainValueId);
                domainValueDescriptionDTO.setDescription(domainValueDescriptionVO.getDescription());
                domainValueDescriptionDTO.setLocale(domainValueDescriptionVO.getLocale());

                if (domainValueDescriptionVO.getId() != null && domainValueDescriptionVO.getId() != 0)
                    domainValueDescriptionDTO = domainValueDescriptionService.update(domainValueDescriptionDTO);
                else
                    domainValueDescriptionDTO = domainValueDescriptionService.create(domainValueDescriptionDTO);
                if(logger.isInfoEnabled())
                	logger.info("--------Created/Updated DomainValueDescription with ID: {}", domainValueDescriptionDTO.getId());
            }
        }

        return domainValueDescriptionDTO;
    }

    /**
     * Creates/updates the {@link DomainValueRelation} entity
     *
     * @param domainValueId
     * @param domainValueRelationVO
     * @return
     */
    private DomainValueRelation createUpdateDomainValueRelation(Integer domainValueId, DomainValueRelationVO domainValueRelationVO) {
    	if(logger.isInfoEnabled())
    		logger.info("--------Inside method 'createUpdateDomainValueRelation'");
        DomainValueRelation domainValueRelationDTO = null;

        if ((domainValueRelationVO.getId() != null && domainValueRelationVO.getId() != 0) &&
                domainValueRelationVO.isDeleted() != null && domainValueRelationVO.isDeleted().booleanValue()) {
            domainValueRelationService.delete(domainValueRelationVO.getId());
        } else {
            if (domainValueRelationVO.isDeleted() != null && domainValueRelationVO.isDeleted().booleanValue())
            	if(logger.isInfoEnabled())
            		logger.info("Skip DV relation record");
            else {
                domainValueRelationDTO = new DomainValueRelation();
                if (domainValueRelationVO.getId() != null && domainValueRelationVO.getId() != 0)
                    domainValueRelationDTO.setId(domainValueRelationVO.getId());

                domainValueRelationDTO.setDomainValueId(domainValueId);
                domainValueRelationDTO.setParentDomainValueId1((domainValueRelationVO.getParent1DomainValueId() == null || domainValueRelationVO.getParent1DomainValueId() == 0) ? null : domainValueRelationVO.getParent1DomainValueId());
                domainValueRelationDTO.setParentDomainValueId2((domainValueRelationVO.getParent2DomainValueId() == null || domainValueRelationVO.getParent2DomainValueId() == 0) ? null : domainValueRelationVO.getParent2DomainValueId());

                if (domainValueRelationVO.getId() != null && domainValueRelationVO.getId() != 0) {
                    domainValueRelationDTO = domainValueRelationService.update(domainValueRelationDTO);
                } else {
                    domainValueRelationDTO = domainValueRelationService.create(domainValueRelationDTO);
                }
                if(logger.isInfoEnabled())
                	logger.info("--------Created/Updated DomainValueRelation with ID: {}", domainValueRelationDTO.getId());
            }
        }

        return domainValueRelationDTO;
    }

    /**
     * Converts String to Integer value if possible otherwise returns null
     *
     * @param value
     * @return
     */
    private Integer stringToIntegerValue(String value) {
        try {
            Integer intVal = null;
            intVal = Integer.parseInt(value);
            return intVal;
        } catch (Exception e) {
            return null;
        }
    }

}
