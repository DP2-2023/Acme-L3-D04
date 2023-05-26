///*
// * StudentActivityListTest.java
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
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//
//import acme.testing.TestHarness;
//
//public class StudentActivityListMineTest extends TestHarness {
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/activity/list-mine-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String title, final String abstract$, final String timePeriod) {
//		// HINT: this test authenticates as a student, lists his or her activities only,
//		// HINT+ and then checks that the listing has the expected data.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Student", "List activities");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.checkColumnHasValue(recordIndex, 0, title);
//		super.checkColumnHasValue(recordIndex, 1, abstract$);
//		super.checkColumnHasValue(recordIndex, 2, timePeriod);
//
//		super.signOut();
//	}
//
//	@Test
//	public void test200Negative() {
//		// HINT: there aren't any negative tests for this feature since it's a listing that
//		// HINT+ doesn't involve entering any data into any forms.
//	}
//
//	@Test
//	public void test300Hacking() {
//		super.checkLinkExists("Sign in");
//		super.request("/student/activity/list-mine");
//		super.checkPanicExists();
//
//		super.signIn("administrator", "administrator");
//		super.request("/student/activity/list-mine");
//		super.checkPanicExists();
//		super.signOut();
//
//		super.signIn("lecturer1", "lecutrer1");
//		super.request("/student/activity/list-mine");
//		super.checkPanicExists();
//		super.signOut();
//	}
//
//}
