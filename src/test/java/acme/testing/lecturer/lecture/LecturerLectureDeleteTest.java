/*
 * LecturerCourseDeleteTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.lecturer.lecture;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lectures.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String isPublished) {
		// HINT: this test authenticates as a lecturer, lists his or her lectures,
		// HINT: then selects one of them, and deletes it.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 2, isPublished);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title) {
		// HINT: this test attempts to delete a lecture that cannot be deleted, yet (the lecture is in a course).
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkErrorsExist();

		super.clickOnMenu("Lecturer", "List lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete a lecture with a role other than "Lecturer".
		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1").stream().limit(2).collect(Collectors.toList());
		for (final Lecture lecture : lectures) {
			param = String.format("id=%d", lecture.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to delete a published lecture that was registered by the principal.

		super.signIn("lecturer1", "lecturer1");

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1").stream().limit(3).collect(Collectors.toList());
		for (final Lecture lecture : lectures)
			if (lecture.isPublished()) {
				param = String.format("id=%d", lecture.getId());
				super.request("/lecturer/lecture/delete", param);
			}
		super.signOut();
	}

}
