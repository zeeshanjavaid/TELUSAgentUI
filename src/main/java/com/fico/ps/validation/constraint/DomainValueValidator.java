package com.fico.ps.validation.constraint;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.fico.core.services.DomainValueServiceBS;

import com.fico.dmp.telusagentuidb.models.query.QueryGetAllDomainValuesByDvTypeCodeResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainValueValidator implements ConstraintValidator<DomainValue, Object> {


    private static final Logger logger = LoggerFactory.getLogger(DomainValueValidator.class);
	
	
    @Autowired
    @Qualifier("facade.DomainValueServiceBS")
    private DomainValueServiceBS domainValueServiceBS;
        
 	private String domainValueTypeCode;
 	private boolean required;
 
    
    @Override
    public void initialize(DomainValue constraintAnnotation) {
        
        logger.info("Initializing Constraint Validation for Domain Value Type Code: " + constraintAnnotation.domainValueTypeCode() + " Required:" + constraintAnnotation.required());
    	
    	this.domainValueTypeCode=constraintAnnotation.domainValueTypeCode();
    	this.required=constraintAnnotation.required();
    }
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		
		logger.debug("Domain Value Validation. Type Code:" + this.domainValueTypeCode);
		
		
		//Check if required and populated.
		if (value==null)
		       return !this.required;
		
		if (value instanceof String  && ((String)value).isEmpty())
		       return !this.required;
		        
		if (value instanceof Integer  && ((Integer)value)==0)
		       return !this.required;
		            
		if (value instanceof Long  && ((Long)value).equals(0L))
		       return !this.required;
		       
		
		try {
		    
		    
		    //Fetch DomainValues from existing cache.
		    List<QueryGetAllDomainValuesByDvTypeCodeResponse> domainValues= domainValueServiceBS.getAllDomainValuesByDVTypeCode(domainValueTypeCode,"en",true);
		    
		    for (QueryGetAllDomainValuesByDvTypeCodeResponse dv:domainValues) {
		  
		    if (value instanceof Integer) {  
		        logger.debug("Domain Value Validation. Type: " +  domainValueTypeCode +  " Id: " + dv.getId()  + " Value:" + value );
            
                if (Long.compare(new Long((Integer)value),dv.getId())==0)   {
                    logger.debug("Match found. Domain Value Validation: " + dv.getId()  + " Value:" + value);
                    return true;
                }
		    }
		    
		    if (value instanceof Long ) {  
		       logger.debug("Domain Value Validation. Type: " +  domainValueTypeCode +  " Id: " + dv.getId()  + " Value:" + value);
            
                if (Long.compare((Long)value,dv.getId())==0)   {
                    logger.debug("Match found. Domain Value Validation: " + dv.getId()  + " Value:" + value);
                    return true;
                }
		    }
		    
		    if (value instanceof String) {  
		        logger.debug("Domain Value Validation: " + dv.getCode()  + " Value:" + value);
            
                if (value.equals(dv.getCode()))   {
                    logger.debug("Domain Value Validation. Type: " +  domainValueTypeCode +  " Code: " + dv.getCode()  + " Value:" + value);
                    return true;
                }
		    }
            
        }
		} catch(Exception ex) {
		   logger.error("Unable to perform domain value validation.",ex);
		} 
                                                                                          
        return false;
	}
	
	
	
}
