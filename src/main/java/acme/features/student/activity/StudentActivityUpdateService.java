/*
 * StudentWorkbookUpdateService.java
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
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityUpdateService extends AbstractService<Student, Activity> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

	// AbstractService<Student, Workbook> -------------------------------------


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
		final Activity activity;
		final Student student;

		masterId = super.getRequest().getData("id", int.class);
		activity = this.repository.findOneActivityById(masterId);

		student = this.repository.findOneStudentById(super.getRequest().getPrincipal().getActiveRoleId());

		status = activity != null && student != null;

		// Check activity is of student
		if (status) {
			final Collection<Activity> studentActivities = this.repository.findManyActivitiesByStudentId(student.getId());
			status = studentActivities.contains(activity);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneActivityById(id);

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

		tuple = super.unbind(object, "title", "abstract$", "type", "timePeriod", "furtherInformation", "published");
		tuple.put("type", choices);

		super.getResponse().setData(tuple);
	}

}
