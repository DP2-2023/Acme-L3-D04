
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import spamfilter.SpamFilter;

@Service
public class StudentEnrolmentRegisterService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected StudentEnrolmentRepository repository;

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
		Enrolment object;
		Student student;

		student = this.repository.findOneStudentById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Enrolment();
		object.setStudent(student);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		final int courseId = super.getRequest().getData("course", int.class);
		final Course course = this.repository.findOneCourseById(courseId);

		object.setCourse(course);
		super.bind(object, "code", "motivation", "goals", "workTime");

	}

	@Override
	public void validate(final Enrolment object) {
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
			final String formError = "student.enrolment.form.error.spam";

			if (!super.getBuffer().getErrors().hasErrors("motivation"))
				super.state(!spamFilter.isSpam(object.getMotivation()), "motivation", formError);

			if (!super.getBuffer().getErrors().hasErrors("goals"))
				super.state(!spamFilter.isSpam(object.getGoals()), "goals", formError);

		}

	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		SelectChoices choices;
		final Collection<Course> courses;
		Tuple tuple;

		courses = this.repository.findAllCourses();

		choices = SelectChoices.from(courses, "code", null);

		tuple = super.unbind(object, "code", "motivation", "goals", "workTime");
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
