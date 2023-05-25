/*
 * StudentActivityCreateTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.student.activity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class StudentActivityCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String abstract$, final String type, final String timePeriod, final String furtherInformation) {
		// HINT: this test authenticates as a student and then lists his or her
		// HINT: activities, creates a new one, and checks that it's been created properly.

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "List activities");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("timePeriod", timePeriod);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Student", "List activities");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, abstract$);
		super.checkColumnHasValue(recordIndex, 2, timePeriod);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("timePeriod", timePeriod);
		super.fillInputBoxIn("furtherInformation", furtherInformation);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String abstract$, final String type, final String timePeriod, final String furtherInformation) {
		// HINT: this test attempts to create activities with incorrect data.

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "List activities");
		super.clickOnButton("Create");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("timePeriod", timePeriod);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a activity using principals with
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/student/activity/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer2", "lecturer2");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();
	}

}
