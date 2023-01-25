package com.fico.pscomponent.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fico.pscomponent.util.PropertiesUtil;
import com.hazelcast.map.IMap;
import com.wavemaker.commons.WMRuntimeException;
import com.wavemaker.runtime.hazelcast.FawbAppHazelcastInstance;

import com.fico.dmp.context.DMPContext;

import com.fico.dmp.security.oauth2.Oauth2ClientException;

@Service
public class DMPAuthenticationHandler {

	private static final Logger logger = LoggerFactory.getLogger(DMPAuthenticationHandler.class);

    private static final String DMP_TOKEN_CLIENT_ID = "DMP_TOKEN_CLIENT_ID";

	private static final String DMP_TOKEN_CLIENT_SECRET = "DMP_TOKEN_CLIENT_SECRET";

	private static final String DMP_TOKEN_JWT_URL = "DMP_TOKEN_JWT_URL";

	private static final String FRAUD_CACHE_MAP = "ProcessCacheMap";

	private static final String JWT_TOKEN = "jwtToken";

	private static final String CACHE_EXPIRE_IN_SEC = "CACHE_TOKEN_EXPIRE_IN_SEC";
	
	private static final String USE_DMP_JWT_TOKEN_URL = "USE_DMP_JWT_TOKEN_URL";
	

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private PropertiesHandler propertiesHandler;

	@Autowired
	private FawbAppHazelcastInstance fawbAppHazelcastInstance;

	private String clientId;

	private String cacheExpireInSec;

	private String getClientId() {
		return clientId;
	}

	private String clientSecret;
	
	

	private String getclientSecret() {
		return clientSecret;
	}

	private String jwtURL;

	public String getJwtURL() {
		return jwtURL;
	}
	
	private String useJWTTokenURL;
	
	

	@PostConstruct
	public void init() {
		
	}

	private String getToken() {
		//logger.info("INVOKED: getToken()");
		IMap<String, String> map = fawbAppHazelcastInstance.getMap(FRAUD_CACHE_MAP);
		if (map.containsKey(JWT_TOKEN)) {
			String token = map.get(JWT_TOKEN);
			return token;
		} 
		return null;
	}

	private void registerToken(String token) {
		//logger.info("INVOKED: registerToken()");
		IMap<String, String> map = fawbAppHazelcastInstance.getMap(FRAUD_CACHE_MAP);
		map.put(JWT_TOKEN, token, Integer.parseInt(this.cacheExpireInSec), TimeUnit.SECONDS);
	}

//    @Cacheable(value = "jwtToken")
	public String getJWTToken() {

		this.useJWTTokenURL = propertiesHandler.getPropertyValueByName(USE_DMP_JWT_TOKEN_URL);
		this.cacheExpireInSec = propertiesHandler.getPropertyValueByName(CACHE_EXPIRE_IN_SEC);
		String jwtToken = "";

		if ("true".equalsIgnoreCase(this.useJWTTokenURL)) {
			this.clientId = propertiesHandler.getPropertyValueByName(DMP_TOKEN_CLIENT_ID);
			this.clientSecret = propertiesHandler.getPropertyValueByName(DMP_TOKEN_CLIENT_SECRET);
			this.jwtURL = propertiesHandler.getPropertyValueByName(DMP_TOKEN_JWT_URL);

//		
//    	String token = getToken();
//    	if (!Objects.isNull(token)) {
//    		return token;
//    	}

			// logger.info("INITIATING :: Fetching DMP JWT Token");
			String result = null;
			try {

				String payload = "{\"clientId\": \"%s\",\"secret\": \"%s\"}";
				// payload = String.format(payload,
				// "195yniux504","Uch45BJf3Ai8^yb1k3d923vU83x8r27hFVXD");
				payload = String.format(payload, this.clientId, this.clientSecret);
				// logger.info("JWT Token payload is: {}", payload);

				StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
				HttpClient client = HttpClientBuilder.create().build();
				// logger.info("getJwtURL:::::::::::::::::", jwtURL);
				HttpPost httpRequest = new HttpPost(this.jwtURL);
				httpRequest.setEntity(entity);
				HttpResponse response = client.execute(httpRequest);

				if (response.getStatusLine().getStatusCode() == 200) {
					result = readResponse(response);
					jwtToken = result;
					// logger.info("Result token {}",result);
					/*
					 * JSONObject jwtObject = new JSONObject(result); jwtToken =
					 * jwtObject.getString("val");
					 */
					// logger.info("SUCCESS :: Fetching DMP JWT Token");

				} else {
					// logger.info("HTTP Status code : " +
					// response.getStatusLine().getStatusCode());
					result = readResponse(response);
					// logger.info("FAILED :: Fetching DMP JWT Token");
					throw new WMRuntimeException(result);
				}
			} catch (Exception e) {

				java.io.StringWriter sw = new java.io.StringWriter();
				java.io.PrintWriter pw = new java.io.PrintWriter(sw);
				e.printStackTrace(pw);
				pw.flush();
				pw.close();
				throw new WMRuntimeException(sw.toString().substring(0, 500), e);
			}
			// logger.info("COMPLETE :: Fetching DMP JWT Token");
			return jwtToken;

		} else {
			try {
				jwtToken = generateToken();
			} catch (Exception e) {
				java.io.StringWriter sw = new java.io.StringWriter();
				java.io.PrintWriter pw = new java.io.PrintWriter(sw);
				e.printStackTrace(pw);
				pw.flush();
				pw.close();
				throw new WMRuntimeException(sw.toString().substring(0, 500), e);
			}
			return jwtToken;
		}
	}

	private String readResponse(HttpResponse response) {
		StringBuffer result = new StringBuffer();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			logger.error("Error reading from error response stream");
			return null;
		}
		return result.toString();
	}
	
	 public String generateToken() throws Exception{
	     logger.info("Inside generateToken");
         try {
             return DMPContext.getContext().getSecurityContext().getClientAccessToken(false, false);
         } catch (Oauth2ClientException e) {
             e.printStackTrace();
           throw new Exception("generating client tokens");
         }
        
     }
	
	
}

