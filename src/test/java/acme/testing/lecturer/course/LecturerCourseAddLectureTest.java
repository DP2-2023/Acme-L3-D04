
package acme.testing.lecturer.course;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.courses.Course;
import acme.testing.TestHarness;

public class LecturerCourseAddLectureTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	//
	@Autowired
	protected LecturerCourseTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/add-lecture-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String lecture) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.clickOnButton("Add lecture");
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.fillInputBoxIn("lecture", lecture);
		super.clickOnSubmit("Add lecture");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/add-lecture-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String lecture) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.clickOnButton("Add lecture");
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.clickOnSubmit("Add lecture");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		final Collection<Course> courses;
		String param;

		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1").stream().limit(2).collect(Collectors.toList());
		for (final Course course : courses) {
			param = String.format("id=%d", course.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/course/add-lecture", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/lecturer/course/add-lecture", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/course/add-lecture", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
