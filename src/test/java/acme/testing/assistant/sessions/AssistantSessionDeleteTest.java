
package acme.testing.assistant.sessions;

/*
 * assistantCourseDeleteTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.Session;
import acme.testing.TestHarness;

public class AssistantSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String sessionType) {
		// HINT: this test authenticates as a assistant, lists his or her sessions,
		// HINT: then selects one of them, and deletes it.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Sessions");
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, sessionType);
		super.clickOnListingRecord(sessionRecordIndex);

		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String sessionType) {
		// HINT: this test is done in hacking test.

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete a session with a role other than "assistant".
		Collection<Session> sessions;
		String param;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findManySessionByAssistantUsername("assistant2");
		for (final Session session : sessions)
			if (session.getTutorial().isPublished()) {
				param = String.format("id=%d", session.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/session/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant2", "assistant2");
				super.request("/assistant/session/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
