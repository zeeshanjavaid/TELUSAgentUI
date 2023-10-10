package com.fico.telus.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.dmp.context.DMPContext;
import com.fico.pscomponent.util.PropertiesUtil;
import com.hazelcast.map.IMap;
import com.wavemaker.runtime.hazelcast.FawbAppHazelcastInstance;

import com.fico.telus.model.TelusTokenResponse;

@Service
public class TelusAPIConnectivityService {
	
	private static final Logger logger = LoggerFactory.getLogger(TelusAPIConnectivityService.class);
	
	private static final String TELUS_API_CLIENT_ID = "TELUS_API_CLIENT_ID";
	
	private static final String TELUS_API_SECRET = "TELUS_API_SECRET";
	
	private static final String TELUS_AUTH_TOKEN_URL = "TELUS_AUTH_TOKEN_URL";
	
	private static final String TELUS_AUTH_TOKEN_CACHE_MAP = "TELUS_AUTH_TOKEN_CACHE_MAP";

	private static final String TELUS_AUTH_TOKEN = "TELUS_AUTH_TOKEN";
	
	private static final String TELUS_AUTH_TOKEN_CACHE_EXPIRE_IN_SEC = "TELUS_AUTH_TOKEN_CACHE_EXPIRE_IN_SEC";
	
	@Autowired
	private FawbAppHazelcastInstance fawbAppHazelcastInstance;
	
	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper mapper;
	
	private String clientId;

	private Integer cacheExpireInSec;

	private String secret;

	private String tokenURL;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	

	
	/**
	 * This method gets the Telus token for emitting the outbound event
	 * @return String
	 * @throws Exception
	 */
	public String getTelusToken (String scope) throws Exception {
		
		String token = getToken();
		if (!Objects.isNull(token)) {
    		return token;
    	}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("client_id", this.clientId);
		map.add("client_secret", this.secret);
		map.add("grant_type", "client_credentials");
		map.add("scope", scope);
		
		logger.info(":::::::::::::::::::::Before calling Telus token URL:::::::::::::::::::::");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		String url = this.tokenURL;
		logger.info("::::::::::::::::::::: Telus token URL:::::::::::::::::::::"+url);
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		String result = "";
		
		logger.info("::::::::::::::::::::: Telus token API eesponse:::::::::::::::::::::"+ response);

		if (response != null) {

			HttpStatus statusCode = response.getStatusCode();
			//TODO: Uncomment and create the object TelusTokenResponse object according to TelUs token response
			if (statusCode == HttpStatus.OK) {
				
				 result = response.getBody(); 
				 TelusTokenResponse telusTokenResponse = mapper.readValue(result, com.fico.telus.model.TelusTokenResponse.class);
				 token = telusTokenResponse.getAccess_token();
				 this.cacheExpireInSec = Integer.parseInt(telusTokenResponse.getExpires_in());
				 registerToken(token, cacheExpireInSec); 
				 
				 logger.info("::::::::Telus token :::::::::::::::::" + token);

			    
			} else {

				logger.info("::::::::Telus token API response status code:::::::::::::::::" + statusCode);
			}
		}
		return token;
	}

	public String getTokenURL() {
		return tokenURL;
	}
	
	private String getToken() {
		
		IMap<String, String> map = fawbAppHazelcastInstance.getMap(TELUS_AUTH_TOKEN_CACHE_MAP);
		if (map.containsKey(TELUS_AUTH_TOKEN)) {
			String token = map.get(TELUS_AUTH_TOKEN);
			return token;
		} 
		return null;
	}
	@PostConstruct
	public void init() {
		
		this.clientId = propertyValueFrom(TELUS_API_CLIENT_ID, "ac93e924-e669-4c21-a8c5-11945cee7ffd");
		this.secret = propertyValueFrom(TELUS_API_SECRET, "2abf5c9c-dd75-4709-963a-115e3e6c19057c59bc75-9672-4b7a-99a8-49ed10f661d9");		
		this.tokenURL = propertyValueFrom(TELUS_AUTH_TOKEN_URL, "https://apigw-st.telus.com/st/token");
		this.cacheExpireInSec = Integer.parseInt(propertyValueFrom(TELUS_AUTH_TOKEN_CACHE_EXPIRE_IN_SEC,"600"));
	}
	
	private String propertyValueFrom(String propertyName, String defaulValueIfNull)
    {
        String propertyValue = propertiesUtil.getPropertyValue(propertyName);
        if (propertyValue == null) {
            logger.info("property value is null, using default");
            propertyValue = defaulValueIfNull;
        }
        return propertyValue;
    }

	/**
	 * This method does the call to Telus API
	 * @param requestPayload
	 * @param endpointURL
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	public String executeTelusAPI(String requestPayload, String endpointURL, String httpMethod, String scope) throws Exception {
        StringEntity entity=null;
        String dmpEnvironment = DMPContext.getDmpEnvironment();
		String bearerToken = getTelusToken(scope);
		logger.info("::::::::requestPayload before calling Telus API:::::::::::::::::" + requestPayload);
        if(requestPayload != null){
		    entity = new StringEntity(requestPayload, ContentType.APPLICATION_JSON);
        }
		logger.info("::::::::::::::::::::: Telus telusEndpointURL:::::::::::::::::::::" + endpointURL);
		HttpResponse response = null;
		ResponseEntity<String> responseEntity=null;
		HttpHeaders headers=null;
		headers = new HttpHeaders();
		if(dmpEnvironment != null) {
			if (dmpEnvironment.toLowerCase().equals("staging")) {
				headers.add("env", "it01");
			}
		}
		switch (httpMethod) {

			case "POST":
	
				// HttpPost httpPost = new HttpPost(endpointURL);
				// httpPost.addHeader("Authorization", "Bearer " + bearerToken);
				// httpPost.setEntity(entity);
				// httpPost.addHeader("Content-Type", "application/json");
				// response = invokeTelusPostAPI(httpPost);
				headers.add("Authorization", "Bearer " + bearerToken);
				headers.add("Content-Type", "application/json");
				responseEntity= invokeTelusPostAPIWithRestTemp(requestPayload, headers,endpointURL);
				break;
	
			case "GET":
				// HttpGet httpGet = new HttpGet(endpointURL);
				// httpGet.addHeader("Authorization", "Bearer " + bearerToken);
				// httpGet.addHeader("Content-Type", "application/json");
				// response = invokeTelusGetAPI(httpGet);
				headers.add("Authorization", "Bearer " + bearerToken);
				headers.add("Content-Type", "application/json");
				responseEntity= invokeTelusGetAPIWithRestTemp(headers,endpointURL);
				break;
	
			case "PATCH":
				// HttpPatch httpPatch = new HttpPatch(endpointURL);
				// httpPatch.addHeader("Authorization", "Bearer " + bearerToken);
				// httpPatch.setEntity(entity);
				// httpPatch.addHeader("Content-Type", "application/json");
				// response = invokeTelusPatchAPI(httpPatch);
				
				headers.add("Authorization", "Bearer " + bearerToken);
				headers.add("Content-Type", "application/merge-patch+json");
				responseEntity= invokeTelusPatchAPIWithRestTemp(requestPayload,headers,endpointURL);
				break;
			default:
		}
		Integer statusCode = null;
		String result = null;
		HttpStatus httpStatus = responseEntity.getStatusCode();
		statusCode=httpStatus.value();
		if (responseEntity != null) {
			// Read the response
			result = responseEntity.getBody();
			logger.info("::::::::Telus outbound API For Header ::::::::::::::::" + responseEntity.getHeaders());

			if (statusCode == 200 || statusCode == 201 ) {
				logger.info("::::::::Telus outbound API response statusCode ::::::::::::::::" + statusCode);
				return result;
			} else {
				logger.error(
						"::::::::Telus outbound API failed response statusCode ::::######::::::::::::" + statusCode);

				throw new Exception("Telus API failed with statusCode : " + statusCode);
			}
		}
		return result;
	}
	
	
		private ResponseEntity<String> invokeTelusGetAPIWithRestTemp(HttpHeaders headers,String endpointURL ) {

     	logger.info(":::::::::::Before calling Telus API get execute method ::::::::::::::::::::");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
		return restTemplate.exchange(endpointURL, HttpMethod.GET, requestEntity, String.class);

	}
	
		private ResponseEntity<String> invokeTelusPostAPIWithRestTemp(String requestPayload, HttpHeaders headers,String endpointURL ) {

		RestTemplate restTemplate =new RestTemplate();

		HttpEntity<String> requestEntity = new HttpEntity<String>(requestPayload, headers);
		return restTemplate.exchange(endpointURL, HttpMethod.POST, requestEntity, String.class);

	}

	private ResponseEntity<String> invokeTelusPatchAPIWithRestTemp(String requestPayload,HttpHeaders headers,String endpointURL ) {

		RestTemplate restTemplate =new RestTemplate();
		
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		

		restTemplate.setRequestFactory(httpRequestFactory);

		HttpEntity<String> requestEntity = new HttpEntity<String>(requestPayload, headers);
		return restTemplate.exchange(endpointURL, HttpMethod.PATCH, requestEntity, String.class);

	}
	
	private String readResponse(HttpResponse response) throws Exception {
		StringBuffer result = new StringBuffer();

		if (response.getEntity() != null) {

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		}
		logger.warn("::::::::TelUs call Result in readresponse:::::::::::::" + result);
		return result.toString();
	}
	
	private HttpResponse invokeTelusGetAPI(HttpGet httpRequest) throws Exception {	
			
			logger.info(":::::::::::Before calling Telus API get execute method ::::::::::::::::::::");
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httpRequest);
			return response;		
	}
	
	private HttpResponse invokeTelusPostAPI(HttpPost httpRequest) throws Exception {
		logger.info(":::::::::::Before calling Telus API post execute method ::::::::::::::::::::");
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpRequest);
		return response;
	}
	private HttpResponse invokeTelusPatchAPI(HttpPatch httpRequest) throws Exception {
		
		logger.info(":::::::::::Before calling Telus API patch execute method ::::::::::::::::::::");
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpRequest);
		return response;
	}
	
	private void registerToken(String token, Integer expiresInSec) {
		logger.info("INVOKED: registerToken()");
		IMap<String, String> map = fawbAppHazelcastInstance.getMap(TELUS_AUTH_TOKEN);
		map.tryPut(TELUS_AUTH_TOKEN, token, expiresInSec, TimeUnit.SECONDS);
	}
	
	
	public ResponseEntity<String> executeTelusAPIAndGetResponseWithHeader(String requestPayload, String endpointURL, String httpMethod, String scope) throws Exception {
		StringEntity entity=null;
		String dmpEnvironment = DMPContext.getDmpEnvironment();
		String bearerToken = getTelusToken(scope);
		logger.info("::::::::requestPayload before calling Telus API:::::::::::::::::" + requestPayload);
		if(requestPayload != null){
			entity = new StringEntity(requestPayload, ContentType.APPLICATION_JSON);
		}
		logger.info("::::::::::::::::::::: Telus telusEndpointURL:::::::::::::::::::::" + endpointURL);
		HttpResponse response = null;
		Integer statusCode = null;
		String result = null;
		ResponseEntity<String> responseEntity=null;
		HttpHeaders headers=null;
		headers = new HttpHeaders();
		if(dmpEnvironment != null) {
			if (dmpEnvironment.toLowerCase().equals("staging")) {
				headers.add("env", "it01");
			}
		}
		headers.add("Authorization", "Bearer " + bearerToken);
		headers.add("Content-Type", "application/json");
		responseEntity= invokeTelusGetAPIWithRestTemp(headers,endpointURL);
		HttpStatus httpStatus = responseEntity.getStatusCode();
		statusCode=httpStatus.value();

		// Read the response
		//	result = responseEntity.getBody();
// 			HttpHeaders headers1=responseEntity.getHeaders();
// 					String li=headers1.getFirst("x-total-count");
					
// 				result = "{\"totalNumberOfElement\":"+Integer.parseInt(li)+",\"responseObjectList\":"+responseEntity.getBody()+"}";

// 					logger.info("::::::::Telus outbound API For  Parr Reports response ::::::::::::::::" +result);

			logger.info("::::::::Telus outbound API For  Parr Reports Header ::::::::::::::::" + responseEntity.getHeaders());
			if (statusCode == 200 || statusCode == 201 ) {
				logger.info("::::::::Telus outbound API response statusCode ::::::::::::::::" + statusCode);
				return responseEntity;
			} else {
				logger.error(
						"::::::::Telus outbound API failed response statusCode ::::######::::::::::::" + statusCode);

				throw new Exception("Telus API failed with statusCode : " + statusCode);

		}
	//	return responseEntity;
	}
	
	
	
}
