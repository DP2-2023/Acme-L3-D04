///*
// * StudentEnrolmentUpdateTest.java
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
//public class StudentEnrolmentUpdateTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected StudentEnrolmentTestRepository repository;
//
//	// Test methods ------------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/enrolment/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String code, final String motivation, final String goals, final String workTime) {
//		// HINT: this test logs in as a lecturer, lists his or her courses, 
//		// HINT+ selects one of them, updates it, and then checks that 
//		// HINT+ the update has actually been performed.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Student", "List enrolments");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.checkColumnHasValue(recordIndex, 0, code);
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkFormExists();
//		super.fillInputBoxIn("motivation", motivation);
//		super.fillInputBoxIn("goals", goals);
//		super.fillInputBoxIn("workTime", workTime);
//		super.clickOnSubmit("Update");
//
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.checkColumnHasValue(recordIndex, 0, code);
//
//		super.clickOnListingRecord(recordIndex);
//		super.checkFormExists();
//		super.checkInputBoxHasValue("code", code);
//		super.fillInputBoxIn("motivation", motivation);
//		super.fillInputBoxIn("goals", goals);
//		super.fillInputBoxIn("workTime", workTime);
//		super.clickOnSubmit("Update");
//
//		super.signOut();
//	}
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/enrolment/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test200Negative(final int recordIndex, final String code, final String motivation, final String goals, final String workTime) {
//		// HINT: this test attempts to update a enrolment with wrong data.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Student", "List enrolments");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.checkColumnHasValue(recordIndex, 0, code);
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkFormExists();
//		super.fillInputBoxIn("motivation", motivation);
//		super.fillInputBoxIn("goals", goals);
//		super.fillInputBoxIn("workTime", workTime);
//		super.clickOnSubmit("Update");
//
//		super.checkErrorsExist();
//
//		super.signOut();
//	}
//
//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to update a enrolment with a role other than "Student",
//		// HINT+ or using a student who is not the owner.
//
//		Collection<Enrolment> enrolments;
//		String param;
//
//		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student2").stream().limit(2).collect(Collectors.toList());
//		for (final Enrolment enrolment : enrolments) {
//			param = String.format("id=%d", enrolment.getId());
//
//			super.checkLinkExists("Sign in");
//			super.request("/student/enrolment/update", param);
//			super.checkPanicExists();
//
//			super.signIn("administrator", "administrator");
//			super.request("/student/enrolment/update", param);
//			super.checkPanicExists();
//			super.signOut();
//
//			super.signIn("student1", "student1");
//			super.request("/student/enrolment/update", param);
//			super.checkPanicExists();
//			super.signOut();
//		}
//	}
//}
