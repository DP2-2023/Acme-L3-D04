
package acme.testing.authenticated.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuthenticatedPracticumShowTest extends TestHarness {

	// Test methods -----------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/practicum/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String abstract$, final String goals, final String estimatedTotalTime, final String course, final String practicumSessions, final String company) {
		// HINT: this test signs in as a assistant, lists all of the tutorials, clicks on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("administrator", "administrator");

		super.clickOnMenu("Authenticated", "List practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("numPracticumSessions", practicumSessions);
		super.checkInputBoxHasValue("companyName", company);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/practicum/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test101Positive(final int recordIndex, final String code, final String title, final String abstract$, final String goals, final String estimatedTotalTime, final String course, final String practicumSessions, final String company) {
		// HINT: this test signs in as a assistant, lists all of the tutorials, clicks on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("student2", "student2");

		super.clickOnMenu("Authenticated", "List practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("numPracticumSessions", practicumSessions);
		super.checkInputBoxHasValue("companyName", company);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: the test hacking is already done in assistant tutorial test
	}
}
