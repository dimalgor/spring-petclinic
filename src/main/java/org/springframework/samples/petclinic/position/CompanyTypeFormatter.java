package org.springframework.samples.petclinic.position;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class CompanyTypeFormatter implements Formatter<CompanyType> {

	private final CompanyRepository companies;

	@Autowired
	public CompanyTypeFormatter(CompanyRepository companies) {
		this.companies = companies;
	}

	@Override
	public String print(CompanyType companyType, Locale locale) {
		return companyType.getName();
	}

	@Override
	public CompanyType parse(String text, Locale locale) throws ParseException {
		Collection<CompanyType> findCompanyTypes = this.companies.findCompanyTypes();
		for (CompanyType type : findCompanyTypes) {
			if (type.getName().equals(text)) {
				return type;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}

}
