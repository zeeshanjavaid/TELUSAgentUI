/**
 * 
 */
package com.fico.ps.hermes.hook;

import com.fico.ps.hermes.save.Error;

/**
 * @author MushfikKhan
 *
 */
public interface ICustomValidator {
	
	public <T> Error validate(T objectToValidate);

}
