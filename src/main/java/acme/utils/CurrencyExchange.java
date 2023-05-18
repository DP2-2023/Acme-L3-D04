
package acme.utils;

import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import acme.framework.components.datatypes.Money;

public class CurrencyExchange {

	private final Map<String, Double>	rates;
	private final String				systemCurrency;


	public CurrencyExchange(final String ratesString, final String systemCurrency) {
		super();

		final Map<String, Double> rates = new HashMap<>();
		final String[] ratePairs = ratesString.split(",");
		for (final String ratePair : ratePairs) {
			final String[] rateParts = ratePair.split(":");
			final String currency = rateParts[0];
			final double rate = Double.parseDouble(rateParts[1]);
			rates.put(currency, rate);
		}
		this.rates = rates;
		this.systemCurrency = systemCurrency;
	}

	public Money exchange(final Money source) {
		if (!this.rates.containsKey(source.getCurrency()))
			return null;
		final Money target = new Money();
		target.setAmount(source.getAmount() * this.rates.get(source.getCurrency()));
		target.setCurrency(this.systemCurrency);
		return target;
	}

	public boolean isAcceptedCurrency(final Money source) {
		return this.rates.containsKey(source.getCurrency());
	}

	public Map<String, Double> getRates() {
		return this.rates;
	}

	public String getSystemCurrency() {
		return this.systemCurrency;
	}

	public static boolean isAcceptedFormat(final String string, final Locale locale) {
		final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
		final String thousandSeparator = Character.toString(symbols.getGroupingSeparator());
		final String decimalSeparator = Character.toString(symbols.getDecimalSeparator());
		final String currencyRegex = "[\\p{L}\\p{Sc}]+";
		final String numberRegex = String.format("[+-]?(\\d+|\\d{1,3}(\\%s\\d{3})*)(\\%s\\d{1,2})?", thousandSeparator, decimalSeparator);

		final String regex = String.format("^((?<C1>%1$s)\\s*(?<A1>%2$s))$|^((?<A2>%2$s)\\s*(?<C2>%1$s))$", currencyRegex, numberRegex);
		final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

		final Matcher matcher = pattern.matcher(string);

		return matcher.find();
	}

}
