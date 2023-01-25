package com.fico.ps.validation.constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated element must be a valid value from the domain value entity. 
 * 

 * Supported types are:
 * 
 * {@code Integer}
 * {@code Long}
 * {@code String}
 * 
 * {@code null} elements are considered valid depending on the required parameter.
 *
 * @author Chris Way-Jones
 */
@Target(value={METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER,TYPE_USE})
@Retention(value=RUNTIME)
@Repeatable(DomainValue.List.class)
@Documented
@Constraint(validatedBy={DomainValueValidator.class})
public @interface DomainValue {


	public String message() default "A valid domain value is required.";
	
	public String domainValueTypeCode() default "";
	
	public boolean required() default true;
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default{};
	
		
	/**
	 * Defines several {@link DomainValue} annotations on the same element.
	 *
	 * @see DomainValue
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		DomainValue[] value();
	}
}
