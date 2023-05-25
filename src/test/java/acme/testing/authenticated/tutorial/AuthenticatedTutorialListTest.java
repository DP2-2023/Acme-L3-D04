///*
// * LecturerLectureListTest.java
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
//package acme.testing.authenticated.tutorial;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//
//import acme.testing.TestHarness;
//
//public class AuthenticatedTutorialListTest extends TestHarness {
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/authenticated/tutorial/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String code, final String title, final String isPublished) {
//		// HINT: this test authenticates as a assistant, lists his or her tutorials only,
//		// HINT+ and then checks that the listing has the expected data.
//
//		super.signIn("administrator", "administrator");
//
//		super.clickOnMenu("Authenticated", "List tutorials");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.checkColumnHasValue(recordIndex, 0, code);
//		super.checkColumnHasValue(recordIndex, 1, title);
//		super.checkColumnHasValue(recordIndex, 3, isPublished);
//
//		super.signOut();
//	}
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/authenticated/tutorial/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test101Positive(final int recordIndex, final String code, final String title, final String isPublished) {
//		// HINT: this test authenticates as a assistant, lists his or her tutorials only,
//		// HINT+ and then checks that the listing has the expected data.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Authenticated", "List tutorials");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.checkColumnHasValue(recordIndex, 0, code);
//		super.checkColumnHasValue(recordIndex, 1, title);
//		super.checkColumnHasValue(recordIndex, 3, isPublished);
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
//		// HINT: the test hacking is already done in assistant tutorial test
//	}
//
//}
