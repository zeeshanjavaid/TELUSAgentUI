package com.fico.telus.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.dmp.context.DMPContext;
import com.fico.pscomponent.util.PropertiesUtil;
import com.fico.telus.model.TelusTokenResponse;
import com.hazelcast.map.IMap;
import com.wavemaker.runtime.hazelcast.FawbAppHazelcastInstance;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TelusAPIConnectivityService {
    private static final Logger logger = LoggerFactory.getLogger(TelusAPIConnectivityService.class);

    private static final String TELUS_API_CLIENT_ID = "TELUS_API_CLIENT_ID";
    private static final String TELUS_API_CLIENT_SECRET = "TELUS_API_CLIENT_SECRET";
    private static final String TELUS_API_ENV = "TELUS_API_ENV";
    private static final String TELUS_AUTH_TOKEN_URL = "TELUS_AUTH_TOKEN_URL";
    private static final String TELUS_AUTH_TOKEN_CACHE_MAP = "TELUS_AUTH_TOKEN_CACHE_MAP";
    private static final String TELUS_AUTH_TOKEN = "TELUS_AUTH_TOKEN";
    private static final String TELUS_AUTH_TOKEN_CACHE_EXPIRE_IN_SEC = "TELUS_AUTH_TOKEN_CACHE_EXPIRE_IN_SEC";

    @Autowired
    private FawbAppHazelcastInstance fawbAppHazelcastInstance;

    @Autowired
    @Qualifier("customObjectMapper")
    private ObjectMapper mapper;

    @Autowired
    private PropertiesUtil propertiesUtil;

    private String telusApiClientId;
    private String telusApiClientSecret;
    private String telusApiEnv;
    private String telusTokenURL;
    private Integer cacheExpireInSec;
    private IMap<String, String> tokenCacheMap;

    @PostConstruct
    public void init() {
        this.telusApiClientId = propertiesUtil.getPropertyValue(TELUS_API_CLIENT_ID);
        this.telusApiClientSecret = propertiesUtil.getPropertyValue(TELUS_API_CLIENT_SECRET);
        this.telusApiEnv = propertiesUtil.getPropertyValue(TELUS_API_ENV);
        this.telusTokenURL = propertiesUtil.getPropertyValue(TELUS_AUTH_TOKEN_URL);
        String ttlStr = propertiesUtil.getPropertyValue(TELUS_AUTH_TOKEN_CACHE_EXPIRE_IN_SEC);
        this.cacheExpireInSec = Integer.parseInt(ttlStr != null ? ttlStr : "270");
        this.tokenCacheMap = fawbAppHazelcastInstance.getMap(TELUS_AUTH_TOKEN_CACHE_MAP);
    }

    /**
     * This method gets the Telus token for emitting the outbound event
     * @return String
     * @throws Exception
     */
    public String getTelusToken (String scope) throws Exception {
        String token = getToken(scope);
        if (!Objects.isNull(token)) {
            return token;
        } else {
            logger.info("::::::::::::::::::::: TOKEN CACHE NULL :::::::::::::::::::::");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("client_id", this.telusApiClientId);
        map.add("client_secret", this.telusApiClientSecret);
        map.add("grant_type", "client_credentials");
        map.add("scope", scope);
        logger.debug("::::::::::::::::::::: Before calling Telus token URL :::::::::::::::::::::");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = this.telusTokenURL;
        logger.debug("::::::::::::::::::::: Telus token URL ::::::::::::::::::::: ", url);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String result = "";
        logger.debug("::::::::::::::::::::: Telus token API response ::::::::::::::::::::: ", response);
        if (response != null) {
            HttpStatus statusCode = response.getStatusCode();
            //TODO: Uncomment and create the object TelusTokenResponse object according to TelUs token response
            if (statusCode == HttpStatus.OK) {
                result = response.getBody();
                TelusTokenResponse telusTokenResponse = mapper.readValue(result, com.fico.telus.model.TelusTokenResponse.class);
                token = telusTokenResponse.getAccess_token();
                this.cacheExpireInSec = Integer.parseInt(telusTokenResponse.getExpires_in());
                registerToken(scope, token, cacheExpireInSec);
                logger.debug("::::::::::::::::::::: Telus token ::::::::::::::::::::: ", token);
            } else {
                logger.error("::::::::::::::::::::: Telus token API response status code ::::::::::::::::::::: ", statusCode);
            }
        }
        return token;
    }

    public String getTelusTokenURL() {
        return telusTokenURL;
    }

    private void registerToken(String scope, String token, Integer expiresInSec) {
        tokenCacheMap.tryPut(scope, token, expiresInSec, TimeUnit.SECONDS);
    }
    // private void registerToken(String token, Integer expiresInSec) {
    //     logger.debug("INVOKED: registerToken()");
    //     IMap<String, String> map = fawbAppHazelcastInstance.getMap(TELUS_AUTH_TOKEN);
    //     map.tryPut(TELUS_AUTH_TOKEN, token, expiresInSec, TimeUnit.SECONDS);
    // }

    private String getToken(String scope) {
        if (tokenCacheMap.containsKey(scope)) {
            String token = tokenCacheMap.get(scope);
            return token;
        }
        return null;
    }
    // private String getToken() {
    //     IMap<String, String> map = fawbAppHazelcastInstance.getMap(TELUS_AUTH_TOKEN_CACHE_MAP);
    //     if (map.containsKey(TELUS_AUTH_TOKEN)) {
    //         String token = map.get(TELUS_AUTH_TOKEN);
    //         return token;
    //     }
    //     return null;
    // }

    /**
     * This method does the call to Telus API
     *
     * @param requestPayload
     * @param endpointURL
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public String executeTelusAPI(String requestPayload, String endpointURL, String httpMethod, String scope) throws Exception {
        StringEntity entity = null;
        String dmpEnvironment = DMPContext.getDmpEnvironment();
        String bearerToken = getTelusToken(scope);
        logger.debug("::::::::::::::::::::: requestPayload before calling Telus API ::::::::::::::::::::: ", requestPayload);
        if (requestPayload != null) {
            entity = new StringEntity(requestPayload, ContentType.APPLICATION_JSON);
        }
        logger.debug("::::::::::::::::::::: Telus telusEndpointURL ::::::::::::::::::::: ", endpointURL);
        HttpResponse response = null;
        ResponseEntity<String> responseEntity = null;
        HttpHeaders headers = null;
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + bearerToken);
        if (telusApiEnv != null) {
            headers.add("env", telusApiEnv);
        }
        switch (httpMethod) {
            case "POST":
                // HttpPost httpPost = new HttpPost(endpointURL);
                // httpPost.setEntity(entity);
                // httpPost.addHeader("Content-Type", "application/json");
                // response = invokeTelusPostAPI(httpPost);
                headers.add("Content-Type", "application/json");
                responseEntity = invokeTelusPostAPIWithRestTemp(requestPayload, headers, endpointURL);
                break;
            case "GET":
                // HttpGet httpGet = new HttpGet(endpointURL);
                // response = invokeTelusGetAPI(httpGet);
                responseEntity = invokeTelusGetAPIWithRestTemp(headers, endpointURL);
                break;
            case "PATCH":
                // HttpPatch httpPatch = new HttpPatch(endpointURL);
                // httpPatch.setEntity(entity);
                // httpPatch.addHeader("Content-Type", "application/merge-patch+json");
                // response = invokeTelusPatchAPI(httpPatch);
                headers.add("Content-Type", "application/merge-patch+json");
                responseEntity = invokeTelusPatchAPIWithRestTemp(requestPayload, headers, endpointURL);
                break;
            default:
        }
        Integer statusCode = null;
        String result = null;
        HttpStatus httpStatus = responseEntity.getStatusCode();
        statusCode = httpStatus.value();
        if (responseEntity != null) {
            // Read the response
            result = responseEntity.getBody();
            logger.debug("::::::::::::::::::::: Telus outbound API For Header ::::::::::::::::::::: ", responseEntity.getHeaders());
            if (statusCode == 200 || statusCode == 201) {
                logger.debug("::::::::::::::::::::: Telus outbound API response statusCode ::::::::::::::::::::: ", statusCode);
                return result;
            } else {
                logger.error("::::::::::::::::::::: Telus outbound API failed response statusCode ::::::::::::::::::::: ", statusCode);
                throw new Exception("Telus API failed with statusCode : " + statusCode);
            }
        }
        return result;
    }

    private ResponseEntity<String> invokeTelusGetAPIWithRestTemp(HttpHeaders headers, String endpointURL) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        return restTemplate.exchange(endpointURL, HttpMethod.GET, requestEntity, String.class);
    }

    private ResponseEntity<String> invokeTelusPostAPIWithRestTemp(String requestPayload, HttpHeaders headers, String endpointURL) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestPayload, headers);
        return restTemplate.exchange(endpointURL, HttpMethod.POST, requestEntity, String.class);
    }

    private ResponseEntity<String> invokeTelusPatchAPIWithRestTemp(String requestPayload, HttpHeaders headers, String endpointURL) {
        RestTemplate restTemplate = new RestTemplate();
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
        logger.warn("::::::::::::::::::::: Telus call result in read response ::::::::::::::::::::: ", result);
        return result.toString();
    }

    private HttpResponse invokeTelusGetAPI(HttpGet httpRequest) throws Exception {
        logger.debug("::::::::::::::::::::: Before calling Telus API get execute method :::::::::::::::::::::");
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(httpRequest);
        return response;
    }

    private HttpResponse invokeTelusPostAPI(HttpPost httpRequest) throws Exception {
        logger.debug("::::::::::::::::::::: Before calling Telus API post execute method :::::::::::::::::::::");
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(httpRequest);
        return response;
    }

    private HttpResponse invokeTelusPatchAPI(HttpPatch httpRequest) throws Exception {
        logger.debug("::::::::::::::::::::: Before calling Telus API patch execute method :::::::::::::::::::::");
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(httpRequest);
        return response;
    }

    public ResponseEntity<String> executeTelusAPIAndGetResponseWithHeader(String requestPayload, String endpointURL, String httpMethod, String scope) throws Exception {
        StringEntity entity = null;
        String dmpEnvironment = DMPContext.getDmpEnvironment();
        String bearerToken = getTelusToken(scope);
        logger.debug("::::::::::::::::::::: requestPayload before calling Telus API ::::::::::::::::::::: ", requestPayload);
        if (requestPayload != null) {
            entity = new StringEntity(requestPayload, ContentType.APPLICATION_JSON);
        }
        logger.debug("::::::::::::::::::::: Telus telusEndpointURL ::::::::::::::::::::: ", endpointURL);
        HttpResponse response = null;
        Integer statusCode = null;
        String result = null;
        ResponseEntity<String> responseEntity = null;
        HttpHeaders headers = null;
        headers = new HttpHeaders();
        if (telusApiEnv != null) {
            headers.add("env", telusApiEnv);
        }
        headers.add("Authorization", "Bearer " + bearerToken);
        headers.add("Content-Type", "application/json");
        responseEntity = invokeTelusGetAPIWithRestTemp(headers, endpointURL);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        statusCode = httpStatus.value();
        // Read the response
        // result = responseEntity.getBody();
        // HttpHeaders headers1 = responseEntity.getHeaders();
        // String li = headers1.getFirst("x-total-count");
        // result = "{\"totalNumberOfElement\":" + Integer.parseInt(li) + ",\"responseObjectList\":" + responseEntity.getBody() + "}";
        // logger.debug("::::::::Telus outbound API For  Parr Reports response ::::::::::::::::", result);
        logger.debug("::::::::::::::::::::: Telus outbound API For  Parr Reports Header ::::::::::::::::::::: ", responseEntity.getHeaders());
        if (statusCode == 200 || statusCode == 201) {
            logger.debug("::::::::::::::::::::: Telus outbound API response statusCode ::::::::::::::::::::: ", statusCode);
            return responseEntity;
        } else {
            logger.error("::::::::::::::::::::: Telus outbound API failed response statusCode ::::::::::::::::::::: ", statusCode);
            throw new Exception("Telus API failed with statusCode : " + statusCode);
        }
        // return responseEntity;
    }

    public ResponseEntity<String> executeTelusAPIAndGetResponseWithHeaderForSpecialChar(String requestPayload, URI uri, String httpMethod, String scope) throws Exception {
        StringEntity entity = null;
        String dmpEnvironment = DMPContext.getDmpEnvironment();
        String bearerToken = getTelusToken(scope);
        logger.debug("::::::::::::::::::::: requestPayload before calling Telus API ::::::::::::::::::::: ", requestPayload);
        if (requestPayload != null) {
            entity = new StringEntity(requestPayload, ContentType.APPLICATION_JSON);
        }
        logger.debug("::::::::::::::::::::: Telus telusEndpointURL ::::::::::::::::::::: ", uri.toString());
        HttpResponse response = null;
        Integer statusCode = null;
        String result = null;
        ResponseEntity<String> responseEntity=null;
        HttpHeaders headers=null;
        headers = new HttpHeaders();
        if (telusApiEnv != null) {
            headers.add("env", telusApiEnv);
        }
        headers.add("Authorization", "Bearer " + bearerToken);
        headers.add("Content-Type", "application/json");
        // responseEntity= invokeTelusGetAPIWithRestTemp(headers,endpointURL);
        logger.debug("::::::::::::::::::::: Before calling Telus API get execute method :::::::::::::::::::::");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        statusCode = httpStatus.value();
        logger.debug("::::::::::::::::::::: Telus outbound API For PARR Reports Header ::::::::::::::::::::: ", responseEntity.getHeaders());
        if (statusCode == 200 || statusCode == 201) {
            logger.debug("::::::::::::::::::::: Telus outbound API response statusCode ::::::::::::::::::::: ", statusCode);
            return responseEntity;
        } else {
            logger.error("::::::::::::::::::::: Telus outbound API failed response statusCode ::::::::::::::::::::: ", statusCode);
            throw new Exception("Telus API failed with statusCode : " + statusCode);
        }
        // return responseEntity;
    }
}
