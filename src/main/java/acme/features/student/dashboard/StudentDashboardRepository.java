///*
// * AdministratorDashboardRepository.java
// *
// * Copyright (C) 2012-2023 Rafael Corchuelo.
// *
// * In keeping with the traditional purpose of furthering education and research, it is
// * the policy of the copyright owner to permit non-commercial use and redistribution of
// * this software. It has been tested carefully, but it is not guaranteed for any particular
// * purposes. The copyright owner does not offer any warranties or representations, nor do
// * they accept any liabilities with respect to them.
// * 
// */
//
//package acme.features.student.dashboard;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import acme.entities.activities.ActivityType;
//import acme.framework.repositories.AbstractRepository;
//
//@Repository
//public interface StudentDashboardRepository extends AbstractRepository {
//
//	@Query("select count(l) from Activity l where l.type = :type and l.student.id = :student")
//	Integer totalNumberOfWorkbookOfType(int studentId, ActivityType type);
//
//	@Query("select avg(l.timePeriod) from Activity l where l.student.id = :studentId")
//	Double averagePeriodOfTheActivitiesPerWorkbook(int studentId);
//
//	@Query("select stddev(l.timePeriod) from Activity l where l.student.id = :studentId")
//	Double deviationOfPeriodOfTheActivitiesPerWorkbook(int studentId);
//
//	@Query("select min(l.timePeriod) from Activity l where l.student.id = :studentId")
//	Double minimumPeriodOfTheActivitiesPerWorkbook(int studentId);
//
//	@Query("select max(l.timePeriod) from Activity l where l.student.id = :studentId")
//	Double maximumPeriodOfTheActivitiesPerHerWorkbook(int studentId);
//
//	@Query("select avg(l.learningTime) from CourseLecture cl, Lecture l, Course c, Enrolment e where e.student.id = :studentId")
//	Double averageLearningTimePerCourse(int studentId);
//
//	@Query("select min(l.learningTime) from CourseLecture cl, Lecture l, Course c, Enrolment e where e.student.id = :studentId")
//	Double minimumLearningTimePerCourse(int studentId);
//
//	@Query("select max(l.learningTime) from CourseLecture cl, Lecture l, Course c, Enrolment e where e.student.id = :studentId")
//	Double maximumLearningTimePerCourse(int studentId);
//
//	@Query("select stddev(l.learningTime) from CourseLecture cl, Lecture l, Course c, Enrolment e where e.student.id = :studentId")
//	Double deviationLearningTimePerCourse(int studentId);
//
//}
