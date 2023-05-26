
package acme.features.any.peep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.peeps.Peep;
import acme.framework.components.accounts.Any;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import spamfilter.SpamFilter;

@Service
public class AnyPeepPublishService extends AbstractService<Any, Peep> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyPeepRepository repository;

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
		final Peep object = new Peep();

		if (super.getRequest().getPrincipal().isAuthenticated())
			object.setNick(super.getRequest().getPrincipal().getUsername());

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Peep object) {
		assert object != null;

		// Bind the text field
		super.bind(object, "title", "nick", "message", "email", "link");
		object.setMoment(MomentHelper.getCurrentMoment());

	}

	@Override
	public void validate(final Peep object) {
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

			if (!super.getBuffer().getErrors().hasErrors("title"))
				super.state(!spamFilter.isSpam(object.getTitle()), "title", "lecturer.lecture.form.error.spam");

			if (!super.getBuffer().getErrors().hasErrors("nick"))
				super.state(!spamFilter.isSpam(object.getNick()), "nick", "lecturer.lecture.form.error.spam");

			if (!super.getBuffer().getErrors().hasErrors("message"))
				super.state(!spamFilter.isSpam(object.getMessage()), "message", "lecturer.lecture.form.error.spam");

			if (!super.getBuffer().getErrors().hasErrors("email"))
				super.state(!spamFilter.isSpam(object.getEmail()), "email", "lecturer.lecture.form.error.spam");

			if (!super.getBuffer().getErrors().hasErrors("link"))
				super.state(!spamFilter.isSpam(object.getLink()), "link", "lecturer.lecture.form.error.spam");
		}
	}

	@Override
	public void perform(final Peep object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Peep object) {
		assert object != null;

		// Return the peep object with the updated nick and text fields
		super.getResponse().setData(super.unbind(object, "moment", "title", "nick", "message", "email", "link"));
	}
}
