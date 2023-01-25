package com.fico.core.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.core.util.CommonUtils;
import com.fico.dmp.context.DMPContext;
import com.fico.dmp.telusagentuidb.User;
import com.fico.dmp.telusagentuidb.service.UserService;
import com.fico.ps.model.CustomPageDataWrapper;
import com.fico.pscomponent.handlers.DMPAuthenticationHandler;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;

@Service("facade.LookupServiceBS")
public class LookupFileServiceBS {

	private static final Logger logger = LoggerFactory.getLogger(LookupFileServiceBS.class);
	private static final String environment = DMPContext.getDmpEnvironment()==null?"design":DMPContext.getDmpEnvironment();
    
	@Autowired
    @Qualifier("TELUSAgentUIDB.UserService")
    private UserService userService;
	
	@Autowired
	private DMPAuthenticationHandler dmpAuthenticationHandler;

	@Autowired
	@Qualifier("core.CommonUtils")
	private CommonUtils commonUtils;

	/* Create lookup sample url : {{env_hostname}}/lookup?env=design */
	private static final String CREATE_LOOKUP_FILE_URL_TEMPLATE = "%slookup?env=%s";

	/*
	 * List lookup sample url :
	 * {{env_hostname}}/lookups?sortBy=name&orderBy=asc&env=design
	 */
	private static final String LIST_LOOKUP_FILE_URL_TEMPLATE = "%s/lookups?env=%s%s";

	/*
	 * Get lookup metadata sample url :
	 * {{env_hostname}}/lookup/{{lookup_id}}/metadata?env=design
	 */
	private static final String GET_LOOKUP_FILE_METADATA_URL_TEMPLATE = "%s/lookup/%s/metadata?env=%s";

	/*
	 * Update lookup file sample url:
	 * {{env_hostname}}/lookup/{{lookup_id}}?env=design
	 */
	private static final String UPDATE_LOOKUP_FILE_URL_TEMPLATE = "%s/lookup/%s?env=%s";

	/*
	 * Get Lookup File By Id url
	 * 
	 * {{env_hostname}}/lookup/{{lookup_id}}
	 */

	private static final String GET_LOOKUP_FILE_URL_TEMPLATE = "%s/lookup/%s?env=%s";

	private String getCreateLookupFileURL(String processURL, String currentEnvironment) {

		String finalCreateLookupFileURL = String.format(LookupFileServiceBS.CREATE_LOOKUP_FILE_URL_TEMPLATE, processURL,
				currentEnvironment);
		return finalCreateLookupFileURL;

	}

	private String getListAllLookupFileURL(String processURL, String currentEnvironment, String queryText) throws Exception {

		String finalListAllLookupFileURL = String.format(LookupFileServiceBS.LIST_LOOKUP_FILE_URL_TEMPLATE, processURL,
				currentEnvironment, queryText);
		
		return finalListAllLookupFileURL;

	}

	private String getLookupFileMetadataURL(String processURL, String lookupId, String currentEnvironment) {

		String finalLookupFileMetadataURL = String.format(LookupFileServiceBS.GET_LOOKUP_FILE_METADATA_URL_TEMPLATE,
				processURL, lookupId, currentEnvironment);
		return finalLookupFileMetadataURL;

	}

	private String getUpdateLookupFileURL(String processURL, String lookupId, String currentEnvironment) {

		String finalUpdateLookupFileURL = String.format(LookupFileServiceBS.UPDATE_LOOKUP_FILE_URL_TEMPLATE, processURL,
				lookupId, currentEnvironment);
		return finalUpdateLookupFileURL;

	}

	private String getLookupFileByIdURL(String processURL, String lookupId, String currentEnvironment) {

		String finalGetLookupFileURL = String.format(LookupFileServiceBS.GET_LOOKUP_FILE_URL_TEMPLATE, processURL,
				lookupId, currentEnvironment);

		return finalGetLookupFileURL;
	}

	private Boolean isExistingLookupFileName(String name) {
		// TODO

		return false;
	}

	public CreateUpdateLookupFileResponseWrapper createLookupFile(String identifier ,String name, String description,
			MultipartFile excelFile, String createdBy,String updatedBy) throws Exception, IOException {

		final String processURL = commonUtils.getProcessURL();

		String jwtToken = dmpAuthenticationHandler.getJWTToken();
		String endpointURL;
																			
		Map<String,String> searchKeys= new HashMap<>();
		searchKeys.put("name", name);
		
		final JSONObject requestJson = new JSONObject();
		if(!((identifier==null) || (identifier.trim().length()==0)))
		{
			requestJson.put("identifier", identifier);
			
		}
		if(!((createdBy==null) || (createdBy.trim().length()==0)))
		{
			requestJson.put("createdBy", createdBy);
			
		}
		if(!((updatedBy==null) || (updatedBy.trim().length()==0)))
		{
			requestJson.put("updatedBy", updatedBy);
			
		}
		
		requestJson.put("name", name);
		requestJson.put("description", description);
		requestJson.put("status", "Active");
		requestJson.put("fileName", excelFile.getOriginalFilename());
		requestJson.put("searchkeys", searchKeys );
		
		
		
		

//		StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		String boundary = "---------------"+UUID.randomUUID().toString();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		
	    builder.setBoundary(boundary);
	    builder.addBinaryBody("lookupFile", excelFile.getInputStream(), ContentType.create("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), excelFile.getOriginalFilename());
	   

	 
	   
		builder.addBinaryBody("request", requestJson.toString().getBytes(), ContentType.APPLICATION_JSON,"blob");

		HttpEntity entity = builder.build();
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse output=null;
		if((identifier==null) || (identifier.trim().length()==0))
		{
			//Create mode
			endpointURL= this.getCreateLookupFileURL(processURL, environment);
			HttpPost httpRequest = new HttpPost(endpointURL);
			httpRequest.addHeader("Accept", "*/*");
			httpRequest.addHeader("Authorization", "Bearer " + jwtToken);
			httpRequest.addHeader("Content-Type", "multipart/form-data"+";boundary="+boundary);
			httpRequest.setEntity(entity);
			if(logger.isWarnEnabled())
				logger.warn("Invoking manage lookup- create service...");
			output = client.execute(httpRequest);
		}
		else
		{
			//Edit mode
			endpointURL= this.getUpdateLookupFileURL(processURL, identifier, environment);
			HttpPut  httpPutRequest= new HttpPut(endpointURL);
			httpPutRequest.addHeader("Accept", "*/*");
			httpPutRequest.addHeader("Authorization", "Bearer " + jwtToken);
			httpPutRequest.addHeader("Content-Type", "multipart/form-data"+";boundary="+boundary);
			httpPutRequest.setEntity(entity);
			output = client.execute(httpPutRequest);
		}
		if(logger.isWarnEnabled())
			logger.warn("Invoked Service. Status code: {}", output.getStatusLine().getStatusCode());
		String res = commonUtils.readResponse(output.getEntity().getContent());
		ObjectMapper mapper = new ObjectMapper();
		Reader reader = new StringReader(res);
		JsonNode root = mapper.readValue(reader, JsonNode.class);
		
		CreateUpdateLookupFileResponseWrapper response= new CreateUpdateLookupFileResponseWrapper() ;
		
		if ((output.getStatusLine().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) || (output.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value()) ) {
			JsonNode errorNode = root.path("error");
			String errorString = errorNode.toPrettyString();
			response.setError(mapper.readValue(errorString, new TypeReference<Error>() {})) ;
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -"+output.getStatusLine().getStatusCode());
		}
		
		else
		{
			JsonNode content = root.path("content");
			String contentString = content.toPrettyString();
			if(logger.isWarnEnabled())
				logger.warn("content string  :" + contentString);
			response = mapper.readValue(contentString, new TypeReference<CreateUpdateLookupFileResponseWrapper>() {});
		}

		if(logger.isWarnEnabled())
		{
			logger.warn("response :" + res);
			logger.warn("----" + output.toString());
		}

		
		return response;

	}

	public CustomPageDataWrapper<ListLookupFileResponseWrapper> getLookupFileList(Map<String,String> queryParams) throws ClientProtocolException, IOException, Exception
	{
		
		CustomPageDataWrapper<ListLookupFileResponseWrapper> response= new CustomPageDataWrapper<>();
		
		ArrayList<ListLookupFileResponseWrapper> result= new ArrayList<>();
		
		String queryString = mapToQueryString(queryParams);
		final String processURL = commonUtils.getProcessURL();

		String jwtToken = dmpAuthenticationHandler.getJWTToken();
		String endpointURL=this.getListAllLookupFileURL(processURL, environment,queryString);
		
		if(logger.isInfoEnabled())
			logger.info("======In getLookupFileList=======Endpoint URL:"
				+ endpointURL +"dmpEnvironment:"+environment);

	
//		HttpEntity entity = builder.build();
		HttpClient client = HttpClientBuilder.create().build();

		HttpGet httpRequest = new HttpGet(endpointURL);

		httpRequest.addHeader("Accept", "*/*");
		httpRequest.addHeader("Authorization", "Bearer " + jwtToken);
	
		if(logger.isWarnEnabled())
			logger.warn("Invoking manage lookup- get lookup file list service...");
		HttpResponse output = client.execute(httpRequest);
		if(logger.isWarnEnabled())
			logger.warn("Invoked Service. Status code: {}", output.getStatusLine().getStatusCode());
		if (output.getStatusLine().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("500", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -500");
		}
		if (output.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("400", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -400");
		}

		String res = commonUtils.readResponse(output.getEntity().getContent());
		if(logger.isWarnEnabled())
		{
			logger.warn("response :"+res);
			logger.warn("----" + output.toString());
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		Reader reader = new StringReader(res);
		JsonNode root = mapper.readValue(reader, JsonNode.class);
		loadValuesFromPageInfo(response,fetchPageInfo(root));
		JsonNode content = root.path("content");
		String contentString= content.toPrettyString();
		if(logger.isWarnEnabled())
			logger.warn("content string  :"+contentString);
		result= mapper.readValue(contentString, new TypeReference<ArrayList<ListLookupFileResponseWrapper>>() {});
		
		for (int i=0; i<result.size();  i++)
		{
			try
			{
			User createduserDTO = userService.getByUserId(result.get(i).getCreatedBy().trim());
			User updateduserDTO = userService.getByUserId(result.get(i).getUpdatedBy().trim());
            if (createduserDTO != null)
            	result.get(i).setCreatedBy(getNameString(createduserDTO));
            
            if (updateduserDTO != null)
            	result.get(i).setUpdatedBy(getNameString(updateduserDTO));
			}
			catch(EntityNotFoundException ex)
			{
				
				logger.debug("Entity not found :"+ex.getMessage());
			}
            
			
		}
		
//		ObjectReader or= mapper.readerForArrayOf(ListLookupFileResponseWrapper[].class);
//		result = or.readValue(content);
		if(logger.isInfoEnabled())
			logger.info("Get Lookup file response :" + result.toString());
        response.setPageContent(result);
		return response;
		
	}
	
	public String getNameString(User user)
	{
		
		return (user.getFirstName()+" "+user.getLastName());
		
		
	}

	public ListLookupMetadataResponseWrapper getLookFileMetadata(String identifier) throws Exception {

		ListLookupMetadataResponseWrapper result = new ListLookupMetadataResponseWrapper();

		final String processURL = commonUtils.getProcessURL();

		String jwtToken = dmpAuthenticationHandler.getJWTToken();
		
		String endpointURL = this.getLookupFileMetadataURL(processURL, identifier, environment);
		if(logger.isInfoEnabled())
			logger.info("======In getLookFileMetadata=======Endpoint URL:" + endpointURL+ " dmpEnvironment:"+environment);

//		HttpEntity entity = builder.build();
		HttpClient client = HttpClientBuilder.create().build();

		HttpGet httpRequest = new HttpGet(endpointURL);

		httpRequest.addHeader("Accept", "*/*");
		httpRequest.addHeader("Authorization", "Bearer " + jwtToken);
		HttpResponse output = client.execute(httpRequest);
		if(logger.isWarnEnabled())
			logger.warn("Invoked Service. Status code: {}", output.getStatusLine().getStatusCode());
		if (output.getStatusLine().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("500", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -500");
		}
		if (output.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("400", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -400");
		}

		String res = commonUtils.readResponse(output.getEntity().getContent());
		if(logger.isWarnEnabled())
		{
			logger.warn("response :" + res);
			logger.warn("----" + output.toString());
		}

		ObjectMapper mapper = new ObjectMapper();
		Reader reader = new StringReader(res);
		JsonNode root = mapper.readValue(reader, JsonNode.class);
		JsonNode content = root.path("content");
		String contentString = content.toPrettyString();
		if(logger.isWarnEnabled())
			logger.warn("content string  :" + contentString);
		result.setSheetData(mapper.readValue(contentString, new TypeReference<ArrayList<SheetData>>() {
		}));

//		ObjectReader or= mapper.readerForArrayOf(ListLookupFileResponseWrapper[].class);
//		result = or.readValue(content);
		if(logger.isInfoEnabled())
			logger.info("Get Lookup metadata response :" + result.toString());

		return result;

	}
	
	public LookupFileInfoWrapper getLookupFileInfoById(String identifier) throws Exception {
		LookupFileInfoWrapper result= new LookupFileInfoWrapper();
		final String processURL = commonUtils.getProcessURL();

		String jwtToken = dmpAuthenticationHandler.getJWTToken();
		String endpointURL = this.getLookupFileByIdURL(processURL, identifier, environment);

//		HttpEntity entity = builder.build();
		HttpClient client = HttpClientBuilder.create().build();

		HttpGet httpRequest = new HttpGet(endpointURL);

		httpRequest.addHeader("Accept", "*/*");
		httpRequest.addHeader("Authorization", "Bearer " + jwtToken);
		if(logger.isWarnEnabled())
			logger.warn("Invoking manage lookup- get lookup file metadata service...");
		HttpResponse output = client.execute(httpRequest);
		if(logger.isWarnEnabled())
			logger.warn("Invoked Service. Status code: {}", output.getStatusLine().getStatusCode());
		if (output.getStatusLine().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("500", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -500");
		}
		if (output.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("400", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -400");
		}

		String res = commonUtils.readResponse(output.getEntity().getContent());
		if(logger.isWarnEnabled())
		{
			logger.warn("response :" + res);
			logger.warn("----" + output.toString());
		}
		ObjectMapper mapper = new ObjectMapper();
		Reader reader = new StringReader(res);
		JsonNode root = mapper.readValue(reader, JsonNode.class);
	
		JsonNode content = root.path("content");
		String contentString= content.toPrettyString();
		if(logger.isWarnEnabled())
			logger.warn("content string  :"+contentString);
		result= mapper.readValue(contentString, new TypeReference<LookupFileInfoWrapper>() {});

		if(logger.isInfoEnabled())
			logger.info("Get Lookup file info response :" + result.toString());
       
		return result;

        

	}

	
	

	public ResponseEntity<Object> getLookupFileById(String identifier) throws Exception {
		final String processURL = commonUtils.getProcessURL();

		String jwtToken = dmpAuthenticationHandler.getJWTToken();
		String endpointURL = this.getLookupFileByIdURL(processURL, identifier, environment);
		if(logger.isDebugEnabled())
			logger.debug("======In getLookFileById=======Endpoint URL:" + endpointURL + " dmpEnvironment:"+environment);

//		HttpEntity entity = builder.build();
		HttpClient client = HttpClientBuilder.create().build();

		HttpGet httpRequest = new HttpGet(endpointURL);

		httpRequest.addHeader("Accept", "*/*");
		httpRequest.addHeader("Authorization", "Bearer " + jwtToken);
		if(logger.isWarnEnabled())
			logger.warn("Invoking manage lookup- get lookup file metadata service...");
		HttpResponse output = client.execute(httpRequest);
		if(logger.isWarnEnabled())
			logger.warn("Invoked Service. Status code: {}", output.getStatusLine().getStatusCode());
		if (output.getStatusLine().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("500", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -500");
		}
		if (output.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("400", "Internal Server Error");
			if(logger.isWarnEnabled())
				logger.warn("Interal Error -400");
		}

		String res = commonUtils.readResponse(output.getEntity().getContent());
		if(logger.isWarnEnabled())
		{
			logger.warn("response :" + res);
			logger.warn("----" + output.toString());
		}
		ObjectMapper mapper = new ObjectMapper();
		Reader reader = new StringReader(res);
		JsonNode root = mapper.readValue(reader, JsonNode.class);
		JsonNode content = root.path("content").path("fileContent");
		String contentString = content.toString().replaceAll("\"", "");
		if(logger.isWarnEnabled())
			logger.warn("content string  :" + contentString);

		JsonNode filename = root.path("content").path("fileName");
		String fileNameString = filename.toString();

//		byte[] decode = Base64.getDecoder().decode(contentString);

		ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(fileNameString).build();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(contentDisposition);

//		byte[] bytes =contentString.getBytes();
		ByteArrayResource resource = new ByteArrayResource(contentString.getBytes());
		return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
				.headers(headers).body(resource);

	}

	private String mapToQueryString(Map<String, String> map) throws Exception{

		SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
		if (map != null && (!map.isEmpty())) {
         
			for (Entry<String, String> mapEntry : map.entrySet()) {
				if (mapEntry.getKey().equalsIgnoreCase("sortBy")) {
					queryBuilder.addCriteria(SearchCriteriaConstants.SORTBY, mapEntry.getValue());

				} else if (mapEntry.getKey().equalsIgnoreCase("orderBy")) {
					queryBuilder.addCriteria(SearchCriteriaConstants.ORDERBY, mapEntry.getValue());

				} else if (mapEntry.getKey().equalsIgnoreCase("status")) {
					queryBuilder.addCriteria(SearchCriteriaConstants.STATUS, mapEntry.getValue());

				} else if (mapEntry.getKey().equalsIgnoreCase("page")) {
					queryBuilder.addCriteria(SearchCriteriaConstants.PAGE, mapEntry.getValue().toString());

				} else if (mapEntry.getKey().equalsIgnoreCase("pageSize")) {
					queryBuilder.addCriteria(SearchCriteriaConstants.PAGESIZE,  mapEntry.getValue().toString());

				} else if (mapEntry.getKey().toString().equalsIgnoreCase("pageNo")) {
					queryBuilder.addCriteria(SearchCriteriaConstants.PAGENO,  mapEntry.getValue().toString());

				} else if (mapEntry.getKey().equalsIgnoreCase("searchText")) {
					queryBuilder.addCriteria(SearchCriteriaConstants.SEARCHTEXT, java.net.URLEncoder.encode(mapEntry.getValue(), StandardCharsets.UTF_8.name()));

				}

			}

		}
		
		return queryBuilder.build();

	}
	
	
	
	
	private PageInfo fetchPageInfo(JsonNode root)
	{
		PageInfo pageInfo = new PageInfo();
		if(root !=null)
		{
		String pageNoString= root.path("pageable").path("pageNumber").toString();
		String pageSizeString= root.path("pageable").path("pageSize").toString();
		String totalRecordCountString= root.path("totalElements").toString();
		if(logger.isInfoEnabled())
			logger.info("======In fetchPageInfo=======pageNo:" + pageNoString +" pageSizeString:"+pageSizeString+" totalRecordCountString:"+totalRecordCountString);
		
		Integer pageNo= Integer.valueOf(!pageNoString.equalsIgnoreCase("")?pageNoString:"0") ;
		Integer pageSize= Integer.valueOf(!pageSizeString.equalsIgnoreCase("")?pageSizeString:"0");
		Long totalRecordCount = Long.valueOf(!totalRecordCountString.equalsIgnoreCase("")?totalRecordCountString:"0");
		
		pageInfo.setPageNo(pageNo);
		pageInfo.setPageSize(pageSize);
		pageInfo.setTotalRecordCount(totalRecordCount);
		
		
		}
		
		return pageInfo;
		
	}
	
	private void loadValuesFromPageInfo (CustomPageDataWrapper wrapper,PageInfo info)
	{
		
		wrapper.setPageNumber(info.getPageNo());
		wrapper.setPageSize(info.getPageSize());
		wrapper.setTotalRecords(info.getTotalRecordCount());
	}

	/**
	 * Response wrapper inner classes for the various requirements defined below
	 * 
	 * 
	 * 
	 * @author RohanShetty
	 *
	 */
	
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class CreateUpdateLookupFileResponseWrapper {

		private String identifier;
		private String name;
		private String description;
		private String fileName;
		private Integer size;
		private Long createdOn;
		private Long updatedOn;
		private String createdBy;
		private String updatedBy;
		private String status;
		private Error error;
		
		
		
	    public Error getError() {
	        return error;
	    }
	    public void setError(Error error) {
	        this.error = error;
	    }

		public String getIdentifier() {
			return identifier;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public Integer getSize() {
			return size;
		}

		public void setSize(Integer size) {
			this.size = size;
		}

		public Long getCreatedOn() {
			return createdOn;
		}

		public void setCreatedOn(Long createdOn) {
			this.createdOn = createdOn;
		}

		public Long getUpdatedOn() {
			return updatedOn;
		}

		public void setUpdatedOn(Long updatedOn) {
			this.updatedOn = updatedOn;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public String getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String toString() {

			return "identifier:" + this.getIdentifier() + " name:" + this.getName() + " description:"
					+ this.getDescription() + " fileName:" + this.getFileName();
		}

	}

	public static class ListLookupFileResponseWrapper {

		private String id;
		private String name;
		private String description;
		private Integer size;
		private Long createdOn;
		private Long updatedOn;
		private String createdBy;
		private String updatedBy;
		private String status;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getSize() {
			return size;
		}

		public void setSize(Integer size) {
			this.size = size;
		}

		public Long getCreatedOn() {
			return createdOn;
		}

		public void setCreatedOn(Long createdOn) {
			this.createdOn = createdOn;
		}

		public Long getUpdatedOn() {
			return updatedOn;
		}

		public void setUpdatedOn(Long updatedOn) {
			this.updatedOn = updatedOn;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public String getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String toString() {

			return "identifier:" + this.getId() + " name:" + this.getName() + " description:" + this.getDescription()
					+ " size:" + this.getSize();
		}

	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)	
	public static class LookupFileInfoWrapper {
		
		private String identifier;
		private String name;
		private String description;
		private Integer size;
		private Long createdOn;
		private Long updatedOn;
		private String createdBy;
		private String updatedBy;
		private String status;
		private String fileName;
		
		private String fileContent;
		
		public String getFileContent() {
			return fileContent;
		}
		public void setFileContent(String fileContent) {
			this.fileContent = fileContent;
		}
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
		public Long getCreatedOn() {
			return createdOn;
		}
		public void setCreatedOn(Long createdOn) {
			this.createdOn = createdOn;
		}
		public Long getUpdatedOn() {
			return updatedOn;
		}
		public void setUpdatedOn(Long updatedOn) {
			this.updatedOn = updatedOn;
		}
		public String getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(String createdBy) {
		
			this.createdBy = createdBy;
		}
		public String getUpdatedBy() {
			return updatedBy;
		}
		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}

	public static class ColumnData {
		public String columnName;
		public String dataType;

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

	}

	public static class SheetData {
		public ArrayList<ColumnData> columnData;
		public String sheetName;

		public ArrayList<ColumnData> getColumnData() {
			return columnData;
		}

		public void setColumnData(ArrayList<ColumnData> columnData) {
			this.columnData = columnData;
		}

		public String getSheetName() {
			return sheetName;
		}

		public void setSheetName(String sheetName) {
			this.sheetName = sheetName;
		}

	}

	public static class ListLookupMetadataResponseWrapper {
        
		private String fileName;
		private String fileDescription;
		private ArrayList<SheetData> sheetData;
		private Integer numberOfElements;

		public ArrayList<SheetData> getSheetData() {
			return sheetData;
		}

		public void setSheetData(ArrayList<SheetData> sheetData) {
			this.sheetData = sheetData;
		}

		public Integer getNumberOfElements() {
			return numberOfElements;
		}

		public void setNumberOfElements(Integer numberOfElements) {
			this.numberOfElements = numberOfElements;
		}

		
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileDescription() {
			return fileDescription;
		}

		public void setFileDescription(String fileDescription) {
			this.fileDescription = fileDescription;
		}
	}

	enum SearchCriteriaConstants {
		SORTBY, ORDERBY, STATUS, PAGE, PAGESIZE, PAGENO, SEARCHTEXT

	}

	public class SearchQueryBuilder {

		private String sortBy;
		private String orderBy;
		private String status;
		private Integer page;
		private Integer pageSize;
		private Integer pageNo;
		private String searchText;

		private String getSortBy() {
			return sortBy;
		}

		private void setSortBy(String sortBy) {
			this.sortBy = sortBy;
		}

		private String getOrderBy() {
			return orderBy;
		}

		private void setOrderBy(String orderBy) {
			this.orderBy = orderBy;
		}

		private String getStatus() {
			return status;
		}

		private void setStatus(String status) {
			this.status = status;
		}

		private Integer getPage() {
			return page;
		}

		private void setPage(Integer page) {
			this.page = page;
		}

		private Integer getPageSize() {
			return pageSize;
		}

		private void setPageSize(Integer pageSize) {
			this.pageSize = pageSize;
		}

		private Integer getPageNo() {
			return pageNo;
		}

		private void setPageNo(Integer pageNo) {
			this.pageNo = pageNo;
		}

		private String getSearchText() {
			return searchText;
		}

		private void setSearchText(String searchText) {
			this.searchText = searchText;
		}

		public SearchQueryBuilder addCriteria(SearchCriteriaConstants parameter, String value) {
			switch (parameter) {
			case SORTBY:
				this.setSortBy(value);
				break;
			case ORDERBY:
				this.setOrderBy(value);
				break;
			case STATUS:
				this.setStatus(value);
				break;
			case PAGE:
				this.setPage(Integer.parseInt(value));
				break;
			case PAGESIZE:
				this.setPageSize(Integer.parseInt(value));
				break;
			case PAGENO:
				this.setPageNo(Integer.parseInt(value));
				break;
			case SEARCHTEXT:
				this.setSearchText(value);
				break;
			}

			return this;
		}

		public String build() {
			StringBuilder queryString = new StringBuilder();
			if (sortBy != null) {
				queryString.append("&sortBy=" + this.getSortBy());
			}
			if (orderBy != null) {
				queryString.append("&orderBy=" + this.getOrderBy());
			}
			if (status != null) {
				queryString.append("&status=" + this.getStatus());
			}
			if (page != null) {
				queryString.append("&page=" + this.getPage());
			}
			if (pageSize != null) {
				queryString.append("&pageSize=" + this.getPageSize());
			}
			if (pageNo != null) {
				queryString.append("&pageNo=" + this.getPageNo());
			}
			if (searchText != null) {
				queryString.append("&searchText=" + this.getSearchText());
			}

			return queryString.toString();

		}
	}
	
	

    
    
    
    public class PageInfo {
    	
    	private Integer pageSize;
    	private Long totalRecordCount;
    	private Integer pageNo;
    	
    	public Integer getPageSize() {
			return pageSize;
		}
		public void setPageSize(Integer pageSize) {
			this.pageSize = pageSize;
		}
		public Long getTotalRecordCount() {
			return totalRecordCount;
		}
		public void setTotalRecordCount(Long totalRecordCount) {
			this.totalRecordCount = totalRecordCount;
		}
		public Integer getPageNo() {
			return pageNo;
		}
		public void setPageNo(Integer pageNo) {
			this.pageNo = pageNo;
		}
		
    	
    }
    
    public static class Error {
        private String code;
        private String timestamp;
        private String desc;
        private String message;
        private InnerError innerError;
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public InnerError getInnerError() {
            return innerError;
        }
        public void setInnerError(InnerError innerError) {
            this.innerError = innerError;
        }
    }

    public static class InnerError {
        private String code;
        private String message;
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
