/*
 * StudentWorkbookCreateService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.Activity;
import acme.entities.activities.ActivityType;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityCreateService extends AbstractService<Student, Activity> {

	@Autowired
	protected StudentActivityRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Enrolment enrolment;
		final Student student;

		student = this.repository.findOneStudentById(super.getRequest().getPrincipal().getActiveRoleId());

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentById(masterId);

		status = enrolment != null && !enrolment.isFinished() && student != null && student.equals(enrolment.getStudent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;
		int masterId;
		Enrolment enrolment;

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentById(masterId);
		object = new Activity();
		object.setEnrolment(enrolment);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		super.bind(object, "title", "abstract$", "type", "timePeriod", "furtherInformation");

	}

	@Override
	public void validate(final Activity object) {
		assert object != null;

	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(ActivityType.class, object.getType());

		tuple = super.unbind(object, "title", "abstract$", "type", "timePeriod", "furtherInformation");
		tuple.put("type", choices);

		super.getResponse().setData(tuple);
	}

}
