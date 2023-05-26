/*
 * LecturerCourseTestRepository.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.any.peep;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.peeps.Peep;
import acme.framework.repositories.AbstractRepository;

public interface AnyPeepTestRepository extends AbstractRepository {

	@Query("select p from Peep p where p.nick = :username")
	Collection<Peep> findManyPeepsByUsername(String username);
}
