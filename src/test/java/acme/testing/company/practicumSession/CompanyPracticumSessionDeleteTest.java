
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicumSessions.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum-session/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int practicumSessionRecordIndex, final String title, final String practicumSessionType) {
		// HINT: this test authenticates as a company, lists his or her practicumSessions,
		// HINT: then selects one of them, and deletes it.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List practicum");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Practicum Session");
		super.checkColumnHasValue(practicumSessionRecordIndex, 0, title);
		super.checkColumnHasValue(practicumSessionRecordIndex, 1, practicumSessionType);
		super.clickOnListingRecord(practicumSessionRecordIndex);

		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final int practicumSessionRecordIndex, final String title, final String practicumSessionType) {
		// HINT: this test is done in hacking test.

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete a practicumSession with a role other than "company".
		Collection<PracticumSession> practicumSessions;
		String param;

		super.signIn("company1", "company1");
		practicumSessions = this.repository.findManyPracticumSessionByCompanyUsername("company2");
		for (final PracticumSession practicumSession : practicumSessions)
			if (practicumSession.getPracticum().isPublished()) {
				param = String.format("id=%d", practicumSession.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum-session/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/practicum-session/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
