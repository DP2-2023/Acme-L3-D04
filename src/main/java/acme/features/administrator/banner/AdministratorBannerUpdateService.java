
package acme.features.administrator.banner;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
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
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected BannerRepository repository;

	// AbstractService<Employer, Company> -------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Banner object;
		int id;
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object = new Banner();
		object.setMoment(moment);

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneBannerById(id);

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
		final Date start = object.getDisplayPeriodStart();
		final Date end = object.getDisplayPeriodEnd();

		boolean periodValidation;

		periodValidation = end.before(DateUtils.addDays(start, 7));
		super.state(!periodValidation, "periodValidation", "javax.validation.constraints.AssertTrue.message");

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

		super.getResponse().setData(tuple);
	}
}
