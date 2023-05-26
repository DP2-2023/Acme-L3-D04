///*
// * StudentEnrolmentShowTest.java
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
//package acme.testing.student.enrolment;
//
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import acme.entities.enrolments.Enrolment;
//import acme.testing.TestHarness;
//
//public class StudentEnrolmentShowTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected StudentEnrolmentTestRepository repository;
//
//	// Test methods -----------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/enrolment/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String code, final String motivation, final String goals, final String workTime) {
//		// HINT: this test signs in as a lecturer, lists all of the courses, clicks on  
//		// HINT+ one of them, and checks that the form has the expected data.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Student", "List enrolments");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkInputBoxHasValue("code", code);
//		super.checkInputBoxHasValue("motivation", motivation);
//		super.checkInputBoxHasValue("goals", goals);
//		super.checkInputBoxHasValue("workTime", workTime);
//
//		super.signOut();
//	}
//
//	@Test
//	public void test200Negative() {
//		// HINT: there aren't any negative tests for this feature because it's a listing
//		// HINT+ that doesn't involve entering any data in any forms.
//	}
//
//	@Test
//	public void test300Hacking() {
//		Collection<Enrolment> enrolments;
//		String param;
//
//		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student2").stream().limit(2).collect(Collectors.toList());
//		for (final Enrolment enrolment : enrolments) {
//			param = String.format("id=%d", enrolment.getId());
//
//			super.checkLinkExists("Sign in");
//			super.request("/student/enrolment/show", param);
//			super.checkPanicExists();
//
//			super.signIn("administrator", "administrator");
//			super.request("/student/enrolment/show", param);
//			super.checkPanicExists();
//			super.signOut();
//
//			super.signIn("lecturer2", "lecturer2");
//			super.request("/student/enrolment/show", param);
//			super.checkPanicExists();
//			super.signOut();
//		}
//	}
//
//}
