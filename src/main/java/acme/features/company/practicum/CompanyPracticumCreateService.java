/*
 * CompanyPracticumCreateService.java
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
import acme.entities.practicums.Practicum;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;
import spamfilter.SpamFilter;

@Service
public class CompanyPracticumCreateService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

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
		Practicum object;
		Company company;

		company = this.repository.findOneCompanyById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Practicum();
		object.setCompany(company);
		object.setPublished(false);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;
		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "code", "title", "abstract$", "goals", "estimatedTotalTime");
		object.setCourse(course);
		object.setPublished(false);
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		// Spam filter
		String spamTerms = null;
		final String spamTermsES = this.repository.findOneConfigByKey("spamTermsES");
		final String spamTermsEN = this.repository.findOneConfigByKey("spamTermsEN");
		final Float threshold = Float.valueOf(this.repository.findOneConfigByKey("spamThreshold"));

		if (spamTermsES != null && !spamTermsES.trim().isEmpty()) {
			spamTerms = spamTermsES;
			if (spamTermsEN != null && !spamTermsEN.trim().isEmpty())
				spamTerms = spamTerms + "," + spamTermsEN;
		} else if (spamTermsEN != null && !spamTermsEN.trim().isEmpty())
			spamTerms = spamTermsEN;

		if (spamTerms != null && threshold != null) {
			final SpamFilter spamFilter = new SpamFilter(spamTerms, threshold);
			final String formError = "assistant.offer.form.error.spam";

			if (!super.getBuffer().getErrors().hasErrors("title"))
				super.state(!spamFilter.isSpam(object.getTitle()), "title", formError);

			if (!super.getBuffer().getErrors().hasErrors("abstract$"))
				super.state(!spamFilter.isSpam(object.getAbstract$()), "abstract$", formError);

			if (!super.getBuffer().getErrors().hasErrors("goals"))
				super.state(!spamFilter.isSpam(object.getGoals()), "goals", formError);

		}

	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		SelectChoices choices;
		Tuple tuple;
		Collection<Course> courses;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "title", "abstract$", "goals", "estimatedTotalTime", "isPublished");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}
}
