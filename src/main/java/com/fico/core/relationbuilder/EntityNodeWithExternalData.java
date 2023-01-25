package com.fico.core.relationbuilder;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author AnubhavDas
 *
 * @implSpec
 * Class that assists for entity builder configuration. This model represents an individual entity which
 * stores further external data on other data stores such as S3. A typical example would be a <b><i>Document</i></b>
 * entity which stores actual document data on S3
 */
public class EntityNodeWithExternalData {

	//name of the DB entity
	private String entityName;
	
	//map of all such services along with APIs for deletion of any such external data associated to this entity
	private LinkedHashMap<String, List<String>> mapOfServiceWithDeleteAPI;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public LinkedHashMap<String, List<String>> getMapOfServiceWithDeleteAPI() {
		return mapOfServiceWithDeleteAPI;
	}

	public void setMapOfServiceWithDeleteAPI(LinkedHashMap<String, List<String>> mapOfServiceWithDeleteAPI) {
		this.mapOfServiceWithDeleteAPI = mapOfServiceWithDeleteAPI;
	}
	
}
