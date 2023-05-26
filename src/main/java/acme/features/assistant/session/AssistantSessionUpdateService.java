
package acme.features.assistant.session;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.Session;
import acme.entities.sessions.SessionType;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;
import spamfilter.SpamFilter;

@Service
public class AssistantSessionUpdateService extends AbstractService<Assistant, Session> {

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
		Tutorial tutorial;

		sessionId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialBySessionId(sessionId);
		status = tutorial != null && tutorial.isPublished() && super.getRequest().getPrincipal().hasRole(tutorial.getAssistant());

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
	public void bind(final Session object) {
		assert object != null;

		super.bind(object, "title", "resume", "sessionType", "periodStart", "periodEnd", "information");
	}

	@Override
	public void validate(final Session object) {
		assert object != null;
		final Date start = object.getPeriodStart();
		final Date end = object.getPeriodEnd();
		if (!super.getBuffer().getErrors().hasErrors("periodStart")) {

			final Date minimumPeriodStart = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			final boolean cumpleStart = MomentHelper.isAfter(start, minimumPeriodStart);

			super.state(cumpleStart, "periodStart", "assistant.session.error.date1");

		}
		if (!super.getBuffer().getErrors().hasErrors("periodEnd")) {
			final long timeDifferenceInMillis = end.getTime() - start.getTime();
			final long hoursDifference = timeDifferenceInMillis / (60 * 60 * 1000);  // Convert milliseconds to hours
			boolean fechaValida = false;
			if (Math.abs(hoursDifference) < 5 && Math.abs(hoursDifference) > 0)
				fechaValida = true;

			super.state(fechaValida, "periodEnd", "assistant.session.error.date");
		}

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

			if (!super.getBuffer().getErrors().hasErrors("resume"))
				super.state(!spamFilter.isSpam(object.getResume()), "resume", formError);

			if (!super.getBuffer().getErrors().hasErrors("information"))
				super.state(!spamFilter.isSpam(object.getInformation()), "information", formError);

		}
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
