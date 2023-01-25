package com.fico.core.relationbuilder;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author AnubhavDas
 * <br><br>
 * 
 * @implSpec
 * Model class for representing how the entities will be ordered in a hierarchy 
 * and used by the builder utility class for operations such as dynamic building
 * of entity relationship hierarchy to assist features such as data purging etc..
 * 
 * @see EntityRelationBuilder
 */
@JsonPropertyOrder(value = {"UUID", "rank", "className", "parentNodes", "relationClauses"})
@JsonRootName(value = "node")
public class EntityNode {

	@JsonProperty(value = "uuid")
	private String UUID;
	private long rank = 0;
	
	@JsonProperty(value = "className")
	@JsonSerialize(using = EntityNodeClassPropSerializer.class)
	@JsonDeserialize(using = EntityNodeClassPropDeserializer.class)
	private Class<?> clazz;
	
	private List<EntityNode> parentNodes;
	
	@JsonProperty(value = "relationClauses")
	private List<String> relationEqualityList;
	
	@JsonIgnore
	private long duplicateMarker = 0;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uuid) {
		UUID = uuid;
	}
	public long getRank() {
		return rank;
	}
	public void setRank(long rank) {
		this.rank = rank;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public List<EntityNode> getParentNodes() {
		return parentNodes;
	}
	public void setParentNodes(List<EntityNode> parentNodes) {
		this.parentNodes = parentNodes;
	}
	public List<String> getRelationEqualityList() {
		return relationEqualityList;
	}
	public void setRelationEqualityList(List<String> relationEqualityList) {
		this.relationEqualityList = relationEqualityList;
	}
	public long getDuplicateMarker() {
		return duplicateMarker;
	}
	public void setDuplicateMarker(long duplicateMarker) {
		this.duplicateMarker = duplicateMarker;
	}
	
}
