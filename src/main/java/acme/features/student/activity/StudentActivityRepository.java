/*
 * StudentWorkbookRepository.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.student.activity;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.activities.Activity;
import acme.entities.enrolments.Enrolment;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentActivityRepository extends AbstractRepository {

	@Query("select w from Activity w where w.id = :id")
	Activity findOneActivityById(int id);

	@Query("select w from Enrolment w where w.id = :id")
	Enrolment findOneEnrolmentById(int id);

	@Query("select s from Student s where s.id = :id")
	Student findOneStudentById(int id);

	@Query("select a from Activity a join a.enrolment e where e.student.id = :studentId")
	Collection<Activity> findManyActivitiesByStudentId(int studentId);

	@Query("select j from Enrolment j where j.isFinished = true")
	Collection<Enrolment> findAllEnrolments();

}
