package com.fico.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fico.ps.exception.WorkFlowException;
import com.fico.pscomponent.handlers.DMPAuthenticationHandler;
import com.fico.pscomponent.handlers.PropertiesHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

/**
 * Class combining the required common utility functionalities to be available
 * under a single place
 * 
 * @author AnubhavDas
 */
@Service("core.CommonUtils")
public class CommonUtils {

	@Autowired
	private DMPAuthenticationHandler dmpAuthenticationHandler;

	@Autowired
	private PropertiesHandler propertiesHandler;

	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	/**
	 * Converts all the {@link String} type properties/attributes of the given list
	 * of objects of the specified class from <b>null</b> to an empty string.
	 * Fetches the declared methods on this class that are prefixed with <b>get</b>
	 * and uses the corresponding <b>set</b> method to update the values. <br>
	 * <b>Note: Considers only properties that are tagged with getters and
	 * setters</b>
	 * 
	 * @param <T>      the type for which the data is processed & returned
	 * @param clazz    the class of the objects to process
	 * @param dataList the list of the specified class
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> setNullToEmptyForStringProperties(Class<T> clazz, List<T> dataList) throws Exception {
		logger.info("-----Inside method 'setNullToEmptyForStringProperties'");

		List<T> modifiedList = dataList;

		if (modifiedList == null || modifiedList.isEmpty() || clazz == null)
			;
		else {
			// get all declared methods on this class
			Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(clazz);

			if (declaredMethods != null && declaredMethods.length != 0) {
				Object[] rawfilteredMethods = Arrays.asList(declaredMethods).stream()
						.filter(method -> method.getName().startsWith("get")).toArray();
				Method[] filteredMethods = Arrays.copyOf(rawfilteredMethods, rawfilteredMethods.length, Method[].class);
				final List<Method> methods = Arrays.asList(filteredMethods);
			

				logger.debug("---- Processing contents of list ----");
				for (T item : modifiedList) {
					// for each filtered methods above
					logger.debug("Record processing started (for setting 'null' valued string fields to empty string)");
					for (Method method : methods) {
						try {
							if ((method.invoke(item) == null)
									&& method.getReturnType().getSimpleName().equals("String")) {
								logger.debug(
										">> Checking if setter method: " + "[set".concat(method.getName().substring(3))
												+ "] exists for field '" + method.getName().substring(3) + "'");

								Method setMethod = ReflectionUtils.findMethod(clazz,
										"set".concat(method.getName().substring(3)), String.class);
								if (setMethod != null) {
									logger.debug("Setting 'null' value to empty string");
									setMethod.invoke(item, "");
								} else {
									logger.debug("Setter method was not found for this field!");
								}
							} // null value check (on field)
						} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException e) {
							logger.error("FAILED to access data for field " + (method.getName().substring(3)), e);
						}
					}
					logger.debug("Record processing completed");
				} // list iteration (over item records)

			} // declared methods check
		}

		logger.info("----Processing completed for setting 'null' string properties to empty string");
		return modifiedList;
	}

	/**
	 * Authenticate on the DMP platform and return the token associated
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAuthToken() throws Exception {
		// final String endpoint =
		// "https://iam-svc.dms.preview.usw2.ficoanalyticcloud.com/registration/rest/client/token";
		final String endpoint = propertiesHandler.getPropertyValueByName("DMP_TOKEN_JWT_URL");
		final String clientId = propertiesHandler.getPropertyValueByName("DMP_TOKEN_CLIENT_ID");
		final String secret = propertiesHandler.getPropertyValueByName("DMP_TOKEN_CLIENT_SECRET");
		final String json = "{\"clientId\": \"" + clientId + "\",\"secret\": \"" + secret + "\"}";
		final StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost httpRequest = new HttpPost(endpoint);

		httpRequest.addHeader("Accept", "*/*");
		// httpRequest.addHeader("Authorization", "Bearer " + jwtToken);
		httpRequest.setEntity(entity);

		logger.warn("Invoking DMP Auth Service...");
		HttpResponse output = client.execute(httpRequest);
		logger.warn("Invoked DMP Auth Service. Status code: {}", output.getStatusLine().getStatusCode());
		String res = readResponse(output.getEntity().getContent());
		logger.warn(res);
		logger.warn("----" + output.toString());

		return res;
	}

	/**
	 * Just get the associate process url defined on as a site property
	 * 
	 * @return
	 */
	public String getProcessURL() {
		String processURL = propertiesHandler.getPropertyValueByName("PROCESS_URL");
		if (StringUtils.isEmpty(processURL)) {
			logger.error("PLEASE DEFINE THE SITE PROPERTY 'PROCESS_URL'");
			throw new RuntimeException("'PROCESS_URL' not defined");
		} else if (!processURL.startsWith("https")) {
			processURL = "https://" + processURL;
		}
		return processURL;
	}

	/**
	 * Submits a HTTP get request
	 * 
	 * @param urlTemplate
	 * @param jwtToken
	 * @param isTestCall
	 * @return
	 * @throws Exception
	 */
	public String callHttpGetRequest(String urlTemplate, String jwtToken, boolean isTestCall) throws Exception {
		logger.info("Calling the url {}", urlTemplate);
		final String token = isTestCall ? getAuthToken()
				: (jwtToken == null || jwtToken.isEmpty() ? dmpAuthenticationHandler.getJWTToken() : jwtToken);
		logger.warn("DMP Token {}", token);

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpGet httpRequest = new HttpGet(urlTemplate);
		httpRequest.addHeader("Accept", "*/*");
		httpRequest.addHeader("Authorization", "Bearer " + token);

		logger.warn("Invoking Service...");
		final HttpResponse output = client.execute(httpRequest);
		logger.warn("Invoked Service. Status code: {}", output.getStatusLine().getStatusCode());
		if (output.getStatusLine().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("500", "Internal Server Error");
			throw new WorkFlowException("500" + "#" + "Internal Server Error");
		}
		if (output.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
			com.fico.ps.model.Error error = new com.fico.ps.model.Error("400", "Internal Server Error");
			throw new WorkFlowException("400" + "#" + "Internal Server Error");
		}

		final String res = readResponse(output.getEntity().getContent());
		logger.warn(res);
		logger.warn("----" + output.toString());

		return res;
	}

	/**
	 * Convert InputStream to String
	 * 
	 * @param response
	 * @return
	 */
	public String readResponse(InputStream response) {
		StringBuffer result = new StringBuffer();
		try {
			if (response != null) {

				BufferedReader rd = new BufferedReader(new InputStreamReader(response));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
			}

		} catch (IOException e) {
			if(logger.isWarnEnabled())
				logger.warn(e.getMessage());
		}

		if(logger.isDebugEnabled())
			logger.debug("result in readresponse:::::::::::::" + result);

		return result.toString();
	}

	/**
	 * Utility method to determine whether a given column under a given DB table is
	 * a <b>foreign</b> column/not
	 * 
	 * @param DBTableName         The exact name of the table in the DB
	 * @param columnName          The exact name of the column that needs to be
	 *                            determined
	 * @param packageName         The fully qualified package name with dot
	 *                            <b>(.)</b> notation
	 * @param deepScan            Enable this if sub-directories also need to be
	 *                            scanned
	 * @param packageParentFolder Note: <b>optional</b> -> specify the relative
	 *                            parent directory path of this package if any
	 *                            (without a starting <b>'/'</b>)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class<?> isForeignColumnReference(String DBTableName, String columnName, String packageName,
			boolean deepScan, String... packageParentFolder) {
		logger.info("-----Inside method 'isColumnReferenceDomainValue'");

		Class<?> parentClass = null;
		try {
			if (DBTableName != null && !DBTableName.isEmpty() && columnName != null && !columnName.isEmpty()
					&& packageName != null && !packageName.isEmpty()) {
				final List<Class> allClassesUnderPkg = listAllClassesUnderPackage(packageName, deepScan,
						packageParentFolder);

				if (allClassesUnderPkg != null && !allClassesUnderPkg.isEmpty()) {
					Stream<Class> matchingClassStream = allClassesUnderPkg.stream()
							.filter(clazz -> AnnotationUtils.findAnnotation(clazz, Table.class) != null
									&& AnnotationUtils.findAnnotation(clazz, Table.class).name().replace("`", "")
											.replace("`", "").equals(DBTableName));

					// if there is a match with the supplied DBTableName
					if (matchingClassStream != null && matchingClassStream.count() == 1) {
						final Class matchingClass = matchingClassStream.findFirst().get();
						final String capitalizedColName = columnName.substring(0, 1).toUpperCase()
								.concat(columnName.substring(1));
						final Method decGetMethod = Arrays.asList(matchingClass.getDeclaredMethods()).stream()
								.filter(method -> method.getName().equals("get".concat(capitalizedColName))).findFirst()
								.get();

						// if a get method exists on the supplied DB column
						if (decGetMethod != null) {
							// check if there is a get DomainValue method on this column
							Stream<Method> getForeignMethodStream = Arrays.asList(matchingClass.getDeclaredMethods())
									.stream()
									.filter(method -> AnnotationUtils.findAnnotation(decGetMethod,
											JoinColumn.class) != null
											&& AnnotationUtils.findAnnotation(decGetMethod, JoinColumn.class).name()
													.replace("`", "").replace("`", "").equals(columnName));

							// if such a DV get method exists for this column
							if (getForeignMethodStream != null && getForeignMethodStream.count() == 1) {
								parentClass = getForeignMethodStream.findFirst().get().getReturnType();
							}
						}
					}
				}
			} else
				throw new Exception("The parameters 'DBTableColumn', 'columnName' & 'packageName' are mandatory");
		} catch (Exception e) {
			logger.error("Unexpected error occurred at method 'isColumnReferenceDomainValue'", e);
			parentClass = null;
		}

		return parentClass;
	}

	/**
	 * Utility method to determine whether a given column under a given DB table is
	 * a <b>foreign</b> column/not
	 * 
	 * @param DBTableName         The exact name of the table in the DB
	 * @param columnName          The exact name of the column that needs to be
	 *                            determined
	 * @param packageName         The fully qualified package name with dot
	 *                            <b>(.)</b> notation
	 * @param typeAnnotationClass Optional >> the annotation class to be scanned if
	 *                            provided any
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class<?> isForeignColumnReference(String DBTableName, String columnName, String packageName,
			Class<? extends Annotation> typeAnnotationClass) {
		logger.info("-----Inside method 'isColumnReferenceDomainValue'");

		Class<?> parentClass = null;
		try {
			if (DBTableName != null && !DBTableName.isEmpty() && columnName != null && !columnName.isEmpty()
					&& packageName != null && !packageName.isEmpty()) {
				final List<Class> allClassesUnderPkg = listAllClassesUnderPackage(packageName, typeAnnotationClass);

				if (allClassesUnderPkg != null && !allClassesUnderPkg.isEmpty()) {
					Stream<Class> matchingClassStream = allClassesUnderPkg.stream()
							.filter(clazz -> AnnotationUtils.findAnnotation(clazz, Table.class) != null
									&& AnnotationUtils.findAnnotation(clazz, Table.class).name().replace("`", "")
											.replace("`", "").equals(DBTableName));

					// if there is a match with the supplied DBTableName
					if (matchingClassStream != null && matchingClassStream.count() == 1) {
						final Class matchingClass = allClassesUnderPkg.stream()
								.filter(clazz -> AnnotationUtils.findAnnotation(clazz, Table.class) != null
								&& AnnotationUtils.findAnnotation(clazz, Table.class).name().replace("`", "")
										.replace("`", "").equals(DBTableName)).findFirst().get();
						final String capitalizedColName = columnName.substring(0, 1).toUpperCase()
								.concat(columnName.substring(1));
						final Method decGetMethod = Arrays.asList(matchingClass.getDeclaredMethods()).stream()
								.filter(method -> method.getName().equals("get".concat(capitalizedColName))).findFirst()
								.get();

						// if a get method exists on the supplied DB column
						if (decGetMethod != null) {
							// check if there is a get DomainValue method on this column
							Stream<Method> getForeignMethodStream = Arrays.asList(matchingClass.getDeclaredMethods())
										.stream().filter(method -> AnnotationUtils.findAnnotation(method,
											JoinColumn.class) != null &&
											AnnotationUtils.findAnnotation(method, JoinColumn.class).name()
													.replace("`", "").replace("`", "").equals(columnName));

							// if such a DV get method exists for this column
							if (getForeignMethodStream != null && getForeignMethodStream.count() == 1) {
								parentClass = Arrays.asList(matchingClass.getDeclaredMethods())
										.stream().filter(method -> AnnotationUtils.findAnnotation(method,
												JoinColumn.class) != null &&
												AnnotationUtils.findAnnotation(method, JoinColumn.class).name()
														.replace("`", "").replace("`", "").equals(columnName))
															.findFirst().get().getReturnType();
							}
						}
					}
				}
			} else
				throw new Exception("The parameters 'DBTableColumn', 'columnName' & 'packageName' are mandatory");
		} catch (Exception e) {
			logger.error("Unexpected error occurred at method 'isColumnReferenceDomainValue'", e);
			parentClass = null;
		}

		return parentClass;
	}

	/**
	 * Method to fetch all the classes under a given package (uses {@link Reflections} library)
	 * 
	 * @param packageName         The fully qualified package name with dot
	 *                            <b>(.)</b> notation
	 * @param typeAnnotationClass Optional >> the annotation class to be scanned if
	 *                            provided any
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Class> listAllClassesUnderPackage(String packageName, Class<? extends Annotation> typeAnnotationClass) {
		logger.info("-----Inside method 'listAllClassesUnderPackage'");

		try {
			List<Class> classes = new ArrayList<>();
			if (typeAnnotationClass != null) {
				Reflections ref = new Reflections(new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage("com.fico.dmp.core"))
						.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.fico.dmp.core"))));
				
				classes = ref.getTypesAnnotatedWith(typeAnnotationClass).stream().collect(Collectors.toList());
				logger.info("-----Inside method 'listAllClassesUnderPackage'. Total classes found: " + classes.size());
				
				return classes;
			}
			else {
				Reflections ref = new Reflections(new ConfigurationBuilder()
						.setScanners(new SubTypesScanner(false))
						.setUrls(ClasspathHelper.forPackage("com.fico.dmp.core"))
						.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.fico.dmp.core"))));
				
				classes = ref.getSubTypesOf(Object.class).stream().collect(Collectors.toList());
				logger.info("-----Inside method 'listAllClassesUnderPackage'. Total classes found: " + classes.size());
				
				return classes;
			}
		} catch (Exception e) {
			logger.error("Unexpected error occurred at method 'listAllClassesUnderPackage'", e);
			return null;
		}
	}

	/**
	 * Method to fetch all the classes under a given package (Uses {@link File}
	 * approach for scanning of classes)
	 * 
	 * @param packageName         The fully qualified package name with dot
	 *                            <b>(.)</b> notation
	 * @param deepScan            Enable this if sub-directories also need to be
	 *                            scanned
	 * @param packageParentFolder Note: <b>optional</b> -> specify the relative
	 *                            parent directory path of this package if any
	 *                            (without a starting <b>'/'</b>)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Class> listAllClassesUnderPackage(String packageName, boolean deepScan, String... packageParentFolder) {
		logger.info("-----Inside method 'listAllClassesUnderPackage'");

		try {
			List<Class> classList = new ArrayList<>();
			File packageDir = new File(
					System.getProperty("user.dir").replaceAll("\\\\", "/") + File.separator
							+ ((packageParentFolder != null && packageParentFolder.length > 0)
									? ((packageParentFolder[0].startsWith("/") ? packageParentFolder[0].substring(1)
											: packageParentFolder[0]) + File.separator)
									: "")
							+ packageName.replaceAll("[.]", "/"));

			findClass(packageDir, packageName, classList, deepScan);
			logger.info("-----Inside method 'listAllClassesUnderPackage'. Total classes found: " + classList.size());
			return classList;
		} catch (Exception e) {
			logger.error("Unexpected error occurred at method 'listAllClassesUnderPackage'", e);
			return null;
		}
	}

	/**
	 * Fetches the {@link Class} from the supplied class name & package name
	 * 
	 * @param className   The name of the class as <b>SomeClass.class</b>
	 * @param packageName The fully qualified package name with dot <b>(.)</b>
	 *                    notation
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class getClass(String className, String packageName) {
		logger.debug("-----Inside method 'getClass'");

		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
			logger.error("Unexpected error occurred at method 'getClass'", e);
		}
		return null;
	}

	/**
	 * Finds all the classes under a given package directory & adds to the list
	 * supplied. If deepscan is enabled, recursive scan happens at the nested
	 * sub-directories as well
	 * 
	 * @param dir         The File representing the package
	 * @param packageName The name of the package
	 * @param clazzes     List where to add classes
	 * @param deepScan    Enable this to perform sub-directories scan
	 */
	@SuppressWarnings("rawtypes")
	private void findClass(File dir, String packageName, List<Class> clazzes, boolean deepScan) throws Exception {
		logger.debug("-----Inside methiod 'findClass'");

		if (dir == null)
			throw new FileNotFoundException("Supplied file/directory is null");
		else if (dir != null && !dir.exists())
			throw new FileNotFoundException(
					"Supplied file/directory does not exists at path: [" + dir.getAbsolutePath() + "]");

		List<File> filesUnderPkg = Arrays.asList(dir.listFiles());
		for (File f : filesUnderPkg) {
			if (!f.isDirectory() && f.getName().endsWith(".java")) {
				logger.debug("Processing class file: " + f.getName());
				final Class clazz = getClass(f.getName(), packageName);
				clazzes.add(clazz);
				logger.debug("Obtained class: " + clazz.getName());
			} else if (deepScan && f.isDirectory())
				findClass(f, (packageName + "." + f.getName()), clazzes, deepScan);
		}
	}

}
