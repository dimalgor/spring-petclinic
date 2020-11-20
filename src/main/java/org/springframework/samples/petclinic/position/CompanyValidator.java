package org.springframework.samples.petclinic.position;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CompanyValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		Company company = (Company) obj;
		String name = company.getName();
		// name validation
		if (!StringUtils.hasLength(name)) {
			errors.rejectValue("name", REQUIRED, REQUIRED);
		}

		// type validation
		if (company.isNew() && company.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}

		// birth date validation
		if (company.getBirthDate() == null) {
			errors.rejectValue("birthDate", REQUIRED, REQUIRED);
		}
	}

	/**
	 * This Validator validates *just* Company instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Company.class.isAssignableFrom(clazz);
	}

}
