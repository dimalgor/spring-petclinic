package org.springframework.samples.petclinic.position;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Past;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.Job;

@Entity
@Table(name = "positions")
public class Position extends Job {

	@Column(name = "start_date")
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@Column(name = "end_date")
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "position")
	private Set<Company> companies;

	protected Set<Company> getCompaniesInternal() {
		if (this.companies == null) {
			this.companies = new HashSet<>();
		}
		return this.companies;
	}

	protected void setCompaniesInternal(Set<Company> companies) {
		this.companies = companies;
	}

	public List<Company> getCompanies() {
		List<Company> sortedCompanies = new ArrayList<>(getCompaniesInternal());
		PropertyComparator.sort(sortedCompanies, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedCompanies);
	}

	public void addCompany(Company company) {
		if (company.isNew()) {
			getCompaniesInternal().add(company);
		}
		company.setPosition(this);
	}

	/**
	 * Return the Company with the given name, or null if none found for this Position.
	 * @param name to test
	 * @return true if company name is already in use
	 */
	public Company getCompany(String name) {
		return getCompany(name, false);
	}

	/**
	 * Return the Company with the given name, or null if none found for this Position.
	 * @param name to test
	 * @return true if company name is already in use
	 */
	public Company getCompany(String name, boolean ignoreNew) {
		name = name.toLowerCase();
		for (Company company : getCompaniesInternal()) {
			if (!ignoreNew || !company.isNew()) {
				String compName = company.getName();
				compName = compName.toLowerCase();
				if (compName.equals(name)) {
					return company;
				}
			}
		}
		return null;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("id", this.getId()).append("new", this.isNew()).append("position", this.getJobPosition())
			.append("area", this.getArea()).append("startDate", this.startDate).append("endDate", this.endDate).toString();
	}

}

