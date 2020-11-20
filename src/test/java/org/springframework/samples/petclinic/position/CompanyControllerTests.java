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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link CompanyController}
 *
 * @author Colin But
 */
@WebMvcTest(value = CompanyController.class,
		includeFilters = @ComponentScan.Filter(value = CompanyTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE))
class CompanyControllerTests {

	private static final int TEST_POSITION_ID = 1;

	private static final int TEST_COMPANY_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CompanyRepository companies;

	@MockBean
	private PositionsRepository positions;

	@BeforeEach
	void setup() {
		CompanyType cat = new CompanyType();
		cat.setId(3);
		cat.setName("hamster");
		given(this.companies.findCompanyTypes()).willReturn(Lists.newArrayList(cat));
		given(this.positions.findById(TEST_POSITION_ID)).willReturn(new Position());
		given(this.companies.findById(TEST_COMPANY_ID)).willReturn(new Company());

	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/positions/{positionId}/companies/new", TEST_POSITION_ID)).andExpect(status().isOk())
				.andExpect(view().name("companies/createOrUpdatePetForm")).andExpect(model().attributeExists("company"));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/positions/{positionId}/companies/new", TEST_POSITION_ID).param("name", "Betty")
				.param("type", "hamster").param("birthDate", "2015-02-12")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/positions/{positionId}"));
	}

	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/positions/{positionId}/companies/new", TEST_POSITION_ID).param("name", "Betty").param("birthDate",
				"2015-02-12")).andExpect(model().attributeHasNoErrors("position"))
				.andExpect(model().attributeHasErrors("company")).andExpect(model().attributeHasFieldErrors("company", "type"))
				.andExpect(model().attributeHasFieldErrorCode("company", "type", "required")).andExpect(status().isOk())
				.andExpect(view().name("companies/createOrUpdatePetForm"));
	}

	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/positions/{positionId}/companies/{petId}/edit", TEST_POSITION_ID, TEST_COMPANY_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists("company"))
				.andExpect(view().name("companies/createOrUpdatePetForm"));
	}

	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/positions/{positionId}/companies/{companyId}/edit", TEST_POSITION_ID, TEST_COMPANY_ID)
			.param("name", "Betty")
			.param("type", "hamster")
			.param("birthDate", "2015-02-12"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/positions/{positionId}"));
	}

	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/positions/{positionId}/companies/{companyId}/edit", TEST_POSITION_ID, TEST_COMPANY_ID).param("name", "Betty")
				.param("birthDate", "2015/02/12")).andExpect(model().attributeHasNoErrors("position"))
				.andExpect(model().attributeHasErrors("company")).andExpect(status().isOk())
				.andExpect(view().name("companies/createOrUpdatePetForm"));
	}

}
