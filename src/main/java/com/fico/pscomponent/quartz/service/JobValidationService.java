package com.fico.pscomponent.quartz.service;

import java.text.ParseException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.apache.commons.lang.BooleanUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fico.pscomponent.quartz.model.JobDTO;

@Service
public class JobValidationService {

	@Autowired
	private Validator validator;

	public void validateJobBody(@Valid JobDTO jobDto) throws Exception {
		Set<ConstraintViolation<JobDTO>> violations = validator.validate(jobDto);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<JobDTO> constraintViolation : violations) {
				sb.append(constraintViolation.getMessage()).append("\n");
			}
			throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
		}

		if (BooleanUtils.isTrue(jobDto.getIsCron()) && BooleanUtils.isTrue(jobDto.getIsFrequency())) {
			throw new Exception("Invalid Request Body [Either cron or frequency should be enabled]");
		}

		if (BooleanUtils.isTrue(jobDto.getIsCron()) && StringUtils.isEmpty(jobDto.getCronExpression())) {
			throw new Exception("Invalid Request Body [Check Cron Details]");
		}

		if (BooleanUtils.isTrue(jobDto.getIsFrequency()) && jobDto.getFrequency() == 0 ) {
			throw new Exception("Invalid Request Body [Check frequency details]") ;
		}
		
		if (BooleanUtils.isTrue(jobDto.getIsCron())) {
			validateCronExpression(jobDto.getCronExpression());
		}
	}

	public void validateCronExpression(String cronExpression) throws Exception {
		try {
			CronExpression.validateExpression(cronExpression);
		} catch (ParseException e) {
			throw new Exception("Invalid Cron Expression");
		}
	}
}
