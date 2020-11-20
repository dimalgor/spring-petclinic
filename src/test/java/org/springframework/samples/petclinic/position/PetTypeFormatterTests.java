/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.position;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test class for {@link CompanyTypeFormatter}
 *
 * @author Colin But
 */
@ExtendWith(MockitoExtension.class)
class CompanyTypeFormatterTests {

	@Mock
	private CompanyRepository companies;

	private CompanyTypeFormatter companyTypeFormatter;

	@BeforeEach
	void setup() {
		this.companyTypeFormatter = new CompanyTypeFormatter(companies);
	}

	@Test
	void testPrint() {
		CompanyType companyType = new CompanyType();
		companyType.setName("Hamster");
		String companyTypeName = this.companyTypeFormatter.print(companyType, Locale.ENGLISH);
		assertThat(companyTypeName).isEqualTo("Hamster");
	}

	@Test
	void shouldParse() throws ParseException {
		given(this.companies.findCompanyTypes()).willReturn(makeCompanyTypes());
		CompanyType companyType = companyTypeFormatter.parse("Bird", Locale.ENGLISH);
		assertThat(companyType.getName()).isEqualTo("Bird");
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		given(this.companies.findCompanyTypes()).willReturn(makeCompanyTypes());
		Assertions.assertThrows(ParseException.class, () -> {
			companyTypeFormatter.parse("Fish", Locale.ENGLISH);
		});
	}

	/**
	 * Helper method to produce some sample company types just for test purpose
	 * @return {@link Collection} of {@link CompanyType}
	 */
	private List<CompanyType> makeCompanyTypes() {
		List<CompanyType> companyTypes = new ArrayList<>();
		companyTypes.add(new CompanyType() {
			{
				setName("Dog");
			}
		});
		companyTypes.add(new CompanyType() {
			{
				setName("Bird");
			}
		});
		return companyTypes;
	}

}
