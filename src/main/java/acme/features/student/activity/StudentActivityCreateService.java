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

import java.util.Collection;

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
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Activity object;
		object = new Activity();

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		final int enrolmentId = super.getRequest().getData("enrolment", int.class);
		final Enrolment enrolment = this.repository.findOneEnrolmentById(enrolmentId);

		object.setEnrolment(enrolment);
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
		final Collection<Enrolment> enrolments;
		enrolments = this.repository.findAllEnrolments();

		SelectChoices choices;
		SelectChoices choices2;
		Tuple tuple;

		choices = SelectChoices.from(ActivityType.class, object.getType());
		choices2 = SelectChoices.from(enrolments, "code", null);

		tuple = super.unbind(object, "title", "abstract$", "timePeriod", "furtherInformation");
		tuple.put("type", choices);
		tuple.put("enrolments", choices2);

		super.getResponse().setData(tuple);
	}

}
