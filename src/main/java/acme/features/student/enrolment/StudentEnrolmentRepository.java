/*
 * StudentCourseRepository.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.entities.enrolments.Enrolment;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentEnrolmentRepository extends AbstractRepository {

	@Query("select j from Enrolment j where j.id = :id")
	Enrolment findOneEnrolmentById(int id);

	@Query("select j from Course j where j.id = :id")
	Course findOneCourseById(int id);

	@Query("select j from Enrolment j where j.student.id = :id")
	Collection<Enrolment> findAllEnrolments(int id);

	@Query("select j from Course j where j.isPublished = true")
	Collection<Course> findAllCourses();

	@Query("select s from Student s where s.id = :id")
	Student findOneStudentById(int id);

	@Query("select c.value from Config c where c.configKey = :key")
	String findOneConfigByKey(String key);

}
