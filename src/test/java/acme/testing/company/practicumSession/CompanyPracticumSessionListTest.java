
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicums.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum-session/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final String code, final int practicumSessionRecordIndex, final String title, final String practicumSessionType) {
		// HINT: this test authenticates as an company, then lists his or her practicums, 
		// HINT+ selects one of them, and check that it has the expected practicumSessions.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List practicum");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumRecordIndex, 0, code);
		super.clickOnListingRecord(practicumRecordIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Practicum Session");

		super.checkListingExists();
		super.checkColumnHasValue(practicumSessionRecordIndex, 0, title);
		super.checkColumnHasValue(practicumSessionRecordIndex, 1, practicumSessionType);
		super.clickOnListingRecord(practicumSessionRecordIndex);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the practicumSessions of a practicum that is unpublished
		// HINT+ using a principal that didn't create it. 

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (practicum.isPublished()) {
				param = String.format("masterId=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum-session/list-practicum", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum-session/list-practicum", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/practicum-session/list-practicum", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
