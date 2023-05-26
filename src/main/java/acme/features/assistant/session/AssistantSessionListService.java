
package acme.features.assistant.session;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.Session;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantSessionListService extends AbstractService<Assistant, Session> {

	@Autowired
	protected AssistantSessionRepository repository;

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
		Collection<Session> objects;
		int assistantId;

		assistantId = super.getRequest().getPrincipal().getActiveRoleId();
		objects = this.repository.findSessionsByAssistantId(assistantId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;

		final Tuple tuple = super.unbind(object, "title", "sessionType");
		super.getResponse().setData(tuple);
	}

}
