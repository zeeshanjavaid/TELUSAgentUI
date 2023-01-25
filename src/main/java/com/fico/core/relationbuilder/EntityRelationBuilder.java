package com.fico.core.relationbuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fico.core.util.CommonUtils;

/**
 * @author AnubhavDas
 * <br><br>
 * 
 * @implSpec
 * Utility class that provides features such as building of entity relationship
 * hierarchy, entity rank ordering, sequential SQL clause generation to support 
 * features such as data purging etc..
 * 
 * @see EntityNode
 */
public class EntityRelationBuilder {

	private CommonUtils commonUtils;
	
	private JdbcTemplate jdbcTemplate;
	
	private ApplicationContext applicationContext;
	
	private Class<?> rootEntity;
	
	//These are configurable properties from an external configuration file
	private String packageName;
	private String rootEntityName;
	private String dataSourceBeanName;
	private List<String> exclusionClassNames;
	private List<String> configuredDeleteClauses;
	private List<EntityNodeWithExternalData> entitiesWithExternalData;
	
	//getters & setters
	public String getRootEntityName() {
		return rootEntityName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setRootEntityName(String rootEntityName) {
		this.rootEntityName = rootEntityName;
		
		if(this.packageName != null && !this.packageName.isEmpty() && this.rootEntityName != null && !this.rootEntityName.isEmpty()) {
			try {
				this.rootEntity = Class.forName(this.packageName + "." + this.rootEntityName);
			} catch (ClassNotFoundException e) {
				if(logger.isErrorEnabled())
					logger.error("Unable to find Class<?> against the given class name", e);
			}
		}
	}

	public String getDataSourceBeanName() {
		return dataSourceBeanName;
	}

	public void setDataSourceBeanName(String dataSourceBeanName) {
		this.dataSourceBeanName = dataSourceBeanName;
	}

	public List<String> getExclusionClassNames() {
		return exclusionClassNames;
	}

	public void setExclusionClassNames(List<String> exclusionClassNames) {
		this.exclusionClassNames = exclusionClassNames;
	}

	public List<String> getConfiguredDeleteClauses() {
		return configuredDeleteClauses;
	}

	public void setConfiguredDeleteClauses(List<String> configuredDeleteClauses) {
		this.configuredDeleteClauses = configuredDeleteClauses;
	}

	public List<EntityNodeWithExternalData> getEntitiesWithExternalData() {
		return entitiesWithExternalData;
	}

	public void setEntitiesWithExternalData(List<EntityNodeWithExternalData> entitiesWithExternalData) {
		this.entitiesWithExternalData = entitiesWithExternalData;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		
		if(this.applicationContext != null) {
			this.jdbcTemplate = new JdbcTemplate((DataSource) this.applicationContext.getBean(this.dataSourceBeanName));
			this.commonUtils = (CommonUtils) this.applicationContext.getBean(CommonUtils.class);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(EntityRelationBuilder.class);
	
	/**
	 * This is the primary method that is responsible for building & providing the entire ENTITY relationship hierarchy
	 * @return {@link List} of {@link EntityNode} with all relationships and ranking between them established
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<EntityNode> buildEntityRelationHierarchy() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildEntityRelationHierarchy'");
		
		if(this.rootEntity != null && this.packageName != null && !this.packageName.isEmpty()) {
			List<Class> exclusionClasses = null;
			List<EntityNode> entityNodes = new ArrayList<>();
			
//			this.commonUtils = new CommonUtils();  //>> uncomment this line for local testing
			List<Class> scannedClasses = this.commonUtils.listAllClassesUnderPackage(this.packageName, Entity.class);
			
			//check for scanned classes
			if(scannedClasses != null && !scannedClasses.isEmpty()) {
				if(scannedClasses.stream().filter(clazz -> clazz.equals(this.rootEntity)).count() == 0)
					throw new Exception("Unexpected error ocurred at method 'buildEntityRelationHierarchy'. "
							+ "Specified root entity class ["+ this.rootEntityName +"] was not found under package ["+ this.packageName +"]");
			
				//checking for exclusion classes
				if (this.exclusionClassNames != null && !this.exclusionClassNames.isEmpty()) {
					exclusionClasses = new ArrayList<>();
					for(String className : this.exclusionClassNames)
						exclusionClasses.add(Class.forName(this.packageName + "." + className));
					
					if(exclusionClasses != null && !exclusionClasses.isEmpty())
						scannedClasses.removeAll(exclusionClasses);
				}
	
				for (Class clazz : scannedClasses) {
					// creating the entity node
					EntityNode entityNode = new EntityNode();
					entityNode.setUUID(UUID.randomUUID().toString());
					entityNode.setClazz(clazz);
					entityNode.setParentNodes(new ArrayList<EntityNode>());
					entityNode.setRelationEqualityList(new ArrayList<String>());
	
					findAndAddEntityToHierarchy(this.rootEntity, entityNodes, clazz, entityNode, exclusionClasses);
				} // iterate over each scanned class
	
				return getEntityNodesOrdered(entityNodes, this.rootEntity);
			}
			else
				return null;
		}
		else
			throw new Exception("-----Inside method 'buildEntityRelationHierarchy'. No root entity class/package specified!");
	}
	
	/**
	 * This method recursively traverses all of the entities to establish the relation & adds to the hierarchy
	 * @param rootEntityClass
	 * The root level {@link Entity} class
	 * @param entityNodeList
	 * The list where to add the nodes
	 * @param clazzToScan
	 * The {@link Class} of the current processing node
	 * @param processingNode
	 * The current processing node
	 * @param exclusionClasses
	 * The list of all exclusion classes <b>(if any)</b>
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void findAndAddEntityToHierarchy(Class rootEntityClass, List<EntityNode> entityNodeList, Class clazzToScan,
			EntityNode processingNode, List<Class> exclusionClasses) throws Exception {
		
		//processing to only happen if processing NODE not in hierarchy
		if(!entityNodeList.stream().anyMatch(node -> node.getClazz().equals(processingNode.getClazz()))) {
			if(logger.isInfoEnabled())
				logger.info("Current processing node: " + processingNode.getClazz().getName());
			entityNodeList.add(processingNode);
		
			final List<Method> joinRefMethods = Arrays.asList(clazzToScan.getDeclaredMethods()).stream().
					filter(method -> AnnotationUtils.findAnnotation(method, JoinColumn.class) != null &&
					!method.getReturnType().equals(clazzToScan)).
					collect(Collectors.toList());
			
			//if there are some parent references, add their types
			List<Class<?>> parentEntities = null;
			if(joinRefMethods != null && !joinRefMethods.isEmpty()) {
				parentEntities = new ArrayList<>();
				
				for(Method method : joinRefMethods) {
					parentEntities.add(method.getReturnType());
				}
			}
			
			//if parent entities are associated, recursively find all the top level entities
			if(parentEntities != null && !parentEntities.isEmpty()) {
				if(exclusionClasses != null && !exclusionClasses.isEmpty())
					parentEntities.removeAll(exclusionClasses);
				
				//after exclusion list modification
				if(!parentEntities.isEmpty()) {
					final int numOfParents = parentEntities.size();
					int currentParentNum = 0;
					
					//processing each parent entities to current processing entity NODE
					for(Class<?> parentEntity : parentEntities) {
						EntityNode parentEntityNode = null;
						if(!entityNodeList.stream().anyMatch(node -> node.getClazz().equals(parentEntity))) {
							if(logger.isDebugEnabled())
								logger.debug("Adding parent node: [" + parentEntity.getName() + "] to current processing node: ["+ clazzToScan.getName() +"]");
							
							//creating the entity node
							parentEntityNode = new EntityNode();
							parentEntityNode.setUUID(UUID.randomUUID().toString());
							parentEntityNode.setClazz(parentEntity);
							parentEntityNode.setParentNodes(new ArrayList<EntityNode>());
							parentEntityNode.setRelationEqualityList(new ArrayList<String>());
						}
						else {
							if(logger.isDebugEnabled())
								logger.debug("Parent node: [" + parentEntity.getName() + "] already exists in hierarchy, using that");
							parentEntityNode = entityNodeList.stream().filter(node -> node.getClazz().equals(parentEntity))
									.findFirst().get();
						}
						
						processingNode.getParentNodes().add(parentEntityNode); //add this as parent node
						currentParentNum += 1;
						findAndAddEntityToHierarchy(rootEntityClass, entityNodeList, parentEntity, parentEntityNode, exclusionClasses); //recursive call
					}  //end of for loop
					
					if(currentParentNum >= numOfParents) {
						//removing all parent nodes from current processing node, which have no parents
						List<EntityNode> parentNodesWithEmptyParents = processingNode.getParentNodes().stream().
								filter(node ->
									(!node.getClazz().equals(rootEntityClass) && node.getParentNodes().isEmpty())
								).
								collect(Collectors.toList());
						
						if(parentNodesWithEmptyParents != null && !parentNodesWithEmptyParents.isEmpty()) {
							if(logger.isDebugEnabled())
								logger.debug("Removing parents with further empty parents on current processing node: " + processingNode.getClazz().getName());
							for(EntityNode parentWithEmptyParent : parentNodesWithEmptyParents) {
								processingNode.getParentNodes().removeIf(parentNode -> 
									parentNode.getClazz().equals(parentWithEmptyParent.getClazz()));
							} //for each empty parent >> remove from processing one
						}
						
						//if non-empty parents present for current processing node >> set the rank
						if(!processingNode.getParentNodes().isEmpty())
							setRankForEntityNode(processingNode);
						else {
							//removing the current processing node if all parents are in exclusion list
							entityNodeList.removeIf(enNode -> enNode.getClazz().equals(processingNode.getClazz()));
						}
					}
				}
				else {
					//removing the current processing node if all parents are in exclusion list
					entityNodeList.removeIf(enNode -> enNode.getClazz().equals(processingNode.getClazz()));
				}
			}
			else
				processingNode.setRank(0); //for root level parent NODEs
		}
	}
	
	/**
	 * Determines the rank order for the given entity node
	 * @param entityNode
	 * The {@link EntityNode} instance
	 * @throws Exception
	 */
	private void setRankForEntityNode(EntityNode entityNode) throws Exception {
		if(entityNode != null ) {
			final List<EntityNode> parentNodes = createCopyEntityNodeHierarchy(entityNode.getParentNodes());
			
			//when there are more than 1 parent
			if(parentNodes.size() > 1) {
				if(logger.isInfoEnabled())
					logger.info("Setting rank for node ["+ entityNode.getClazz().getName() +"] with "+ parentNodes.size() +" parents");
				
				parentNodes.sort(new Comparator<EntityNode>() {
	
					@Override
					public int compare(EntityNode o1, EntityNode o2) {
						return (int)(o2.getRank() - o1.getRank());
					}
				});
				
				//setting the rank of the 'current NODE = parent NODE with highest rank + 1'
				entityNode.setRank(parentNodes.get(0).getRank() + 1);
			}
			else if(parentNodes.size() == 1) {
				if(logger.isInfoEnabled())
					logger.info("Setting rank for node ["+ entityNode.getClazz().getName() +"] with 1 parent");
				
				//setting the rank of the 'current NODE = parent NODE rank + 1'
				entityNode.setRank(entityNode.getParentNodes().get(0).getRank() + 1);
			}
		}
		else
			throw new Exception("Exception ocurred at method 'getRankForEntityNode', EntityNode supplied is null");
	}
	
	/**
	 * Removes any duplicates from the supplied list
	 * @param entityNodes
	 * The list of {@link EntityNode}s
	 * @return
	 * List of {@link EntityNode} without any duplicates
	 * @throws Exception
	 */
	private List<EntityNode> removeDuplicatesIfAny(List<EntityNode> entityNodes) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'removeDuplicatesIfAny'");
		
		if(entityNodes != null && !entityNodes.isEmpty()) {
			List<EntityNode> finalNodeList = new ArrayList<>();
			
			while(!entityNodes.isEmpty()) {
				//removing duplicates
				for(EntityNode en : entityNodes) {
					final List<EntityNode> matchingToCurNode = entityNodes.stream().
							filter(node -> node.getClazz().equals(en.getClazz())).collect(Collectors.toList());
					
					if(matchingToCurNode.size() > 1) {
						//doing this to filter the one with highest rank
						matchingToCurNode.sort(new Comparator<EntityNode>() {
	
							@Override
							public int compare(EntityNode o1, EntityNode o2) {
								return (int)(o2.getRank() - o1.getRank());
							}
						});
					}
					
					EntityNode nodeToAdd = new EntityNode();
					nodeToAdd.setUUID(matchingToCurNode.get(0).getUUID());
					nodeToAdd.setRank(matchingToCurNode.get(0).getRank());
					nodeToAdd.setClazz(matchingToCurNode.get(0).getClazz());
					nodeToAdd.setParentNodes(matchingToCurNode.get(0).getParentNodes());
					nodeToAdd.setRelationEqualityList(matchingToCurNode.get(0).getRelationEqualityList());
					
					finalNodeList.add(nodeToAdd); //added the one with the highest rank for matching nodes
					entityNodes.forEach(node -> {
						if(node.getClazz().equals(matchingToCurNode.get(0).getClazz())) {
							node.setDuplicateMarker(-1);
						}
					});
					
					break;
				} //iterator-block 
				
				entityNodes.removeIf(node -> node.getDuplicateMarker() == -1); //removing the nullified NODEs
			}
			if(logger.isInfoEnabled())
				logger.info("-----Inside method 'removeDuplicatesIfAny'. Post duplicates removal, the node count is: " + finalNodeList.size());
			return finalNodeList;
		}
		else
			return null;
	}
	
	/**
	 * Returns the final list of {@link EntityNode} ordered from lowest to highest
	 * @param entityNodes
	 * Initial list of {@link EntityNode}
	 * @param rootEntityClass
	 * The root level entity {@link Class}
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private List<EntityNode> getEntityNodesOrdered(List<EntityNode> entityNodes, Class rootEntityClass) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getEntityNodesOrdered'");
		
		if(entityNodes != null && !entityNodes.isEmpty()) {
			List<EntityNode> modifiedList = removeDuplicatesIfAny(entityNodes);
			buildRelationEqList(modifiedList, rootEntityClass);
			
			modifiedList.sort(new Comparator<EntityNode>() {

				@Override
				public int compare(EntityNode o1, EntityNode o2) {
					return (int)(o1.getRank() - o2.getRank());
				}
			});
			
			final List<EntityNode> copyEntityNodeHierarchy = createCopyEntityNodeHierarchy(modifiedList);
			return copyEntityNodeHierarchy;
		}
		else
			return null;
	}
	
	/**
	 * Supports the generation of {@link EntityNode}'s SQL select clause equality statements
	 * @param entityNodeList
	 * The final list of {@link EntityNode}
	 * @param rootEntityClass
	 * The root level entity {@link Class}
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void buildRelationEqList(List<EntityNode> entityNodeList, Class rootEntityClass) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'buildRelationEqList'");
		
		//iterate per entity node
		for(EntityNode en : entityNodeList) {
			//considers only a single root entity node (even if multiple root level nodes)
			if(en.getRank() == 0 && en.getParentNodes().isEmpty() && en.getClazz().equals(rootEntityClass))
				addRootEntityEquality(en.getClazz(), en.getRelationEqualityList());
			else {
				Class lastParentClass = null;
				
				//for each parent node
				for(EntityNode parentNode : en.getParentNodes()) {
					if(lastParentClass != null && lastParentClass.equals(parentNode.getClazz()));
					else
						addForeignEntityEquality(en.getClazz(), parentNode.getClazz(), en.getRelationEqualityList());
					
					lastParentClass = parentNode.getClazz();
				}
			}
		}
	}
	
	/**
	 * Creates the SQL select clause for the primary/root class provided
	 * @param entityClass
	 * The {@link Class} of the entity to be processed
	 * @param relationEqList
	 * List where to add the clauses for the given entity
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void addRootEntityEquality(Class entityClass, List<String> relationEqList) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getPrimaryKeyFromEntity'");
		
		if(entityClass != null) {
			final Method primaryKeyMethod = Arrays.asList(entityClass.getDeclaredMethods()).stream().
				filter(method -> AnnotationUtils.findAnnotation(method, Id.class) != null).findFirst().get();
			
				relationEqList.add("SELECT GROUP_CONCAT(" +
					"`" + AnnotationUtils.findAnnotation(entityClass, Table.class).name().replace("`", "").replace("`", "") + "`".
					concat("." + AnnotationUtils.findAnnotation(primaryKeyMethod, Column.class).name().replace("`", "").replace("`", "")) +
					") FROM " + "`" + AnnotationUtils.findAnnotation(entityClass, Table.class).name().replace("`", "").replace("`", "") + "`" 
					+ " WHERE " + "`" + AnnotationUtils.findAnnotation(entityClass, Table.class).name().replace("`", "").replace("`", "") + "`".
					concat("." + AnnotationUtils.findAnnotation(primaryKeyMethod, Column.class).name().replace("`", "").replace("`", "")) +
					" IN ({{" + entityClass.getName() + "}})");
		}
		else
			throw new Exception("Exception occurred at method 'getPrimaryKeyFromEntity'. Supplied entity class is null");
	}
	
	/**
	 * Creates the SQL select clause for the foreign key references
	 * @param entityClass
	 * The {@link Class} of the entity to be processed
	 * @param parentEntityClass
	 * The current parent{@link Class} of the entity to be processed
	 * @param relationEqList
	 * List where to add the clauses for the given entity
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void addForeignEntityEquality(Class entityClass, Class parentEntityClass, List<String> relationEqList) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'getPrimaryKeyFromEntity'");
		
		if(entityClass != null && parentEntityClass != null) {
			final Method primaryKeyMethod = Arrays.asList(entityClass.getDeclaredMethods()).stream().
					filter(method -> AnnotationUtils.findAnnotation(method, Id.class) != null).findFirst().get();
			
			final List<Method> foreignMethods = Arrays.asList(entityClass.getDeclaredMethods()).stream().
				filter(method -> AnnotationUtils.findAnnotation(method, JoinColumn.class) != null &&
						method.getReturnType().equals(parentEntityClass)).collect(Collectors.toList());
			
			for(Method method : foreignMethods) {
				relationEqList.add("SELECT GROUP_CONCAT(" +
						"`" + AnnotationUtils.findAnnotation(entityClass, Table.class).name().replace("`", "").replace("`", "") + "`".
						concat("." + AnnotationUtils.findAnnotation(primaryKeyMethod, Column.class).name().replace("`", "").replace("`", "")) +
						") FROM " + "`" + AnnotationUtils.findAnnotation(entityClass, Table.class).name().replace("`", "").replace("`", "") + "`"
						+ " WHERE " + "`" + AnnotationUtils.findAnnotation(entityClass, Table.class).name().replace("`", "").replace("`", "") + "`".
						concat("." + AnnotationUtils.findAnnotation(method, JoinColumn.class).name().replace("`", "").replace("`", "")) +
						" IN ({{" + parentEntityClass.getName() + "}})");
			}
		}
		else
			throw new Exception("Exception occurred at method 'getPrimaryKeyFromEntity'. Supplied entity/parentEntity class is null");
	}
	
	/**
	 * Returns a {@link Map} of the SQL select statements per entity node
	 * @param rootEntityIdValue
	 * The {@link Integer} Id value of the root entity
	 * @param orderedEntityNodes
	 * The list of final ordered {@link EntityNode}s
	 * @return
	 * {@link Map} of the SQL select statements per entity node
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public LinkedHashMap <Class, List<String>> getOrderedSelectClauseIds(Integer rootEntityIdValue,
			List<EntityNode> orderedEntityNodes) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside metod 'getOrderedSelectClauseIds'");
		
		if(this.jdbcTemplate == null)
			throw new Exception("Unexpected error ocurred at method 'getOrderedSelectClauseIds'. JdbcTemplate instance is null");
		
		if(this.rootEntity != null && rootEntityIdValue != null && rootEntityIdValue > 0 &&
				orderedEntityNodes != null && !orderedEntityNodes.isEmpty()) {
			LinkedHashMap<Class, List<String>> selectClauseResultIds = new LinkedHashMap<>();
			
			for(EntityNode en : orderedEntityNodes) {
				//if the NODE is root
				if(en.getRank() == 0 && en.getParentNodes().isEmpty() && en.getClazz().equals(this.rootEntity)) {
					String resultIds = "";
					
					//iterate over each select clause
					for(String selectClause : en.getRelationEqualityList()) {
						selectClause = updateSQLClausesForLargeInFilters(selectClause.replace("{{"+ en.getClazz().getName() +"}}",
								rootEntityIdValue.toString()));
						String fetchedIds = this.jdbcTemplate.query(selectClause,
								new ResultSetExtractor<String>() {
		
									@Override
									public String extractData(ResultSet rs) throws SQLException, DataAccessException {
										if(rs.next()) {
											return (rs.getString(1) == null ? "0" : rs.getString(1));
										}
										
										return "0";
									}
						});
						
						if(fetchedIds != null) {
							if(fetchedIds.equals("0"))
								return null;
							
							if(fetchedIds.endsWith(","))
								fetchedIds = fetchedIds.substring(0, (fetchedIds.length() - 1));
								
							resultIds = (resultIds.equals("")) ? fetchedIds : resultIds.concat("," + fetchedIds);
						}
					}
					
					//removing duplicate ID values
					resultIds = removeDuplicateIds(resultIds);
					
					//add the entry to the map
					if(selectClauseResultIds.get(en.getClazz()) == null) {
						List<String> ID_data = new ArrayList<>();
						ID_data.add(resultIds);
						selectClauseResultIds.put(en.getClazz(), ID_data);
					}
					else
						selectClauseResultIds.get(en.getClazz()).add(resultIds);
				}
				//otherwise
				else {
					String resultIds = "";
					
					//iterate over each select clause
					for(String selectClause : en.getRelationEqualityList()) {
						String entityClassToReplace = selectClause.substring(selectClause.indexOf("{{"), (selectClause.indexOf("}}") + 2));
						entityClassToReplace = entityClassToReplace.substring(2, entityClassToReplace.length() - 2);
						
						String parentIds = null;
						if(selectClauseResultIds.get(Class.forName(entityClassToReplace)) != null) {
							final List<String> parentIdList = selectClauseResultIds.get(Class.forName(entityClassToReplace));
							for(String IdSet : parentIdList)
								parentIds  = (parentIds == null) ? IdSet : parentIds.concat("," + IdSet);
						}
						
						selectClause = updateSQLClausesForLargeInFilters(selectClause.replaceAll("\\{\\{\\S+\\}\\}",
								(parentIds == null ? "0" : parentIds)));
						String fetchedIds = this.jdbcTemplate.query(selectClause,
								new ResultSetExtractor<String>() {
		
									@Override
									public String extractData(ResultSet rs) throws SQLException, DataAccessException {
										if(rs.next()) {
											return (rs.getString(1) == null ? "0" : rs.getString(1));
										}
										
										return "0";
									}
						});
						
						if(fetchedIds != null) {
							if(fetchedIds.endsWith(","))
								fetchedIds = fetchedIds.substring(0, (fetchedIds.length() - 1));
							
							resultIds = (resultIds.equals("")) ? fetchedIds : resultIds.concat("," + fetchedIds);
						}
					}
					
					//removing duplicate ID values
					resultIds = removeDuplicateIds(resultIds);
					
					//add the entry to the map
					if(selectClauseResultIds.get(en.getClazz()) == null) {
						List<String> ID_data = new ArrayList<>();
						ID_data.add(resultIds);
						selectClauseResultIds.put(en.getClazz(), ID_data);
					}
					else
						selectClauseResultIds.get(en.getClazz()).add(resultIds);
				}
			}  //iterate over entity nodes
			
			return selectClauseResultIds;
		}
		else
			return null;
	}
	
	/**
	 * Triggers full application data deletion by executing an ordered {@link List} of the SQL DELETE statements per entity node
	 * <b>plus</b> the deletion of any external data as for the configured set of such entities
	 * @param rootEntityIdValue
	 * The {@link Integer} Id value of the root entity
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void executeFullDeletion(Integer rootEntityIdValue) throws Exception {
		final Date startTime = new Date();
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeFullDeletion'. Operation started at: " +
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS Z").format(startTime));
		
		
		if(this.packageName == null || this.packageName.isEmpty() || this.rootEntity == null 
				|| rootEntityIdValue == null || rootEntityIdValue < 0)
			throw new Exception("Exception ocurred at method 'executeFullDeletion'. One/more required paramters are null/empty");

		List<EntityNode> orderedEntityNodes = buildEntityRelationHierarchy();
		LinkedHashMap <Class, List<String>> selectClausesOrdered = getOrderedSelectClauseIds(rootEntityIdValue, orderedEntityNodes);
		
		//adjust for configurable DELETE clauses
		adjustForConfiguredDeleteClauses(selectClausesOrdered);
		
		//perform deletion of external data on configured entities
		executeExternalDataDeletion(selectClausesOrdered);
		
		if(orderedEntityNodes != null && !orderedEntityNodes.isEmpty() && selectClausesOrdered != null && !selectClausesOrdered.isEmpty()) {
			LinkedHashMap<Class, List<String>> deleteClauses = new LinkedHashMap<>();
			List<EntityNode> copyNodeList = createCopyEntityNodeHierarchy(orderedEntityNodes);
			
			//sort from NODE rank (highest/lowest)
			copyNodeList.sort(new Comparator<EntityNode>() {

				@Override
				public int compare(EntityNode o1, EntityNode o2) {
					return (int)(o2.getRank() - o1.getRank());
				}
			});
			
			//get the ID's of the corresponding NODEs returned by the select clause
			final Set<Entry<Class, List<String>>> selectClauseEntrySet = selectClausesOrdered.entrySet();
			for(EntityNode en : copyNodeList) {
				final Entry<Class, List<String>> matchedNodeEntry = selectClauseEntrySet.stream().
						filter(entry -> entry.getKey().equals(en.getClazz())).findFirst().get();
				
				final Method primaryKeyMethod = Arrays.asList(matchedNodeEntry.getKey().getDeclaredMethods()).stream().
						filter(method -> AnnotationUtils.findAnnotation(method, Id.class) != null).findFirst().get();
				
				//attach the ID set from select clause to delete clause
				for(String idSet : matchedNodeEntry.getValue()) {
					String deleteClause = "DELETE FROM " +
							"`" + AnnotationUtils.findAnnotation(matchedNodeEntry.getKey(), Table.class).name().replace("`", "").replace("`", "") + "`" 
							+ " WHERE " + "`" + AnnotationUtils.findAnnotation(matchedNodeEntry.getKey(), Table.class).name().replace("`", "").replace("`", "") + "`".
							concat("." + AnnotationUtils.findAnnotation(primaryKeyMethod, Column.class).name().replace("`", "").replace("`", "")) +
							" IN (" + ((idSet == null || idSet.isEmpty()) ? "0" : idSet) + ")";
					deleteClause = updateSQLClausesForLargeInFilters(deleteClause);
					
					//checking if KEY exists
					if(deleteClauses.get(matchedNodeEntry.getKey()) == null) {
						List<String> delClause = new ArrayList<String>();
						delClause.add(deleteClause);
						deleteClauses.put(matchedNodeEntry.getKey(), delClause);
					}
					else
						deleteClauses.get(matchedNodeEntry.getKey()).add(deleteClause);
				}  //iterate over ID-set returned via select clause
			} //entity node iteration
			
			List<String> deleteClausesFinal = new ArrayList<>();
			if(deleteClauses != null && !deleteClauses.isEmpty()) {
				Iterator<Class> keyItr = deleteClauses.keySet().iterator();
				
				while(keyItr.hasNext()) {
					final Class currentClazz = keyItr.next();
					deleteClausesFinal.addAll(deleteClauses.get(currentClazz));
				}
			}
			
			//execute each SQL DELETE clause
			for(String deleteClause : deleteClausesFinal) {
				if(logger.isInfoEnabled())
					logger.info("DELETE clause to be executed: ["+ deleteClause +"]");
				this.jdbcTemplate.execute(deleteClause);
				if(logger.isInfoEnabled())
					logger.info("DELETE clause: ["+ deleteClause +"] executed successfully");
			}
		}
		
		final Date endTime = new Date();
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeFullDeletion'. Operation completed at: "
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS Z").format(endTime)
				+ ". Operation took: " + (endTime.getTime() - startTime.getTime()) + "ms");
	}
	
	/**
	 * Returns an ordered {@link List} of the SQL delete statements per entity node
	 * @param rootEntityIdValue
	 * The {@link Integer} Id value of the root entity
	 * @return
	 * {@link DeleteClausesWrapper} instance wrapping the list of ordered SQL delete statements per entity node
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public DeleteClausesWrapper getOrderedDeleteClauses(Integer rootEntityIdValue) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside metod 'getOrderedDeleteClauses'");
		
		if(this.packageName == null || this.packageName.isEmpty() || this.rootEntity == null 
				|| rootEntityIdValue == null || rootEntityIdValue < 0)
			throw new Exception("Exception ocurred at method 'getOrderedDeleteClauses'. One/more required paramters are null/empty");
		
		List<EntityNode> orderedEntityNodes = buildEntityRelationHierarchy();
		LinkedHashMap <Class, List<String>> selectClausesOrdered = getOrderedSelectClauseIds(rootEntityIdValue, orderedEntityNodes);
		
		//adjust for configurable DELETE clauses
		adjustForConfiguredDeleteClauses(selectClausesOrdered);
		
		if(orderedEntityNodes != null && !orderedEntityNodes.isEmpty() && selectClausesOrdered != null && !selectClausesOrdered.isEmpty()) {
			LinkedHashMap<Class, List<String>> deleteClauses = new LinkedHashMap<>();
			List<EntityNode> copyNodeList = createCopyEntityNodeHierarchy(orderedEntityNodes);
			
			//sort from NODE rank (highest/lowest)
			copyNodeList.sort(new Comparator<EntityNode>() {

				@Override
				public int compare(EntityNode o1, EntityNode o2) {
					return (int)(o2.getRank() - o1.getRank());
				}
			});
			
			//get the ID's of the corresponding NODEs returned by the select clause
			final Set<Entry<Class, List<String>>> selectClauseEntrySet = selectClausesOrdered.entrySet();
			for(EntityNode en : copyNodeList) {
				final Entry<Class, List<String>> matchedNodeEntry = selectClauseEntrySet.stream().
						filter(entry -> entry.getKey().equals(en.getClazz())).findFirst().get();
				
				final Method primaryKeyMethod = Arrays.asList(matchedNodeEntry.getKey().getDeclaredMethods()).stream().
						filter(method -> AnnotationUtils.findAnnotation(method, Id.class) != null).findFirst().get();
				
				//attach the ID set from select clause to delete clause
				for(String idSet : matchedNodeEntry.getValue()) {
					String deleteClause = "DELETE FROM " +
							"`" + AnnotationUtils.findAnnotation(matchedNodeEntry.getKey(), Table.class).name().replace("`", "").replace("`", "") + "`" 
							+ " WHERE " + "`" + AnnotationUtils.findAnnotation(matchedNodeEntry.getKey(), Table.class).name().replace("`", "").replace("`", "") + "`".
							concat("." + AnnotationUtils.findAnnotation(primaryKeyMethod, Column.class).name().replace("`", "").replace("`", "")) +
							" IN (" + ((idSet == null || idSet.isEmpty()) ? "0" : idSet) + ")";
					deleteClause = updateSQLClausesForLargeInFilters(deleteClause);
					
					//checking if KEY exists
					if(deleteClauses.get(matchedNodeEntry.getKey()) == null) {
						List<String> delClause = new ArrayList<String>();
						delClause.add(deleteClause);
						deleteClauses.put(matchedNodeEntry.getKey(), delClause);
					}
					else
						deleteClauses.get(matchedNodeEntry.getKey()).add(deleteClause);
				}  //iterate over ID-set returned via select clause
			} //entity node iteration
			
			List<String> deleteClausesFinal = new ArrayList<>();
			if(deleteClauses != null && !deleteClauses.isEmpty()) {
				Iterator<Class> keyItr = deleteClauses.keySet().iterator();
				
				while(keyItr.hasNext()) {
					final Class currentClazz = keyItr.next();
					deleteClausesFinal.addAll(deleteClauses.get(currentClazz));
				}
				
				DeleteClausesWrapper wrapper = new DeleteClausesWrapper();
				wrapper.setDeleteClauses(deleteClausesFinal);
				return wrapper;
			}
			else
				return null;
		}
		else
			return null;
	}
	
	/**
	 * Method that assists for deletion of any external data attached to relevant configured entities
	 * @param selectClausesOrdered
	 * The {@link Map} of entity NODE >> list of generated SELECT clauses
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void executeExternalDataDeletion(LinkedHashMap<Class, List<String>> selectClausesOrdered) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeExternalDataDeletion'");
		
		if(this.entitiesWithExternalData != null && !this.entitiesWithExternalData.isEmpty() && selectClausesOrdered != null) {
			
			//for each configured entity NODE having external data
			for(EntityNodeWithExternalData entityNodeWithExternalData : this.entitiesWithExternalData) {
				final Class entityClass = Class.forName(this.packageName.concat("." + entityNodeWithExternalData.getEntityName()));
				
				if(entityClass != null && selectClausesOrdered.get(entityClass) != null) {
					if(logger.isDebugEnabled())
						logger.debug("Processing started for deletion of external data against entity: ["+ entityClass.getName() +"]");
					
					if(entityNodeWithExternalData.getMapOfServiceWithDeleteAPI() != null) {
						Iterator<String> serviceClassItr = entityNodeWithExternalData.getMapOfServiceWithDeleteAPI().keySet().iterator();
						
						//per service class configured
						while(serviceClassItr.hasNext()) {
							final String serviceClassName = serviceClassItr.next(); 
							final Class serviceClass = Class.forName(serviceClassName);
							Object serviceClassInstance = this.applicationContext.getBean(serviceClass);
							if(logger.isDebugEnabled())
								logger.debug("Processing started for service class: ["+ serviceClassName +"] against entity: ["+ entityClass.getName() +"]");
							
							//for each methods configured
							for(String APIName : entityNodeWithExternalData.getMapOfServiceWithDeleteAPI().get(serviceClassName)) {
								final Method matchingMethod = serviceClass.getDeclaredMethod(APIName, Integer.class);
								
								if(matchingMethod != null) {
									if(logger.isDebugEnabled())
										logger.debug("Processing started on method: ["+ APIName +"] for service class: ["+ serviceClassName +"] against entity: ["+ entityClass.getName() +"]");
									
									//invoke this method for the entire ID set
									for(String IdSet : selectClausesOrdered.get(entityClass)) {
										for(String IdVal : IdSet.split(","))
											matchingMethod.invoke(serviceClassInstance, Integer.valueOf(IdVal));
									}
									if(logger.isDebugEnabled())
										logger.debug("Processing completed on method: ["+ APIName +"] for service class: ["+ serviceClassName +"] against entity: ["+ entityClass.getName() +"]");
								}
							}
							if(logger.isDebugEnabled())
								logger.debug("Processing completed for service class: ["+ serviceClassName +"] against entity: ["+ entityClass.getName() +"]");
						} //end of while-loop
					}
					if(logger.isDebugEnabled())
						logger.debug("Processing completed for deletion of external data against entity: ["+ entityClass.getName() +"]");
				}
			} //end of for-loop
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeExternalDataDeletion'. External data deletion completed");
	}
	
	/**
	 * Method that transforms the configured list of template <b><i>DELETE</i></b> clauses set on the external configuration file
	 * and adds it to the ID set of the particular entity NODE to be deleted
	 * @param selectClausesOrdered
	 * The {@link Map} of entity NODE >> list of generated SELECT clauses
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void adjustForConfiguredDeleteClauses(LinkedHashMap<Class, List<String>> selectClausesOrdered) throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'adjustForConfiguredDeleteClauses'");
		
		if(this.jdbcTemplate != null && this.configuredDeleteClauses != null && !this.configuredDeleteClauses.isEmpty() &&
				selectClausesOrdered != null) {
			final String regexPattern1 = "DELETE FROM \\S+ WHERE \\S+ IN \\(SELECT \\S+ FROM \\S+ WHERE \\S+ IN \\(\\{\\{\\S+\\}\\}\\)\\)";
			final String regexPattern2 = "DELETE FROM \\S+ WHERE \\S+ IN \\(SELECT \\S+ FROM \\S+ WHERE \\S+ IN \\([\\d,]+\\)\\)";
			final String regexPattern3 = "DELETE FROM \\S+ WHERE \\S+ IN \\([\\d,]+\\)";
			
			//for each configured DELETE clauses
			for(String configuredDelete : this.configuredDeleteClauses) {
				//checking for pattern 1
				if(Pattern.compile(regexPattern1).matcher(configuredDelete).matches()) {
					if(logger.isDebugEnabled())
						logger.debug("-----Inside method 'adjustForConfiguredDeleteClauses'. SQL clause matches pattern 1");
					
					final String innerSelectClause = configuredDelete.substring((configuredDelete.indexOf("(") + 1),
							configuredDelete.lastIndexOf(")")).trim();
					final String entityName = innerSelectClause.substring((innerSelectClause.indexOf("IN") + 2)).
							trim().replaceAll("[\\(\\{\\}\\)]+", "").trim();
					
					//only if the processing NODE entry matches
					if(selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityName))) != null) {
						String IdSet = "";
						for(String currentIdSet : selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityName)))) {
							IdSet = IdSet.equals("") ? currentIdSet : IdSet.concat("," + currentIdSet);
						}
						IdSet = removeDuplicateIds(IdSet);
						
						String updatedSelectClause = innerSelectClause.replaceAll("\\{\\{\\S+\\}\\}", IdSet);
						updatedSelectClause = updateSQLClausesForLargeInFilters(updatedSelectClause);
						String resultIds = "";
						
						//run the SELECT clause
						String fetchedIds = this.jdbcTemplate.query(updatedSelectClause,
								new ResultSetExtractor<String>() {
		
									@Override
									public String extractData(ResultSet rs) throws SQLException, DataAccessException {
										if(rs.next()) {
											return (rs.getString(1) == null ? "0" : rs.getString(1));
										}
										
										return "0";
									}
						});
						
						if(fetchedIds != null) {
							if(fetchedIds.endsWith(","))
								fetchedIds = fetchedIds.substring(0, (fetchedIds.length() - 1));
							
							resultIds = (resultIds.equals("")) ? fetchedIds : resultIds.concat("," + fetchedIds);
						}
						
						//removing duplicates
						resultIds = removeDuplicateIds(resultIds);
						
						//from the current DELETE clause add IDs to its corresponding list
						final String entityNameForDeletion = configuredDelete.substring(configuredDelete.indexOf("FROM") + 4,
								configuredDelete.indexOf("WHERE")).trim().replaceAll("[\\{\\}]+", "");
						
						//check for Entity's reference in the map
						if(selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))) != null) {
							if(selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).isEmpty())
								selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).add(resultIds);
							else {
								String entityIdSet = selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).get(0);
								
								if(entityIdSet.startsWith(","))
									entityIdSet = entityIdSet.substring(1);
								if(entityIdSet.endsWith(","))
									entityIdSet = entityIdSet.substring(0, (entityIdSet.length() - 1));
								
								entityIdSet = entityIdSet.concat("," + resultIds);
								entityIdSet = removeDuplicateIds(entityIdSet);
								
								//setting the updated list of IDs to the entity
								selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).set(0, entityIdSet);
							}
						}
						else {
							List<String> IdList = new ArrayList<String>();
							IdList.add(resultIds);
							selectClausesOrdered.put(Class.forName(this.packageName.concat("." + entityNameForDeletion)), IdList);
						}
					}
				}
				//checking for pattern 2
				else if(Pattern.compile(regexPattern2).matcher(configuredDelete).matches()) {
					if(logger.isDebugEnabled())
						logger.debug("-----Inside method 'adjustForConfiguredDeleteClauses'. SQL clause matches pattern 2");
					
					final String innerSelectClause = configuredDelete.substring((configuredDelete.indexOf("(") + 1),
							configuredDelete.lastIndexOf(")")).trim();
					
					String updatedSelectClause = updateSQLClausesForLargeInFilters(innerSelectClause);
					String resultIds = "";
					
					//run the SELECT clause
					String fetchedIds = this.jdbcTemplate.query(updatedSelectClause,
							new ResultSetExtractor<String>() {
	
								@Override
								public String extractData(ResultSet rs) throws SQLException, DataAccessException {
									if(rs.next()) {
										return (rs.getString(1) == null ? "0" : rs.getString(1));
									}
									
									return "0";
								}
					});
					
					if(fetchedIds != null) {
						if(fetchedIds.endsWith(","))
							fetchedIds = fetchedIds.substring(0, (fetchedIds.length() - 1));
						
						resultIds = (resultIds.equals("")) ? fetchedIds : resultIds.concat("," + fetchedIds);
					}
					
					//removing duplicates
					resultIds = removeDuplicateIds(resultIds);
					
					//from the current DELETE clause add IDs to its corresponding list
					final String entityNameForDeletion = configuredDelete.substring(configuredDelete.indexOf("FROM") + 4,
							configuredDelete.indexOf("WHERE")).trim().replaceAll("[\\{\\}]+", "");
					
					//check for Entity's reference in the map
					if(selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))) != null) {
						if(selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).isEmpty())
							selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).add(resultIds);
						else {
							String entityIdSet = selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).get(0);
							
							if(entityIdSet.startsWith(","))
								entityIdSet = entityIdSet.substring(1);
							if(entityIdSet.endsWith(","))
								entityIdSet = entityIdSet.substring(0, (entityIdSet.length() - 1));
							
							entityIdSet = entityIdSet.concat("," + resultIds);
							entityIdSet = removeDuplicateIds(entityIdSet);
							
							//setting the updated list of IDs to the entity
							selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).set(0, entityIdSet);
						}
					}
					else {
						List<String> IdList = new ArrayList<String>();
						IdList.add(resultIds);
						selectClausesOrdered.put(Class.forName(this.packageName.concat("." + entityNameForDeletion)), IdList);
					}
				}
				//checking for pattern 3
				else if(Pattern.compile(regexPattern3).matcher(configuredDelete).matches()) {
					if(logger.isDebugEnabled())
						logger.debug("-----Inside method 'adjustForConfiguredDeleteClauses'. SQL clause matches pattern 3");
					
					//from the current DELETE clause add IDs to its corresponding list
					final String entityNameForDeletion = configuredDelete.substring(configuredDelete.indexOf("FROM") + 4,
							configuredDelete.indexOf("WHERE")).trim().replaceAll("[\\{\\}]+", "");
					
					String specifiedIds = configuredDelete.substring((configuredDelete.indexOf("(") + 1),
							configuredDelete.lastIndexOf(")")).trim();
					specifiedIds = removeDuplicateIds(specifiedIds);
					
					//check for Entity's reference in the map
					if(selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))) != null) {
						if(selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).isEmpty())
							selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).add(specifiedIds);
						else {
							String entityIdSet = selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).get(0);
							
							if(entityIdSet.startsWith(","))
								entityIdSet = entityIdSet.substring(1);
							if(entityIdSet.endsWith(","))
								entityIdSet = entityIdSet.substring(0, (entityIdSet.length() - 1));
							
							entityIdSet = entityIdSet.concat("," + specifiedIds);
							entityIdSet = removeDuplicateIds(entityIdSet);
							
							//setting the updated list of IDs to the entity
							selectClausesOrdered.get(Class.forName(this.packageName.concat("." + entityNameForDeletion))).set(0, entityIdSet);
						}
					}
					else {
						List<String> IdList = new ArrayList<String>();
						IdList.add(specifiedIds);
						selectClausesOrdered.put(Class.forName(this.packageName.concat("." + entityNameForDeletion)), IdList);
					}
				}
			} //end of for-loop
		}
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'adjustForConfiguredDeleteClauses'. External DELETE clauses processed");
	}
	
	/**
	 * Method that executes each of the <b><i>DELETE</i></b> clause present on the supplied ordered list of
	 * SQL DELETE clauses generated by the method <b>getOrderedDeleteClauses</b> 
	 * 
	 * <br><br>
	 * <b>Note: This method doesn't takes into account the deletion of external data for configured entities,
	 * as it primarily works on the ordered sequence of the generated (via the <i>getOrderedDeleteClauses</i> method )
	 * SQL DELETE clauses only</b>
	 * <br><br>
	 * 
	 * @param orderedDeleteClauses
	 * The {@link List} of ordered SQL DELETE clauses
	 * @throws Exception
	 */
	public void executeOrderedDeleteClauses(DeleteClausesWrapper deleteClausesWrapper) throws Exception {
		final Date startTime = new Date();
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeOrderedDeleteClauses'. Operated started at: " +
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS Z").format(startTime));
		
		if(deleteClausesWrapper != null && deleteClausesWrapper.getDeleteClauses() != null && !deleteClausesWrapper.getDeleteClauses().isEmpty()) {
			if(this.jdbcTemplate == null)
				throw new Exception("Unexpected error ocurred at method 'executeOrderedDeleteClauses'. JdbcTemplate instance is null");
			
			//execute each SQL DELETE clause
			for(String deleteClause : deleteClausesWrapper.getDeleteClauses()) {
				if(logger.isInfoEnabled())
					logger.info("DELETE clause to be executed: ["+ deleteClause +"]");
				this.jdbcTemplate.execute(deleteClause);
				if(logger.isInfoEnabled())
					logger.info("DELETE clause: ["+ deleteClause +"] executed successfully");
			}
		}
		else
			if(logger.isWarnEnabled())
				logger.warn("-----Inside method 'executeOrderedDeleteClauses'. No DELETE clauses to execute");
		
		final Date endTime = new Date();
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'executeOrderedDeleteClauses'. Operated completed at: "
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS Z").format(endTime)
				+ ". Operation took: " + (endTime.getTime() - startTime.getTime()) + "ms");
	}
	
	/**
	 * Removes any duplicate Ids on the supplied comma separated ID list
	 * @param commaSeparatedIds
	 * The comma separated ID values as received from a SQL select clause operation
	 * @return
	 * Comma separated ID values without duplicates
	 */
	private String removeDuplicateIds(String commaSeparatedIds) {
		if(logger.isDebugEnabled())
			logger.debug("-----Inside method 'removeDuplicateIds'");
		
		String updateIdList = "";
		if(commaSeparatedIds == null || commaSeparatedIds.isEmpty() || commaSeparatedIds.equals("0"))
			updateIdList = "0";
		else {
			Matcher matcher = Pattern.compile("[0,]+").matcher(commaSeparatedIds);
			if(matcher.matches())
				updateIdList = "0";
			else {
				List<String> IdList = new ArrayList<>(Arrays.asList(commaSeparatedIds.split(",")));
				
				//until list is empty
				while(!IdList.isEmpty()) {
					String lastProcessedId = "-1";
					for(String IdValue : IdList) {
						lastProcessedId = IdValue;
						updateIdList = (updateIdList.equals("")) ? IdValue : updateIdList.concat("," + IdValue);
						break;
					}  //check for each ID value
					
					final String copyLastProcessedId = lastProcessedId;
					IdList.removeIf(idVal -> idVal.equals(copyLastProcessedId));
				}
			}
		}
		
		return updateIdList;
	}
	
	/**
	 * Updates the SQL clause containing IN clauses by breaking larger set of IN values into
	 * sub-filters where each IN contains no more than <b>100</b> values
	 * @param originalSQLClause
	 * The original SQL clause to be updated
	 * @return
	 * The updated SQL clause
	 * @throws Exception
	 */
	private String updateSQLClausesForLargeInFilters(String originalSQLClause) throws Exception {
		if(logger.isDebugEnabled())
			logger.debug("-----Inside method 'updateSQLClausesForLargeInFilters'");
		
		String updatedClause = "";
		final int MAX_IN_FILTER_SIZE = 100;  //SQL supports upto 1000 values within the IN clause, using long IN list will result in performance hit
		
		if(originalSQLClause != null && !originalSQLClause.isEmpty()) {
			if(logger.isInfoEnabled())
				logger.info("-----Inside method 'updateSQLClausesForLargeInFilters'. SQL clause to update: \n ["+ originalSQLClause +"]");
			
			//get part of clause until WHERE
			final String partTillWhere = originalSQLClause.substring(0, (originalSQLClause.indexOf("WHERE") + 5));
			final String partBtwWhereAndIn = originalSQLClause.substring((originalSQLClause.indexOf("WHERE") + 5),
					originalSQLClause.indexOf("IN"));
			final String partInFilter = originalSQLClause.substring((originalSQLClause.indexOf("IN (") + 4),
					originalSQLClause.lastIndexOf(")"));
			
			final List<String> IdValList = new ArrayList<>(Arrays.asList(partInFilter.split(",")));
			
			//determining number of sub-lists needed
			final int totalSubListsReq = ((IdValList.size() % MAX_IN_FILTER_SIZE) != 0) ? ((int)(IdValList.size() / MAX_IN_FILTER_SIZE) + 1)
					: ((int)(IdValList.size() / MAX_IN_FILTER_SIZE));
			for(int i = 0; i < totalSubListsReq; i++) {
				int lastLimit = (((i + 1) * MAX_IN_FILTER_SIZE) > IdValList.size()) ? IdValList.size() : ((i + 1) * MAX_IN_FILTER_SIZE);
				final List<String> derivedIdSubList = IdValList.subList((i * MAX_IN_FILTER_SIZE), lastLimit);
				
				String commaSepIds = Arrays.deepToString(derivedIdSubList.toArray(new String[derivedIdSubList.size()]));
				commaSepIds = commaSepIds.replace("[", "").replace("]", "").replaceAll("\\s+", "");
				
				updatedClause = (updatedClause.equals("")) ? (" IN (" + commaSepIds + ")") :
					updatedClause.concat(" OR " + partBtwWhereAndIn + " IN (" + commaSepIds + ")");
			}
			
			//final updated clause
			updatedClause = partTillWhere + " " + partBtwWhereAndIn + " " + updatedClause;
			updatedClause = updatedClause.replaceAll("\\s+", " ");
			if(logger.isInfoEnabled())
				logger.info("-----Inside method 'updateSQLClausesForLargeInFilters'. Updated SQL clause: \n ["+ updatedClause +"]");
		}
		
		return updatedClause;
	}
	
	/**
	 * Method to download the entity relation hierarchy as JSON
	 * @param orderedNodeList
	 * The ordered list of {@link EntityNode} that builds up the hierarchy
	 * @return
	 * The content as {@link ByteArrayResource}
	 * @throws Exception
	 */
	public ByteArrayResource downloadEntityHierarchyJSON() throws Exception {
		if(logger.isInfoEnabled())
			logger.info("-----Inside method 'downloadEntityHierarchyJSON'");
		
		List<EntityNode> orderedNodeList = buildEntityRelationHierarchy();
		
		if(orderedNodeList != null && !orderedNodeList.isEmpty()) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			new ObjectMapper().writeValue(bos, orderedNodeList);
			return new ByteArrayResource(bos.toByteArray());
		}
		else
			throw new Exception("Failed to download Entity Hierarchy JSON as supplied list is null/empty");
	}
	
	/**
	 * Utility method for creating a deep copy of the supplied list of {@link EntityNode} 
	 * @param originalOrderedEntityNodes
	 * Original list of {@link EntityNode}
	 * @return
	 * Copy of the supplied list
	 * @throws Exception
	 */
	private List<EntityNode> createCopyEntityNodeHierarchy(List<EntityNode> originalOrderedEntityNodes) throws Exception {
		if(logger.isDebugEnabled())
			logger.debug("-----Inside method 'createCopyEntityNodeHierarchy'");
		
		if(originalOrderedEntityNodes != null && !originalOrderedEntityNodes.isEmpty()) {
			//creating a copy of the ordered entity hierarchy
			List<EntityNode> copyNodeList = new ArrayList<>();
			for(EntityNode en : originalOrderedEntityNodes) {
				EntityNode entityNodeCopy = new EntityNode();
				entityNodeCopy.setUUID(en.getUUID());
				entityNodeCopy.setClazz(en.getClazz());
				entityNodeCopy.setRank(en.getRank());
				entityNodeCopy.setParentNodes(new ArrayList<EntityNode>());
				entityNodeCopy.setRelationEqualityList(new ArrayList<String>());
				
				//for relation equality list
				List<String> enNodeCpEqList = new ArrayList<>();
				for(String relationEq : en.getRelationEqualityList()) {
					enNodeCpEqList.add(relationEq);
				}
				entityNodeCopy.getRelationEqualityList().addAll(enNodeCpEqList);
				
				//adding the NODE copy to the output list
				copyNodeList.add(entityNodeCopy);
				
				//recursively scan & copy for each parents & their further parents
				if(en.getParentNodes() != null && !en.getParentNodes().isEmpty()) {
					copyParentNodesOfGivenNode(entityNodeCopy, en);
				}
			}
			
			return copyNodeList;
		}
		else
			return null;
	}
	
	/**
	 * Recursively copies all the parent & their further parent NODEs of the given entity NODE
	 * @param copiedNode
	 * The copy instance of the current processing node
	 * @param originalNode
	 * The original instance of the current processing node from which the copy is made
	 * @throws Exception
	 */
	private void copyParentNodesOfGivenNode(EntityNode copiedNode, EntityNode originalNode) throws Exception {
		if(logger.isDebugEnabled())
			logger.debug("-----Inside method 'copyParentNodesOfGivenNode'");
		
		if(copiedNode == null || originalNode == null)
			throw new Exception("Exception ocurred at method 'copyParentNodesOfGivenNode'. Supplied copyNode or originalNode instance is null");
		
		//for each available parent NODEs to current processing NODE
		for(EntityNode parentNode : originalNode.getParentNodes()) {
			EntityNode parentCopyNode = new EntityNode();
			parentCopyNode.setUUID(parentNode.getUUID());
			parentCopyNode.setClazz(parentNode.getClazz());
			parentCopyNode.setRank(parentNode.getRank());
			parentCopyNode.setParentNodes(new ArrayList<EntityNode>());
			parentCopyNode.setRelationEqualityList(new ArrayList<String>());
			
			//for relation equality list
			List<String> parentNodeCpEqList = new ArrayList<>();
			for(String relationEq : parentNode.getRelationEqualityList()) {
				parentNodeCpEqList.add(relationEq);
			}
			parentCopyNode.getRelationEqualityList().addAll(parentNodeCpEqList);
			
			//adding the parent NODE's copy to the current processing NODE's parent list
			copiedNode.getParentNodes().add(parentCopyNode);
			
			//recursively scan & copy for each parents & their further parents
			if(parentNode.getParentNodes() != null && !parentNode.getParentNodes().isEmpty()) {
				copyParentNodesOfGivenNode(parentCopyNode, parentNode);
			}
		}
	}
	
	//code for testing  >> uncomment below for local testing
//	public static void main(String[] args) {
//		try {
//			EntityRelationBuilder entityRelationBuilder = new EntityRelationBuilder();
//			String excludeNames = "DomainValue,DomainValueType,DomainValueRelation,DomainValueTypeRelationship,DomainValueDescription,Locale,User,"
//					+ "Role,Group,Permission,RolePermission,GroupRole,UserGroup,Queue,QueueGroup,QrtzBlobTriggers,QrtzBlobTriggersId,"
//					+ "QrtzCalendars,QrtzCalendarsId,QrtzCronTriggers,QrtzCronTriggersId,QrtzFiredTriggers,QrtzFiredTriggersId,"
//					+ "QrtzJobDetails,QrtzJobDetailsId,QrtzLocks,QrtzLocksId,QrtzPausedTriggerGrps,QrtzPausedTriggerGrpsId,"
//					+ "QrtzSchedulerState,QrtzSchedulerStateId,QrtzSimpleTriggers,QrtzSimpleTriggersId,QrtzSimpropTriggers,"
//					+ "QrtzSimpropTriggersId,QrtzTriggers,QrtzTriggersId,ApplicationSequenceNumber,DuplicateArirejections,"
//					+ "Producttype,Product,Brodrepo,PodsrepoOne,PodsrepoTwo,SchemaVersion,Podscontroller,PodsbatchHistory,"
//					+ "FawbPropertySource,CustomerLink";
//			String[] exclude = excludeNames.split(",");
//			List<EntityNode> response = entityRelationBuilder.buildEntityRelationHierarchy("com.fico.dmp.core", "Application",
//					Arrays.asList(exclude));
//			
//			IOUtils.write(new ObjectMapper().writeValueAsString(response),
//					new FileOutputStream(new File("C:/From D5ZFL9H2/Documents and Projects/PS Harvesting-OM/Relation.json")), 
//					StandardCharsets.UTF_8);
//		}
//		catch (Exception e) {
//			System.err.println(e);
//		}
//	}
	
}
