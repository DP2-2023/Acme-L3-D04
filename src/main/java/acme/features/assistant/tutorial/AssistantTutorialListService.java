
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialListService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialRepository repository;

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
		Collection<Tutorial> objects;
		int assistantId;

		assistantId = super.getRequest().getPrincipal().getActiveRoleId();
		objects = this.repository.findTutorialByAssistantId(assistantId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		final Tuple tuple = super.unbind(object, "code", "title", "goals", "isPublished");
		super.getResponse().setData(tuple);
	}

}
