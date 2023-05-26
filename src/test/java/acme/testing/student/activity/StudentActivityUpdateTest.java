///*
// * StudentActivityUpdateTest.java
// *
// * Copyright (C) 2012-2023 Rafael Corchuelo.
// *
// * In keeping with the traditional purpose of furthering education and research, it is
// * the policy of the copyright owner to permit non-commercial use and redistribution of
// * this software. It has been tested carefully, but it is not guaranteed for any particular
// * purposes. The copyright owner does not offer any warranties or representations, nor do
// * they accept any liabilities with respect to them.
// */
//
//package acme.testing.student.activity;
//
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import acme.entities.activities.Activity;
//import acme.testing.TestHarness;
//
//public class StudentActivityUpdateTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected StudentActivityTestRepository repository;
//
//	// Test methods ------------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/activity/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String title, final String abstract$, final String type, final String timePeriod, final String furtherInformation) {
//		// HINT: this test logs in as a student, lists his or her activities, 
//		// HINT+ selects one of them, updates it, and then checks that 
//		// HINT+ the update has actually been performed.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Student", "List activities");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkFormExists();
//		super.checkInputBoxHasValue("title", title);
//		super.checkInputBoxHasValue("abstract$", abstract$);
//		super.checkInputBoxHasValue("type", type);
//		super.checkInputBoxHasValue("timePeriod", timePeriod);
//		super.checkInputBoxHasValue("furtherInformation", furtherInformation);
//		super.clickOnSubmit("Update");
//
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.checkColumnHasValue(recordIndex, 0, title);
//
//		super.clickOnListingRecord(recordIndex);
//		super.checkFormExists();
//		super.checkInputBoxHasValue("title", title);
//		super.checkInputBoxHasValue("abstract$", abstract$);
//		super.checkInputBoxHasValue("type", type);
//		super.checkInputBoxHasValue("timePeriod", timePeriod);
//		super.checkInputBoxHasValue("furtherInformation", furtherInformation);
//
//		super.signOut();
//	}
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/activity/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test200Negative(final int recordIndex, final String title, final String abstract$, final String type, final String timePeriod, final String furtherInformation) {
//		// HINT: this test attempts to update a activity with wrong data.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Student", "List activities");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkFormExists();
//		super.checkInputBoxHasValue("title", title);
//		super.checkInputBoxHasValue("abstract$", abstract$);
//		super.checkInputBoxHasValue("type", type);
//		super.checkInputBoxHasValue("timePeriod", timePeriod);
//		super.checkInputBoxHasValue("furtherInformation", furtherInformation);
//		super.clickOnSubmit("Update");
//
//		super.checkErrorsExist();
//
//		super.signOut();
//	}
//
//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to update a activity with a role other than "Student",
//		// HINT+ or using a student who is not the owner.
//
//		Collection<Activity> activities;
//		String param;
//
//		activities = this.repository.findManyActivitiesByStudentUsername("student2").stream().limit(3).collect(Collectors.toList());
//		for (final Activity activity : activities) {
//			param = String.format("id=%d", activity.getId());
//
//			super.checkLinkExists("Sign in");
//			super.request("/student/activity/update", param);
//			super.checkPanicExists();
//
//			super.signIn("administrator", "administrator");
//			super.request("/student/activity/update", param);
//			super.checkPanicExists();
//			super.signOut();
//
//			super.signIn("student1", "student1");
//			super.request("/student/activity/update", param);
//			super.checkPanicExists();
//			super.signOut();
//		}
//	}
//
//}
