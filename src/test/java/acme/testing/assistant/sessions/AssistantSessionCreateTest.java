
package acme.testing.assistant.sessions;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String resume, final String sessionType, final String periodStart, final String periodEnd, final String information) {
		// HINT: this test authenticates as an assistant, list his or her tutorials, navigates
		// HINT+ to their sessions, and checks that they have the expected data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("resume", resume);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("information", information);
		super.clickOnSubmit("Create");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, sessionType);

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("resume", resume);
		super.checkInputBoxHasValue("sessionType", sessionType);
		super.checkInputBoxHasValue("periodStart", periodStart);
		super.checkInputBoxHasValue("periodEnd", periodEnd);
		super.checkInputBoxHasValue("information", information);
		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String resume, final String sessionType, final String periodStart, final String periodEnd, final String information) {
		// HINT: this test attempts to create sessions using wrong data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("resume", resume);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("periodStart", periodStart);
		super.fillInputBoxIn("periodEnd", periodEnd);
		super.fillInputBoxIn("information", information);
		super.clickOnSubmit("Create");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a session for a tutorial as a principal without 
		// HINT: the "Assistant" role.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("masterId=%d", tutorial.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to create a session for a published tutorial created by 
		// HINT+ the principal.

		Collection<Tutorial> tutorials;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (!tutorial.isPublished()) {
				param = String.format("masterId=%d", tutorial.getId());
				super.request("/assistant/session/create", param);
				super.checkPanicExists();
			}
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to create sessions for tutorials that weren't created 
		// HINT+ by the principal.

		Collection<Tutorial> tutorials;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant2");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("masterId=%d", tutorial.getId());
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
		}
	}

}
