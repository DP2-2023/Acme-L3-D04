
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicumSessions.PracticumSession;
import acme.entities.practicums.Practicum;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;
import spamfilter.SpamFilter;

@Service
public class CompanyPracticumSessionCreateService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository repository;

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
		Practicum practicum;

		masterId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(masterId);
		status = practicum != null && practicum.isPublished() && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession object;
		int masterId;
		Practicum practicum;

		masterId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(masterId);

		object = new PracticumSession();
		object.setTitle("");
		object.setAbstract$("");
		object.setSessionStartDate(new Date());
		object.setSessionEndDate(new Date());
		object.setLink("");
		object.setPracticum(practicum);

		super.getBuffer().setData(object);

	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;

		super.bind(object, "title", "abstract$", "sessionStartDate", "sessionEndDate", "link");

	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;
		final Date start = object.getSessionStartDate();
		final Date end = object.getSessionEndDate();
		if (!super.getBuffer().getErrors().hasErrors("sessionStartDate")) {

			final Date minimumPeriodStart = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			final boolean cumpleStart = MomentHelper.isAfter(start, minimumPeriodStart);

			super.state(cumpleStart, "sessionStartDate", "company.practicumSession.error.date1");

		}
		if (!super.getBuffer().getErrors().hasErrors("sessionEndDate")) {
			final long timeDifferenceInMillis = end.getTime() - start.getTime();
			final long hoursDifference = timeDifferenceInMillis / (60 * 60 * 1000);
			boolean fechaValida = false;
			if (Math.abs(hoursDifference) < 5 && Math.abs(hoursDifference) > 0)
				fechaValida = true;

			super.state(fechaValida, "sessionEndDate", "company.practicumSession.error.date");
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
			final String formError = "company.offer.form.error.spam";

			if (!super.getBuffer().getErrors().hasErrors("title"))
				super.state(!spamFilter.isSpam(object.getTitle()), "title", formError);

			if (!super.getBuffer().getErrors().hasErrors("abstract$"))
				super.state(!spamFilter.isSpam(object.getAbstract$()), "abstract$", formError);

			if (!super.getBuffer().getErrors().hasErrors("link"))
				super.state(!spamFilter.isSpam(object.getLink()), "link", formError);
		}
	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstract$", "sessionStartDate", "sessionEndDate", "link");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("published", object.getPracticum().isPublished());
		tuple.put("addendum", false);

		super.getResponse().setData(tuple);
	}
}
