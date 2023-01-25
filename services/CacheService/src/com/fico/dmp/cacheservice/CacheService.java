/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.cacheservice;


import com.wavemaker.runtime.service.annotations.ExposeToClient;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fico.ps.cache.CacheUtils;

import com.hazelcast.config.Config;
import com.hazelcast.map.LocalMapStats;

import java.util.Map;


//import com.fico.dmp.cacheservice.model.*;

/**
 * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
 * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
 * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
 * Complex Types/Objects will become part of the Request body in the generated API.
 */
@ExposeToClient
public class CacheService {


    @Autowired
	private CacheUtils cacheUtils;

    public Map<String,LocalMapStats>  stats() {
        
        return cacheUtils.stats();
    }
    
    public Map<String,Integer>  cacheSizes() {
        
        
        return cacheUtils.cacheSizes();
    }
    
      public Integer  memberCount() {
      
        return cacheUtils.memberCount();
    }

    public ObjectNode clusterMembers() {
        
        return cacheUtils.clusterMembers();
    }


    public Config config() {
   
        return cacheUtils.config();
            
    }

}