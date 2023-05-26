
package acme.features.assistant.session;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.sessions.Session;
import acme.framework.controllers.AbstractController;
import acme.roles.Assistant;

@Controller
public class AssistantSessionController extends AbstractController<Assistant, Session> {

	@Autowired
	protected AssistantSessionListService			listService;

	@Autowired
	protected AssistantSessionListTutorialService	listTutorialService;

	@Autowired
	protected AssistantSessionShowService			showService;

	@Autowired
	protected AssistantSessionCreateService			createService;

	@Autowired
	protected AssistantSessionUpdateService			updateService;

	@Autowired
	protected AssistantSessionDeleteService			deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-tutorial", "list", this.listTutorialService);
	}
}
