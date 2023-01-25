/**
 * 
 */
package com.fico.ps.hook;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fico.ps.model.ApplicationVO;
import com.fico.ps.hermes.save.Error;
import com.fico.ps.hermes.hook.ICustomValidator;

/**
 * @author MushfikKhan
 *
 */
@Component("Hermes.CustomValidator")
public class CustomValidatorImpl implements ICustomValidator {

	@Override
	public <T> Error validate(T objectToValidate) {
		/*
		 * ApplicationVO application = (ApplicationVO) objectToValidate; Error error =
		 * new Error(null, null); if (application != null && application.getApplicants()
		 * != null) { for (ApplicantVO applicant : application.getApplicants()) {
		 * if(applicant.getDateOfBirth() != null) { Date dob =
		 * applicant.getDateOfBirth(); LocalDate today = LocalDate.now(); // Today's
		 * date // LocalDate birthday =
		 * dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // Birth date
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(dob); int year =
		 * calendar.get(Calendar.YEAR); int month = calendar.get(Calendar.MONTH); int
		 * day = calendar.get(Calendar.DAY_OF_MONTH); LocalDate birthday =
		 * LocalDate.of(year, month, day);
		 * 
		 * Period p = Period.between(birthday, today);
		 * 
		 * if (p.getYears() < 18) { error.addError(new Error("C-0001",
		 * "Applicant's age cannot be less than 18 years.")); }
		 * 
		 * } } }
		 */
		return new Error(null, null);
	}
}
