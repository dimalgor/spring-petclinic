package org.springframework.samples.petclinic.position;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/positions/{positionId}")
class CompanyController {

	private static final String VIEWS_COMPANIES_CREATE_OR_UPDATE_FORM = "companies/createOrUpdatePetForm";

	private final CompanyRepository companies;

	private final PositionsRepository positions;

	public CompanyController(CompanyRepository companies, PositionsRepository positions) {
		this.companies = companies;
		this.positions = positions;
	}

	@ModelAttribute("types")
	public Collection<CompanyType> populateCompanyTypes() {
		return this.companies.findCompanyTypes();
	}

	@ModelAttribute("position")
	public Position findPosition(@PathVariable("positionId") int positionId) {
		return this.positions.findById(positionId);
	}

	@InitBinder("position")
	public void initPositionBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("company")
	public void initCompanyBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new CompanyValidator());
	}

	@GetMapping("/companies/new")
	public String initCreationForm(Position position, ModelMap model) {
		Company company = new Company();
		position.addCompany(company);
		model.put("company", company);
		return VIEWS_COMPANIES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/companies/new")
	public String processCreationForm(Position position, @Valid Company company, BindingResult result, ModelMap model) {
		if (StringUtils.hasLength(company.getName()) && company.isNew() && position.getCompany(company.getName(), true) != null) {
			result.rejectValue("name", "duplicate", "already exists");
		}
		position.addCompany(company);
		if (result.hasErrors()) {
			model.put("company", company);
			return VIEWS_COMPANIES_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.companies.save(company);
			return "redirect:/positions/{positionId}";
		}
	}

	@GetMapping("/companies/{companyId}/edit")
	public String initUpdateForm(@PathVariable("companyId") int companyId, ModelMap model) {
		Company company = this.companies.findById(companyId);
		model.put("company", company);
		return VIEWS_COMPANIES_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/companies/{companyId}/edit")
	public String processUpdateForm(@Valid Company company, BindingResult result, Position position, ModelMap model) {
		if (result.hasErrors()) {
			company.setPosition(position);
			model.put("company", company);
			return VIEWS_COMPANIES_CREATE_OR_UPDATE_FORM;
		}
		else {
			position.addCompany(company);
			this.companies.save(company);
			return "redirect:/positions/{positionId}";
		}
	}

}
