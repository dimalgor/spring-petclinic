package org.springframework.samples.petclinic.position;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PositionsRepository extends Repository<Position, Integer> {

	/**
	 * Retrieve {@link Position}s from the data store by last name, returning all positions
	 * whose jobPositon <i>starts</i> with the given string.
	 * @param position Value to search for
	 * @return a Collection of matching {@link Position}s (or an empty Collection if none
	 * found)
	 */
	@Query("SELECT DISTINCT position FROM Position position left join fetch position.companies WHERE position.jobPosition LIKE :position%")
	@Transactional(readOnly = true)
	Collection<Position> findByPosition(@Param("position") String position);

	/**
	 * Retrieve an {@link Position} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Position} if found
	 */
	@Query("SELECT position FROM Position position left join fetch position.companies WHERE position.id =:id")
	@Transactional(readOnly = true)
	Position findById(@Param("id") Integer id);

	/**
	 * Save an {@link Position} to the data store, either inserting or updating it.
	 * @param position the {@link Position} to save
	 */
	void save(Position position);

}
