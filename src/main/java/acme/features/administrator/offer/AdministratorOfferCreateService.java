
package acme.features.administrator.offer;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.offers.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.utils.CurrencyExchange;
import spamfilter.SpamFilter;

@Service
public class AdministratorOfferCreateService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository repository;

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
		Offer object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new Offer();
		//object.setMoment(moment);
		object.setPublished(false);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;

		super.bind(object, "heading", "summary", "offerStartDate", "offerEndDate", "price", "link");

		// Currency conversion
		final Locale locale = super.getRequest().getLocale();
		final boolean isAcceptedFormat = CurrencyExchange.isAcceptedFormat(super.getRequest().getData().get("price").toString(), locale);

		if (isAcceptedFormat) {
			final String ratesString = this.repository.findOneConfigByKey("currencyExchangeRates");
			final String systemCurrency = this.repository.findOneConfigByKey("systemCurrency");
			final CurrencyExchange currencyExchange = new CurrencyExchange(ratesString, systemCurrency);
			final Money sourcePrice = super.getRequest().getData("price", Money.class);
			object.setPrice(sourcePrice);
			if (sourcePrice != null) {
				final Money targetPrice = currencyExchange.exchange(sourcePrice);
				if (targetPrice != null)
					object.setPrice(targetPrice);
			}
		}
	}

	@Override
	public void validate(final Offer object) {
		assert object != null;

		final Date start = object.getOfferStartDate();
		final Date end = object.getOfferEndDate();

		final Date minimumStart = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);

		if (start != null && end != null) {
			final long differenceInMillis = end.getTime() - start.getTime();

			final long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

			if (!super.getBuffer().getErrors().hasErrors("offerStartDate"))
				super.state(start.after(minimumStart), "offerStartDate", "administrator.offer.form.error.not.after");

			if (!super.getBuffer().getErrors().hasErrors("offerEndDate"))
				super.state(differenceInDays >= 7, "offerEndDate", "administrator.offer.form.error.not.period.length");
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
			final String formError = "administrator.offer.form.error.spam";

			if (!super.getBuffer().getErrors().hasErrors("heading"))
				super.state(!spamFilter.isSpam(object.getHeading()), "heading", formError);

			if (!super.getBuffer().getErrors().hasErrors("summary"))
				super.state(!spamFilter.isSpam(object.getSummary()), "summary", formError);

			if (!super.getBuffer().getErrors().hasErrors("link"))
				super.state(!spamFilter.isSpam(object.getLink()), "link", formError);
		}

		// Currency conversion
		final String ratesString = this.repository.findOneConfigByKey("currencyExchangeRates");
		final String systemCurrency = this.repository.findOneConfigByKey("systemCurrency");
		final CurrencyExchange currencyExchange = new CurrencyExchange(ratesString, systemCurrency);
		final Locale locale = super.getRequest().getLocale();
		final boolean isAcceptedFormat = CurrencyExchange.isAcceptedFormat(super.getRequest().getData().get("price").toString(), locale);

		if (!super.getBuffer().getErrors().hasErrors("price"))
			if (!isAcceptedFormat)
				super.state(isAcceptedFormat, "price", "administrator.offer.form.error.not-accepted-format");
			else if (object.getPrice() != null) {
				super.state(currencyExchange.isAcceptedCurrency(object.getPrice()), "price", "administrator.offer.form.error.not-accepted-currency");
				super.state(object.getPrice().getAmount() >= 0, "price", "administrator.offer.form.error.negative-price");
			} else
				super.state(object.getPrice() != null, "price", "administrator.offer.form.error.not-accepted-currency");

	}

	@Override
	public void perform(final Offer object) {
		assert object != null;

		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setMoment(moment);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "heading", "summary", "offerStartDate", "offerEndDate", "price", "link");
		tuple.put("readonly", false);

		super.getResponse().setData(tuple);
	}
}
