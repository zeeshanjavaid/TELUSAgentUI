//package com.fico.ps.hermes.config;
//
//import java.lang.reflect.Constructor;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.PostConstruct;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.fico.ps.hermes.audit.IAuditDataSave;
//
///**
// * @author MushfikKhan
// *
// */
//@Component
//public class CustomImplProvider {
//	
//	private static Logger logger = LoggerFactory.getLogger(CustomImplProvider.class);
//
//	@Autowired
//	private ApplicationConfig applicationConfig;
//
//	private Map<String, IAuditDataSave> auditDataSaveMap;
//
//	@PostConstruct
//	private void init() {
//		this.auditDataSaveMap = new HashMap<String, IAuditDataSave>();
//		if (applicationConfig.getDataAuditSaveImpl() != null && !"".equals(applicationConfig.getDataAuditSaveImpl().trim())) {
//			Class<?> clazz;
//			try {
//				clazz = Class.forName(applicationConfig.getDataAuditSaveImpl().trim());
//				Constructor<?> cons = clazz.getConstructor();
//				IAuditDataSave auditDataSave = (IAuditDataSave) cons.newInstance();
//				this.auditDataSaveMap.put("modelConfig", auditDataSave); // TODO read key from config file for multiple domain model support
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			} 
//		}
//	}
//
//	/**
//	 * @return the auditDataSaveMap
//	 */
//	public Map<String, IAuditDataSave> getAuditDataSaveMap() {
//		return auditDataSaveMap;
//	}
//
//	/**
//	 * @param auditDataSaveMap the auditDataSaveMap to set
//	 */
//	public void setAuditDataSaveMap(Map<String, IAuditDataSave> auditDataSaveMap) {
//		this.auditDataSaveMap = auditDataSaveMap;
//	}
//	
//	public IAuditDataSave getAuditDataSave() { //TODO add param key for multiple domain model support
//		return this.auditDataSaveMap.get("modelConfig");
//	}
//
//}
