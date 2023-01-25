package com.fico.ps.hermes.audit;

/**
 * @author MushfikKhan
 *
 */
public interface IAuditDataSave {
	
	/**
	 * @param savedRootObject
	 * @param auditData
	 * @return
	 */
	public boolean save(Object savedRootObject, AuditData auditData);

	public boolean save(AuditData auditData, Integer applicationId);

}
