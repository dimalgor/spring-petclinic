package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

@MappedSuperclass
public class Job extends BaseEntity {

	@Column(name = "area")
	@NotEmpty
	private String area;

	@Column(name = "position")
	@NotEmpty
	private String jobPosition;

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getJobPosition() {
		return this.jobPosition;
	}

	public void setJobPosition(String lastName) {
		this.jobPosition = lastName;
	}

}
