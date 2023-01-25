package com.fico.ps.hermes.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import com.fico.ps.hermes.config.ApplicationConfig;
import com.fico.ps.hermes.config.ApplicationConfig.DomainModelSaveConfig;

/**
 * @author MushfikKhan
 *
 */
public class Utility {
	
	/**
	 * @param domainClassName
	 * @param applicationConfig
	 * @return
	 */
	public static String getIdAttributeNameByDomainClass(String domainClassName, ApplicationConfig applicationConfig) {
		String idAttributeName = "id";
		DomainModelSaveConfig domainModelSaveConfig = applicationConfig.getDomainModelSaveConfigMap().get(domainClassName);
		if (domainModelSaveConfig != null && domainModelSaveConfig.getIdAttribName() != null && !"".equals(domainModelSaveConfig.getIdAttribName().trim())){
			idAttributeName = domainModelSaveConfig.getIdAttribName().trim();
		} else if (applicationConfig.getDefaultIdAttribName() != null && !"".equals(applicationConfig.getDefaultIdAttribName().trim())) {
			idAttributeName = applicationConfig.getDefaultIdAttribName().trim();
		}
		
		return idAttributeName;
	}
	
	/**
	 * @param fqClassName
	 * @return
	 */
	public static String getClassNameWithoutPackage(String fqClassName) {
		String className = null;
		if (fqClassName != null) {
			String[] strArray = fqClassName.split("\\.");
			if (strArray != null && strArray.length > 0) {
				className = strArray[strArray.length-1];
			}
		}
		return className;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
	    return (type.isPrimitive() && type != void.class) || type.isEnum() ||
	        type == Double.class || type == Float.class || type == Long.class ||
	        type == Integer.class || type == Short.class || type == Character.class ||
	        type == Byte.class || type == Boolean.class || type == String.class ||
	        type == BigInteger.class || type == BigDecimal.class || 
	        type == Date.class || type == LocalDateTime.class || type == Timestamp.class ||
	        type == LocalDate.class || type == LocalTime.class;
	}
	
	/**
	 * @param applicationConfig
	 * @return
	 */
	public static String getDefaultIdAttributeName(ApplicationConfig applicationConfig) {
		return applicationConfig.getDefaultIdAttribName() != null
				&& !applicationConfig.getDefaultIdAttribName().trim().isEmpty()
						? applicationConfig.getDefaultIdAttribName().trim()
						: "id";
	}
}
