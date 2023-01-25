package com.fico.ps.hermes.service;

import com.fico.ps.hermes.save.ResponseSave;
import com.fico.ps.hermes.save.Error;

/**
 * @author MushfikKhan
 *
 */
public interface IDomainModelSave {
	/**
	 * @param <T>
	 * @param original
	 * @param modified
	 * @param params : (boolean) params[0] -> is sensitivity check required? Default value is 'true'
	 * @param params : (String) params[1] -> model config name. Default value is 'defaultModelConfig'
	 * @return
	 * @throws Exception
	 */
	public <T> ResponseSave<T, Error>  save(T original, T modified, Object... objects) throws Exception;

}
