package com.fico.telus.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

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


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.pscomponent.util.PropertiesUtil;
import com.hazelcast.map.IMap;
import com.wavemaker.runtime.hazelcast.FawbAppHazelcastInstance;

import com.fico.telus.model.TelusTokenResponse;

@Service
public class TelusAPIConnectivityService {
	
	private static final Logger logger = LoggerFactory.getLogger(TelusAPIConnectivityService.class);
	
	private static final String TELUS_API_CLIENT_ID = "TELUS_API_CLIENT_ID";
	
	private static final String TELUS_API_SECRET = "TELUS_API_SECRET";
	
	private static final String TELUS_TOKEN_URL = "TELUS_TOKEN_URL";
	
	private static final String TELUS_TOKEN_CACHE_MAP = "TELUS_TOKEN_CACHE_MAP";

	private static final String TELUS_TOKEN = "TELUS_TOKEN";
	
	private static final String TELUS_TOKEN_CACHE_EXPIRE_IN_SEC = "TELUS_TOKEN_CACHE_EXPIRE_IN_SEC";
	
	@Autowired
	private FawbAppHazelcastInstance fawbAppHazelcastInstance;
	
	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper mapper;
	
	private String clientId;

	private long cacheExpireInSec;

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
				//  this.cacheExpireInSec = telusTokenResponse.getExpires_in();
				//  registerToken(telusTokenResponse.getAccess_token()); 
				 token = telusTokenResponse.getAccess_token();
				 
				 
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
		
		IMap<String, String> map = fawbAppHazelcastInstance.getMap(TELUS_TOKEN_CACHE_MAP);
		if (map.containsKey(TELUS_TOKEN)) {
			String token = map.get(TELUS_TOKEN);
			return token;
		} 
		return null;
	}
	@PostConstruct
	public void init() {
		
		this.clientId = propertyValueFrom(TELUS_API_CLIENT_ID, "ac93e924-e669-4c21-a8c5-11945cee7ffd");
		this.secret = propertyValueFrom(TELUS_API_SECRET, "2abf5c9c-dd75-4709-963a-115e3e6c19057c59bc75-9672-4b7a-99a8-49ed10f661d9");		
		this.tokenURL = propertyValueFrom(TELUS_TOKEN_URL, "https://apigw-st.telus.com/st/token");
		this.cacheExpireInSec = Long.parseLong(propertyValueFrom(TELUS_TOKEN_CACHE_EXPIRE_IN_SEC,"600"));
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
		String bearerToken = getTelusToken(scope);
		logger.info("::::::::requestPayload before calling Telus API:::::::::::::::::" + requestPayload);
        if(requestPayload != null){
		    entity = new StringEntity(requestPayload, ContentType.APPLICATION_JSON);
        }
		logger.info("::::::::::::::::::::: Telus telusEndpointURL:::::::::::::::::::::" + endpointURL);
		HttpResponse response = null;
		ResponseEntity<String> responseEntity=null;
		HttpHeaders headers=null;
		switch (httpMethod) {

			case "POST":
	
				// HttpPost httpPost = new HttpPost(endpointURL);
				// httpPost.addHeader("Authorization", "Bearer " + bearerToken);
				// httpPost.setEntity(entity);
				// httpPost.addHeader("Content-Type", "application/json");
				// response = invokeTelusPostAPI(httpPost);
				
				headers = new HttpHeaders();
				headers.add("Authorization", "Bearer " + bearerToken);
				headers.add("Content-Type", "application/json");
				responseEntity= invokeTelusPostAPIWithRestTemp(requestPayload, headers,endpointURL);
				break;
	
			case "GET":
				// HttpGet httpGet = new HttpGet(endpointURL);
				// httpGet.addHeader("Authorization", "Bearer " + bearerToken);
				// httpGet.addHeader("Content-Type", "application/json");
				// response = invokeTelusGetAPI(httpGet);
				
				 headers = new HttpHeaders();
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
				
				headers = new HttpHeaders();
				headers.add("Authorization", "Bearer " + bearerToken);
				headers.add("Content-Type", "application/json");
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

		HttpEntity<String> requestEntity = new HttpEntity<String>(requestPayload, headers);
		return restTemplate.exchange(endpointURL, HttpMethod.PUT, requestEntity, String.class);

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
}
