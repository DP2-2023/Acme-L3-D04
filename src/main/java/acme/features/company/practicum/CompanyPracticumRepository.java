/*
 * AuthenticatedCompanyRepository.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.entities.practicumSessions.PracticumSession;
import acme.entities.practicums.Practicum;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface CompanyPracticumRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.id = :id")
	Practicum findOnePracticumById(int id);

	@Query("select p from Practicum p where p.company.id = :id")
	Collection<Practicum> findPracticumByCompanyId(int id);

	@Query("select c from Company c where c.id = :id")
	Company findOneCompanyById(int id);

	@Query("select p from Practicum p where p.company.id = :companyId")
	Collection<Practicum> findManyPracticumsByCompanyId(int companyId);

	@Query("select cs from Course cs where cs.id = :id")
	Course findOneCourseById(int id);

	@Query("select p from Practicum p where p.isPublished = true")
	Collection<Practicum> findAllPracticum();

	@Query("select cs from Course cs where cs.isPublished = true")
	Collection<Course> findAllCourses();

	@Query("select c from Company c")
	Collection<Company> findAllCompanies();

	@Query("select ps from PracticumSession ps where ps.practicum.id = :practicumId")
	Collection<PracticumSession> findManyPracticumsSessionsByPracticumId(int practicumId);

	@Query("select c.value from Config c where c.configKey = :key")
	String findOneConfigByKey(String key);

}
