
package acme.features.administrator.banner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.BannerRepository;
import acme.entities.banner.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import spamfilter.SpamFilter;

@Service
public class AdministratorBannerCreateService extends AbstractService<Administrator, Banner> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected BannerRepository repository;

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
		Banner object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new Banner();
		object.setMoment(moment);
		object.setLinkPicture("");
		object.setSlogan("");
		object.setLinkTarget("");

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "displayPeriodStart", "displayPeriodEnd", "linkPicture", "slogan", "linkTarget");
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		final Date moment = MomentHelper.getCurrentMoment();
		final Date start = object.getDisplayPeriodStart();
		final Date end = object.getDisplayPeriodEnd();

		if (start != null && end != null) {
			final long differenceInMillis = end.getTime() - start.getTime();

			final long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

			if (!super.getBuffer().getErrors().hasErrors("displayPeriodStart"))
				super.state(start.after(moment), "displayPeriodStart", "administrator.banner.form.error.not.after");

			if (!super.getBuffer().getErrors().hasErrors("displayPeriodEnd"))
				super.state(differenceInDays >= 7, "displayPeriodEnd", "administrator.banner.form.error.not.period.length");
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
			final String formError = "administrator.banner.form.error.spam";

			if (!super.getBuffer().getErrors().hasErrors("slogan"))
				super.state(!spamFilter.isSpam(object.getSlogan()), "slogan", formError);
		}

	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setMoment(moment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "displayPeriodStart", "displayPeriodEnd", "linkPicture", "slogan", "linkTarget");
		tuple.put("confirmation", false);
		tuple.put("readonly", false);

		super.getResponse().setData(tuple);
	}

}
