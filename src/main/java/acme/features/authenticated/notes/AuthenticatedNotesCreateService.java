
package acme.features.authenticated.notes;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.notes.Note;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import spamfilter.SpamFilter;

@Service
public class AuthenticatedNotesCreateService extends AbstractService<Authenticated, Note> {

	@Autowired
	protected AuthenticatedNotesRepository repository;


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
		Note object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new Note();
		object.setTitle("");
		object.setMoment(moment);
		object.setAuthor("");
		object.setMessage("");
		object.setEmail("");
		object.setLink("");

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Note object) {
		assert object != null;

		super.bind(object, "moment", "title", "message", "email", "link");

		final String name = super.getRequest().getData("name", String.class);
		final String surname = super.getRequest().getData("surname", String.class);
		final String username = super.getRequest().getPrincipal().getUsername();

		object.setAuthor(String.format("%s - %s, %s", username, surname, name));

	}

	@Override
	public void validate(final Note object) {
		assert object != null;
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

		final String name = super.getRequest().getData("name", String.class);
		final String surname = super.getRequest().getData("surname", String.class);

		// Name and surname
		if (!super.getBuffer().getErrors().hasErrors("name"))
			super.state(!name.trim().isEmpty(), "name", "authenticated.notes.form.error.blank");

		if (!super.getBuffer().getErrors().hasErrors("surname"))
			super.state(!surname.trim().isEmpty(), "surname", "authenticated.notes.form.error.blank");

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
			final String formError = "authenticated.notes.form.error.spam";

			if (!super.getBuffer().getErrors().hasErrors("title"))
				super.state(!spamFilter.isSpam(object.getTitle()), "title", formError);

			if (!super.getBuffer().getErrors().hasErrors("author"))
				super.state(!spamFilter.isSpam(object.getAuthor()), "author", formError);

			if (!super.getBuffer().getErrors().hasErrors("message"))
				super.state(!spamFilter.isSpam(object.getMessage()), "message", formError);

			if (!super.getBuffer().getErrors().hasErrors("email"))
				super.state(!spamFilter.isSpam(object.getEmail()), "email", formError);

			if (!super.getBuffer().getErrors().hasErrors("link"))
				super.state(!spamFilter.isSpam(object.getLink()), "link", formError);
		}

	}

	@Override
	public void perform(final Note object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Note object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "moment", "title", "author", "message", "email", "link");

		tuple.put("confirmation", false);

		super.getResponse().setData(tuple);
	}

}
