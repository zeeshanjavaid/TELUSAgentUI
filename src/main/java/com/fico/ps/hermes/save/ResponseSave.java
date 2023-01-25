package com.fico.ps.hermes.save;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.lang.Nullable;

/**
 * @author MushfikKhan
 *
 * @param <T>
 * @param <R>
 */
public class ResponseSave<T, R> {
	
	private ResponseStatus status;
	
	@Nullable
	private T response;
	
	private Map<String, Boolean> sensitiveFlagMap;
	
	private Map<String, List<SensitiveDataChange>> sensitiveDataChangeMap;
	
	@Nullable
	private R error;
	
	public ResponseSave(T response, ResponseStatus status) {
		this.status = status;
		this.response = response;
	}

	public ResponseSave(T response, R error, ResponseStatus status) {
		this.status = status;
		this.response = response;
		this.error = error;
	}

	/**
	 * @return the status
	 */
	public ResponseStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	/**
	 * @return the response
	 */
	public T getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(T response) {
		this.response = response;
	}

	/**
	 * @return the error
	 */
	public R getError() {
		return error;
	}

	/**
	 * @return the sensitiveFlagMap
	 */
	public Map<String, Boolean> getSensitiveFlagMap() {
		return this.sensitiveFlagMap;
	}
	
	/**
	 * @return the sensitiveFlagMap the sensitiveFlagMap to set
	 */
	public void setSensitiveFlagMap(Map<String, Boolean> sensitiveFlagMap) {
		this.sensitiveFlagMap = sensitiveFlagMap;
	}

	/**
	 * @return the sensitiveDataChangeMap
	 */
	public Map<String, List<SensitiveDataChange>> getSensitiveDataChangeMap() {
		return sensitiveDataChangeMap;
	}

	/**
	 * @param sensitiveDataChangeMap the sensitiveDataChangeMap to set
	 */
	public void setSensitiveDataChangeMap(Map<String, List<SensitiveDataChange>> sensitiveDataChangeMap) {
		this.sensitiveDataChangeMap = sensitiveDataChangeMap;
		if (sensitiveDataChangeMap != null) {
			Map<String, Boolean> sensitiveFlagMap = null;
			for (Entry<String, List<SensitiveDataChange>> entry : sensitiveDataChangeMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().size() > 0) {
					if (sensitiveFlagMap == null) {
						sensitiveFlagMap = new HashMap<String, Boolean>();
					}
					sensitiveFlagMap.put(entry.getKey(), true);
				}
			}
			setSensitiveFlagMap(sensitiveFlagMap);
		}
	}
}
