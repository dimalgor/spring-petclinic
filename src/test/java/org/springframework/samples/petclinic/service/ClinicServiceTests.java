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

package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.position.*;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code> </code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ClinicServiceTests {

	@Autowired
	protected PositionsRepository positions;

	@Autowired
	protected PetRepository pets;

	@Autowired
	protected VisitRepository visits;

	@Autowired
	protected VetRepository vets;

	@Test
	void shouldFindPositionsByLastName() {
		Collection<Position> positions = this.positions.findByPosition("Java / R&D Engineer");
		assertThat(positions).hasSize(1);

		positions = this.positions.findByPosition("Clown");
		assertThat(positions).isEmpty();
	}

	@Test
	void shouldFindSinglePositionWithPet() {
		Position position = this.positions.findById(1);
		assertThat(position.getJobPosition()).endsWith("Engineer");
		assertThat(position.getPets()).hasSize(1);
		assertThat(position.getPets().get(0).getType()).isNotNull();
		assertThat(position.getPets().get(0).getType().getName()).isEqualTo("cat");
	}

	@Test
	@Transactional
	void shouldInsertPosition() {
		Collection<Position> positions = this.positions.findByPosition("Schultz");
		int found = positions.size();

		Position position = new Position();
		position.setArea("Sam");
		position.setJobPosition("Schultz");
		position.setAddress("4, Evans Street");
		position.setCity("Wollongong");
		position.setTelephone("4444444444");
		this.positions.save(position);
		assertThat(position.getId().longValue()).isNotEqualTo(0);

		positions = this.positions.findByPosition("Schultz");
		assertThat(positions.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	void shouldUpdatePosition() {
		Position position = this.positions.findById(1);
		String oldLastName = position.getJobPosition();
		String newLastName = oldLastName + "X";

		position.setJobPosition(newLastName);
		this.positions.save(position);

		// retrieving new name from database
		position = this.positions.findById(1);
		assertThat(position.getJobPosition()).isEqualTo(newLastName);
	}

	@Test
	void shouldFindPetWithCorrectId() {
		Pet pet7 = this.pets.findById(7);

		System.out.println(pet7.toString());
		assertThat(pet7.getName()).startsWith("Samantha");
		assertThat(pet7.getPosition().getArea()).isEqualTo("Information Technology");

	}

	@Test
	void shouldFindAllPetTypes() {
		Collection<PetType> petTypes = this.pets.findPetTypes();

		PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
		assertThat(petType1.getName()).isEqualTo("cat");
		PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
		assertThat(petType4.getName()).isEqualTo("snake");
	}

	@Test
	@Transactional
	void shouldInsertPetIntoDatabaseAndGenerateId() {
		Position position6 = this.positions.findById(6);
		int found = position6.getPets().size();

		Pet pet = new Pet();
		pet.setName("bowser");
		Collection<PetType> types = this.pets.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		position6.addPet(pet);
		assertThat(position6.getPets().size()).isEqualTo(found + 1);

		this.pets.save(pet);
		this.positions.save(position6);

		position6 = this.positions.findById(6);
		assertThat(position6.getPets().size()).isEqualTo(found + 1);
		// checks that id has been generated
		assertThat(pet.getId()).isNotNull();
	}

	@Test
	@Transactional
	void shouldUpdatePetName() throws Exception {
		Pet pet7 = this.pets.findById(7);
		String oldName = pet7.getName();

		String newName = oldName + "X";
		pet7.setName(newName);
		this.pets.save(pet7);

		pet7 = this.pets.findById(7);
		assertThat(pet7.getName()).isEqualTo(newName);
	}

	@Test
	void shouldFindVets() {
		Collection<Vet> vets = this.vets.findAll();

		Vet vet = EntityUtils.getById(vets, Vet.class, 3);
		assertThat(vet.getJobPosition()).isEqualTo("Douglas");
		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
		assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
	}

	@Test
	@Transactional
	void shouldAddNewVisitForPet() {
		Pet pet7 = this.pets.findById(7);
		int found = pet7.getVisits().size();
		Visit visit = new Visit();
		pet7.addVisit(visit);
		visit.setDescription("test");
		this.visits.save(visit);
		this.pets.save(pet7);

		pet7 = this.pets.findById(7);
		assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
		assertThat(visit.getId()).isNotNull();
	}

	@Test
	void shouldFindVisitsByPetId() throws Exception {
		Collection<Visit> visits = this.visits.findByPetId(7);
		assertThat(visits).hasSize(2);
		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
		assertThat(visitArr[0].getDate()).isNotNull();
		assertThat(visitArr[0].getPetId()).isEqualTo(7);
	}

}
