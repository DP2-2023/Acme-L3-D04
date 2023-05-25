
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enrolments.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

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

		super.bind(object, "code", "motivation", "goals", "workTime");

	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		int studentId;
		SelectChoices choices;
		final Collection<Enrolment> enrolments;
		Tuple tuple;

		studentId = super.getRequest().getPrincipal().getActiveRoleId();
		enrolments = this.repository.findManyEnrolmentsByStudentId(studentId);

		choices = SelectChoices.from(enrolments, "course", null);

		tuple = super.unbind(object, "code", "motivation", "goals", "workTime");
		tuple.put("enrolments", choices);

		super.getResponse().setData(tuple);
	}

}
