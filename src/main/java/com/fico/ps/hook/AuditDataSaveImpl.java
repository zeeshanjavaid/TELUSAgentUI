package com.fico.ps.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fico.dmp.telusagentuidb.service.AuditDataChangeService;
import com.fico.ps.hermes.audit.AuditData;
import com.fico.ps.hermes.audit.IAuditDataSave;
import com.fico.ps.model.ApplicationVO;

/**
 * @author MushfikKhan
 *
 */
@Component("Hermes.AuditDataSave")
public class AuditDataSaveImpl implements IAuditDataSave {

	private static Logger logger = LoggerFactory.getLogger(AuditDataSaveImpl.class);
	
	@Autowired
	@Qualifier("TELUSAgentUIDB.AuditDataChangeService")
	private AuditDataChangeService auditDataChangeService;
	
	@Override
	public boolean save(Object savedRootObject, AuditData auditData) {
		boolean success = false;
		try {
			ApplicationVO application = (ApplicationVO) savedRootObject;
			com.fico.dmp.telusagentuidb.AuditDataChange auditDataChangeDao = new com.fico.dmp.telusagentuidb.AuditDataChange();
			auditDataChangeDao.setAction(auditData.getAction().name());
			auditDataChangeDao.setApplicationId(application.getId());
			auditDataChangeDao.setChangeData(auditData.getChangeData());
			auditDataChangeDao.setCreatedBy(auditData.getCreatedBy());
			auditDataChangeDao.setCreatedOn(auditData.getCreatedOn());
			auditDataChangeDao.setDomainClassName(auditData.getDomainClassName());
			auditDataChangeDao.setEntityId(auditData.getEntityId() != null && auditData.getEntityId() instanceof Integer ?  (Integer) auditData.getEntityId() : null);
			auditDataChangeDao.setEntityName(auditData.getEntityName());
			auditDataChangeDao.setParentDomainClassName(auditData.getParentDomainClassName());
			auditDataChangeDao.setParentEntityName(auditData.getParentEntityName());
			auditDataChangeDao.setParentEntityId(auditData.getParentEntityId() != null && auditData.getParentEntityId() instanceof Integer ? (Integer) auditData.getParentEntityId() : null);
			
			auditDataChangeService.create(auditDataChangeDao);
			success = true;
			//logger.info("Successfully saved audit data : {}", auditDataChangeDao);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return success;
	}

	@Override
	public boolean save(AuditData auditData, Integer applicationId) {
		boolean success = false;
		try {
			com.fico.dmp.telusagentuidb.AuditDataChange auditDataChangeDao = new com.fico.dmp.telusagentuidb.AuditDataChange();
			auditDataChangeDao.setAction(auditData.getAction().name());
			auditDataChangeDao.setApplicationId(applicationId);
			auditDataChangeDao.setChangeData(auditData.getChangeData());
			auditDataChangeDao.setCreatedBy(auditData.getCreatedBy());
			auditDataChangeDao.setCreatedOn(auditData.getCreatedOn());
			auditDataChangeDao.setDomainClassName(auditData.getDomainClassName());
			auditDataChangeDao.setEntityId(auditData.getEntityId() != null && auditData.getEntityId() instanceof Integer ?  (Integer) auditData.getEntityId() : null);
			auditDataChangeDao.setEntityName(auditData.getEntityName());
			auditDataChangeDao.setParentDomainClassName(auditData.getParentDomainClassName());
			auditDataChangeDao.setParentEntityName(auditData.getParentEntityName());
			auditDataChangeDao.setParentEntityId(auditData.getParentEntityId() != null && auditData.getParentEntityId() instanceof Integer ? (Integer) auditData.getParentEntityId() : null);

			auditDataChangeService.create(auditDataChangeDao);
			success = true;
			//logger.info("Successfully saved audit data : {}", auditDataChangeDao);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return success;
	}

}
