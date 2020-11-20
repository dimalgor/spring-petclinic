package org.springframework.samples.petclinic.position;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface CompanyRepository extends Repository<Company, Integer> {

	/**
	 * Retrieve all {@link CompanyType}s from the data store.
	 * @return a Collection of {@link CompanyType}s.
	 */
	@Query("SELECT ptype FROM CompanyType ptype ORDER BY ptype.name")
	@Transactional(readOnly = true)
	List<CompanyType> findCompanyTypes();

	/**
	 * Retrieve a {@link Company} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Company} if found
	 */
	@Transactional(readOnly = true)
	Company findById(Integer id);

	/**
	 * Save a {@link Company} to the data store, either inserting or updating it.
	 * @param company the {@link Company} to save
	 */
	void save(Company company);

}
