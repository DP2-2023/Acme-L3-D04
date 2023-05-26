
package acme.features.assistant.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.Session;
import acme.entities.sessions.SessionType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantSessionShowService extends AbstractService<Assistant, Session> {

	@Autowired
	protected AssistantSessionRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int sessionId;
		Session session;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);
		status = session != null && super.getRequest().getPrincipal().hasRole(session.getTutorial().getAssistant());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Session object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSessionById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Session object) {

		assert object != null;
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(SessionType.class, object.getSessionType());

		tuple = super.unbind(object, "title", "resume", "sessionType", "periodStart", "periodEnd", "information");
		tuple.put("sessionTypes", choices);
		super.getResponse().setData(tuple);
	}

}
