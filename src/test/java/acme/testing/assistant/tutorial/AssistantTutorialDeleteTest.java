//
//package acme.testing.assistant.tutorial;
///*
// * assistantCourseDeleteTest.java
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
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import acme.entities.tutorial.Tutorial;
//import acme.testing.TestHarness;
//
//public class AssistantTutorialDeleteTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected AssistantTutorialRepositoryTest repository;
//
//	// Test methods -----------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/assistant/tutorial/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String code, final String isPublished) {
//		// HINT: this test authenticates as a assistant, lists his or her tutorials,
//		// HINT: then selects one of them, and deletes it.
//
//		super.signIn("assistant1", "assistant1");
//
//		super.clickOnMenu("Assistant", "List tutorials");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.checkColumnHasValue(recordIndex, 0, code);
//		super.checkColumnHasValue(recordIndex, 3, isPublished);
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkFormExists();
//		super.clickOnSubmit("Delete");
//		super.checkNotErrorsExist();
//
//		super.signOut();
//	}
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/assistant/tutorial/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test200Negative(final int recordIndex, final String code) {
//		// HINT: this test attempts to delete a tutorial that cannot be deleted, yet (the tutorial is in a course).
//		super.signIn("assistant1", "assistant1");
//
//		super.clickOnMenu("Assistant", "List tutorials");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.checkColumnHasValue(recordIndex, 0, code);
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkFormExists();
//		super.clickOnSubmit("Delete");
//		super.checkErrorsExist();
//
//		super.clickOnMenu("Assistant", "List tutorials");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.checkColumnHasValue(recordIndex, 0, code);
//
//		super.signOut();
//	}
//
//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to delete a tutorial with a role other than "assistant".
//		final Collection<Tutorial> tutorials;
//		String param;
//
//		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1").stream().limit(2).collect(Collectors.toList());
//		for (final Tutorial tutorial : tutorials) {
//			param = String.format("id=%d", tutorial.getId());
//
//			super.checkLinkExists("Sign in");
//			super.request("/assistant/tutorial/delete", param);
//			super.checkPanicExists();
//
//			super.signIn("administrator", "administrator");
//			super.request("/assistant/tutorial/delete", param);
//			super.checkPanicExists();
//			super.signOut();
//
//			super.signIn("assistant2", "assistant2");
//			super.request("/assistant/tutorial/delete", param);
//			super.checkPanicExists();
//			super.signOut();
//		}
//	}
//
//	@Test
//	public void test301Hacking() {
//		// HINT: this test tries to delete a published tutorial that was registered by the principal.
//
//		super.signIn("assistant1", "assistant1");
//
//		final Collection<Tutorial> tutorials;
//		String param;
//
//		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1").stream().limit(3).collect(Collectors.toList());
//		for (final Tutorial tutorial : tutorials)
//			if (tutorial.isPublished()) {
//				param = String.format("id=%d", tutorial.getId());
//				super.request("/assistant/tutorial/delete", param);
//			}
//		super.signOut();
//	}
//
//}
