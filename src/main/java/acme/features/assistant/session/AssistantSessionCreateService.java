
package acme.features.assistant.session;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.Session;
import acme.entities.sessions.SessionType;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantSessionCreateService extends AbstractService<Assistant, Session> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Tutorial tutorial;

		masterId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(masterId);
		status = tutorial != null && tutorial.isPublished() && super.getRequest().getPrincipal().hasRole(tutorial.getAssistant());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Session object;
		int masterId;
		Tutorial tutorial;

		masterId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(masterId);

		object = new Session();
		object.setTitle("");
		object.setResume("");
		object.setSessionType(null);
		object.setPeriodStart(new Date());
		object.setPeriodEnd(new Date());
		object.setInformation("");
		object.setTutorial(tutorial);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Session object) {
		assert object != null;

		super.bind(object, "title", "resume", "sessionType", "periodStart", "periodEnd", "information");
	}

	@Override
	public void validate(final Session object) {
		assert object != null;
	}

	@Override
	public void perform(final Session object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;

		SelectChoices choices;

		Tuple tuple;

		choices = SelectChoices.from(SessionType.class, object.getSessionType());

		tuple = super.unbind(object, "title", "resume", "sessionType", "periodStart", "periodEnd", "information");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("published", object.getTutorial().isPublished());
		tuple.put("sessionTypes", choices);

		super.getResponse().setData(tuple);
	}

}
