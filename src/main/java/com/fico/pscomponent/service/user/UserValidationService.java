package com.fico.pscomponent.service.user;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fico.pscomponent.model.UserDTO;


@Service
public class UserValidationService {
    
    	private static final Logger logger = LoggerFactory.getLogger(UserValidationService.class);

	@Autowired
	private Validator validator;

	public void validateUserBody(@Valid UserDTO userDTO) {
	    logger.info("Before executing violations :::::::::::::::::::::");
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
		logger.info("After executing violations value :::::::::::::::::::::{}", violations.isEmpty());
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<UserDTO> constraintViolation : violations) {
				sb.append(constraintViolation.getMessage()).append("\n");
			}
			throw new ConstraintViolationException(sb.toString(), violations);
		}
	}
}
