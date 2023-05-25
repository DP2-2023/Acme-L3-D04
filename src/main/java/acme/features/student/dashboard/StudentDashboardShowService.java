/*
 * LecturerDashboardShowService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 * 
 */

package acme.features.student.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.ActivityType;
import acme.forms.StudentDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentDashboardShowService extends AbstractService<Student, StudentDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentDashboardRepository repository;
	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Principal principal;
		principal = super.getRequest().getPrincipal();
		final int studentId = principal.getActiveRoleId();

		StudentDashboard dashboard;
		final Integer totalNumberOfTheoryWorkbook;
		final Integer totalNumberOfHandsOnWorkbook;
		final Double averagePeriodOfTheActivitiesPerWorkbook;
		final Double minimumPeriodOfTheActivitiesPerWorkbook;
		final Double maximumPeriodOfTheActivitiesPerHerWorkbook;
		final Double deviationOfPeriodOfTheActivitiesPerWorkbook;
		Double averageLearningTimePerCourse;
		final Double deviationLearningTimePerCourse;
		final Double minimumLearningTimePerCourse;
		Double maximumLearningTimePerCourse;

		totalNumberOfTheoryWorkbook = this.repository.totalNumberOfWorkbookOfType(studentId, ActivityType.THEORETICAL);
		totalNumberOfHandsOnWorkbook = this.repository.totalNumberOfWorkbookOfType(studentId, ActivityType.HANDS_ON);
		averagePeriodOfTheActivitiesPerWorkbook = this.repository.averagePeriodOfTheActivitiesPerWorkbook(studentId);
		deviationOfPeriodOfTheActivitiesPerWorkbook = this.repository.deviationOfPeriodOfTheActivitiesPerWorkbook(studentId);
		minimumPeriodOfTheActivitiesPerWorkbook = this.repository.minimumPeriodOfTheActivitiesPerWorkbook(studentId);
		maximumPeriodOfTheActivitiesPerHerWorkbook = this.repository.maximumPeriodOfTheActivitiesPerHerWorkbook(studentId);
		averageLearningTimePerCourse = this.repository.averageLearningTimePerCourse(studentId);
		deviationLearningTimePerCourse = this.repository.deviationLearningTimePerCourse(studentId);
		minimumLearningTimePerCourse = this.repository.minimumLearningTimePerCourse(studentId);
		maximumLearningTimePerCourse = this.repository.maximumLearningTimePerCourse(studentId);

		System.out.println(totalNumberOfHandsOnWorkbook);
		System.out.println(totalNumberOfTheoryWorkbook);

		dashboard = new StudentDashboard();
		dashboard.setTotalNumberOfTheoryWorkbook(totalNumberOfTheoryWorkbook);
		dashboard.setTotalNumberOfHandsOnWorkbook(totalNumberOfHandsOnWorkbook);
		dashboard.setAveragePeriodOfTheActivitiesPerWorkbook(averagePeriodOfTheActivitiesPerWorkbook);
		dashboard.setDeviationOfPeriodOfTheActivitiesPerWorkbook(deviationOfPeriodOfTheActivitiesPerWorkbook);
		dashboard.setMinimumPeriodOfTheActivitiesPerWorkbook(minimumPeriodOfTheActivitiesPerWorkbook);
		dashboard.setMaximumPeriodOfTheActivitiesPerHerWorkbook(maximumPeriodOfTheActivitiesPerHerWorkbook);
		dashboard.setAverageLearningTimePerCourse(averageLearningTimePerCourse);
		dashboard.setDeviationLearningTimePerCourse(deviationLearningTimePerCourse);
		dashboard.setMinimumLearningTimePerCourse(minimumLearningTimePerCourse);
		dashboard.setMaximumLearningTimePerCourse(maximumLearningTimePerCourse);

		super.getBuffer().setData(dashboard);
	}

	@Override
	public void unbind(final StudentDashboard object) {
		final Tuple tuple;

		tuple = super.unbind(object, //
			"totalNumberOfTheoryWorkbook", "totalNumberOfHandsOnWorkbook",//
			"averagePeriodOfTheActivitiesPerWorkbook", "deviationOfPeriodOfTheActivitiesPerWorkbook",//
			"minimumPeriodOfTheActivitiesPerWorkbook", "maximumPeriodOfTheActivitiesPerHerWorkbook",//
			"averageLearningTimePerCourse", "deviationLearningTimePerCourse",//
			"minimumLearningTimePerCourse", "maximumLearningTimePerCourse");

		super.getResponse().setData(tuple);
	}

}
