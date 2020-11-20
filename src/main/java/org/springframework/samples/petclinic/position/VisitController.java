package org.springframework.samples.petclinic.position;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class VisitController {

	private final VisitRepository visits;

	private final CompanyRepository companies;

	public VisitController(VisitRepository visits, CompanyRepository companies) {
		this.visits = visits;
		this.companies = companies;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
	 * we always have fresh data - Since we do not use the session scope, make sure that
	 * Company object always has an id (Even though id is not part of the form fields)
	 * @param companyId
	 * @return Company
	 */
	@ModelAttribute("visit")
	public Visit loadCompanyWithVisit(@PathVariable("companyId") int companyId, Map<String, Object> model) {
		Company company = this.companies.findById(companyId);
		company.setVisitsInternal(this.visits.findByCompanyId(companyId));
		model.put("pet", company);
		Visit visit = new Visit();
		company.addVisit(visit);
		return visit;
	}

	// Spring MVC calls method loadCompanyWithVisit(...) before initNewVisitForm is called
	@GetMapping("/positions/*/companies/{companyId}/visits/new")
	public String initNewVisitForm(@PathVariable("companyId") int companyId, Map<String, Object> model) {
		return "companies/createOrUpdateVisitForm";
	}

	// Spring MVC calls method loadCompanyWithVisit(...) before processNewVisitForm is called
	@PostMapping("/positions/{positionId}/companies/{companyId}/visits/new")
	public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
		if (result.hasErrors()) {
			return "companies/createOrUpdateVisitForm";
		}
		else {
			this.visits.save(visit);
			return "redirect:/positions/{positionId}";
		}
	}

}
