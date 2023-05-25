/*
 * LecturerLectureShowTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.authenticated.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuthenticatedTutorialShowTest extends TestHarness {

	// Test methods -----------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/tutorial/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String resume, final String goals, final String estimatedTotalTime, final String course, final String sessions, final String assistant) {
		// HINT: this test signs in as a assistant, lists all of the tutorials, clicks on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("administrator", "administrator");

		super.clickOnMenu("Authenticated", "List tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("resume", resume);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("numSessions", sessions);
		super.checkInputBoxHasValue("assistantName", assistant);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/tutorial/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test101Positive(final int recordIndex, final String code, final String title, final String resume, final String goals, final String estimatedTotalTime, final String course, final String sessions, final String assistant) {
		// HINT: this test signs in as a assistant, lists all of the tutorials, clicks on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("student2", "student2");

		super.clickOnMenu("Authenticated", "List tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("resume", resume);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("numSessions", sessions);
		super.checkInputBoxHasValue("assistantName", assistant);

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
