
package acme.testing.company.practicum;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicums.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code) {
		// HINT: this test authenticates as a company, lists his or her practicums,
		// HINT: then selects one of them, and publishes it.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List practicum");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, code);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.checkColumnHasValue(recordIndex, 3, "True");

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: Only requirement for publishing a practicum is that it is not published.
		// HINT+ Publishing a published practicum is a hacking test.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a practicum with a role other than "Company".

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1").stream().limit(3).collect(Collectors.toList());

		for (final Practicum practicum : practicums) {
			param = String.format("id=%d", practicum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/practicum/publish", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/company/practicum/publish", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/company/practicum/publish", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published practicum that was registered by the principal.

		super.signIn("company1", "company1");

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1").stream().limit(3).collect(Collectors.toList());
		for (final Practicum practicum : practicums)
			if (practicum.isPublished()) {
				param = String.format("id=%d", practicum.getId());
				super.request("/company/practicum/publish", param);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a practicum that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		super.signIn("company2", "company2");

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1").stream().limit(3).collect(Collectors.toList());
		for (final Practicum practicum : practicums) {
			param = String.format("id=%d", practicum.getId());
			super.request("/company/practicum/publish", param);
		}
		super.signOut();
	}

}
