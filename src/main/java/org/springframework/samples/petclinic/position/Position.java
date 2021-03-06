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
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
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
	private Set<Pet> pets;

	protected Set<Pet> getPetsInternal() {
		if (this.pets == null) {
			this.pets = new HashSet<>();
		}
		return this.pets;
	}

	protected void setPetsInternal(Set<Pet> pets) {
		this.pets = pets;
	}

	public List<Pet> getPets() {
		List<Pet> sortedPets = new ArrayList<>(getPetsInternal());
		PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedPets);
	}

	public void addPet(Pet pet) {
		if (pet.isNew()) {
			getPetsInternal().add(pet);
		}
		pet.setPosition(this);
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Position.
	 * @param name to test
	 * @return true if pet name is already in use
	 */
	public Pet getPet(String name) {
		return getPet(name, false);
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Position.
	 * @param name to test
	 * @return true if pet name is already in use
	 */
	public Pet getPet(String name, boolean ignoreNew) {
		name = name.toLowerCase();
		for (Pet pet : getPetsInternal()) {
			if (!ignoreNew || !pet.isNew()) {
				String compName = pet.getName();
				compName = compName.toLowerCase();
				if (compName.equals(name)) {
					return pet;
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

