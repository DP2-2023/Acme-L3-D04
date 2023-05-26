///*
// * StudentEnrolmentFinaliseTest.java
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
//public class StudentEnrolmentFinaliseTest extends TestHarness {
//
//	// Internal data ----------------------------------------------------------
//
//	@Autowired
//	protected StudentEnrolmentTestRepository repository;
//
//	// Test methods -----------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/enrolment/finalise-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String code) {
//		// HINT: this test authenticates as a student, lists his or her enrolments,
//		// HINT: then selects one of them, and finalises it.
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
//		super.clickOnSubmit("Finalise");
//		super.checkNotErrorsExist();
//
//		super.signOut();
//	}
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/enrolment/finalise-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test200Negative(final int recordIndex, final String code) {
//		// HINT: this test attempts to finalise a enrolment that cannot be finalised, yet.
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
//		super.clickOnSubmit("Finalise");
//		super.checkErrorsExist();
//
//		super.signOut();
//	}
//
//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to finalise a enrolment with a role other than "Student".
//
//		Collection<Enrolment> enrolments;
//		String param;
//
//		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student2").stream().limit(3).collect(Collectors.toList());
//
//		for (final Enrolment enrolment : enrolments) {
//			param = String.format("id=%d", enrolment.getId());
//
//			super.checkLinkExists("Sign in");
//			super.request("/student/enrolment/finalise", param);
//			super.checkPanicExists();
//
//			super.signIn("administrator", "administrator");
//			super.request("/student/enrolment/finalise", param);
//			super.checkPanicExists();
//			super.signOut();
//
//			super.signIn("lecturer2", "lecturer2");
//			super.request("/student/enrolment/finalise", param);
//			super.checkPanicExists();
//			super.signOut();
//		}
//	}
//
//	@Test
//	public void test301Hacking() {
//		// HINT: this test tries to finalise a finalised enrolment that was registered by the principal.
//
//		super.signIn("student2", "student2");
//
//		Collection<Enrolment> enrolments;
//		String param;
//
//		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student2").stream().limit(3).collect(Collectors.toList());
//		for (final Enrolment enrolment : enrolments)
//			if (enrolment.isFinished()) {
//				param = String.format("id=%d", enrolment.getId());
//				super.request("/student/enrolment/finalise", param);
//			}
//		super.signOut();
//	}
//
//	@Test
//	public void test302Hacking() {
//		// HINT: this test tries to finalise a enrolment that wasn't registered by the principal,
//		// HINT+ be it finalised or no finalised.
//
//		super.signIn("student1", "student1");
//
//		Collection<Enrolment> enrolments;
//		String param;
//
//		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student2").stream().limit(3).collect(Collectors.toList());
//		for (final Enrolment enrolment : enrolments) {
//			param = String.format("id=%d", enrolment.getId());
//			super.request("/student/enrolment/finalise", param);
//		}
//		super.signOut();
//	}
//
//}
