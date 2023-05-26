/*
 * LecturerLecturePublishTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.assistant.tutorial;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code) {
		// HINT: this test authenticates as a assistant, lists his or her tutorials,
		// HINT: then selects one of them, and publishes it.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List tutorials");
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
		// HINT: Only requirement for publishing a tutorial is that it is not published.
		// HINT+ Publishing a published tutorial is a hacking test.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a tutorial with a role other than "Assistant".

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1").stream().limit(3).collect(Collectors.toList());

		for (final Tutorial tutorial : tutorials) {
			param = String.format("id=%d", tutorial.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/tutorial/publish", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/tutorial/publish", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/tutorial/publish", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published tutorial that was registered by the principal.

		super.signIn("assistant1", "assistant1");

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1").stream().limit(3).collect(Collectors.toList());
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isPublished()) {
				param = String.format("id=%d", tutorial.getId());
				super.request("/assistant/tutorial/publish", param);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a tutorial that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		super.signIn("assistant2", "assistant2");

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1").stream().limit(3).collect(Collectors.toList());
		for (final Tutorial tutorial : tutorials) {
			param = String.format("id=%d", tutorial.getId());
			super.request("/assistant/tutorial/publish", param);
		}
		super.signOut();
	}

}
