/*
 * AuthenticatedPracticumSessionShowService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.practicumSessions.PracticumSession;
import acme.entities.practicums.Practicum;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumShowService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

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
		int id;
		Practicum practicum;

		id = super.getRequest().getData("id", int.class);
		practicum = this.repository.findOnePracticumById(id);
		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOnePracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();

		choices = SelectChoices.from(courses, "title", object.getCourse());

		final Collection<PracticumSession> practicumSessions = this.repository.findManyPracticumsSessionsByPracticumId(object.getId());
		tuple = super.unbind(object, "code", "title", "abstract$", "goals", "estimatedTotalTime", "isPublished");
		tuple.put("company", object.getCompany().getName());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("numPracticumSessions", practicumSessions.size());

		super.getResponse().setData(tuple);
	}
}
