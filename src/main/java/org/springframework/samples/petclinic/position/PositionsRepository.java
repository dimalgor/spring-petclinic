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

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository class for <code>Position</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data. See:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface PositionsRepository extends Repository<Position, Integer> {

	/**
	 * Retrieve {@link Position}s from the data store by last name, returning all positions
	 * whose positon <i>starts</i> with the given string.
	 * @param position Value to search for
	 * @return a Collection of matching {@link Position}s (or an empty Collection if none
	 * found)
	 */
	@Query("SELECT DISTINCT position FROM Position position left join fetch position.pets WHERE position.jobPosition LIKE :position%")
	@Transactional(readOnly = true)
	Collection<Position> findByPosition(@Param("position") String position);

	/**
	 * Retrieve an {@link Position} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Position} if found
	 */
	@Query("SELECT position FROM Position position left join fetch position.pets WHERE position.id =:id")
	@Transactional(readOnly = true)
	Position findById(@Param("id") Integer id);

	/**
	 * Save an {@link Position} to the data store, either inserting or updating it.
	 * @param position the {@link Position} to save
	 */
	void save(Position position);

}
