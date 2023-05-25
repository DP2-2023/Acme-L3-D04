///*
// * AssistantTutorialUpdateTest.java
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
//package acme.testing.assistant.tutorial;
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
//public class AssistantTutorialUpdateTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected AssistantTutorialRepositoryTest repository;
//
//	// Test methods ------------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/assistant/tutorial/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String code, final String title, final String resume, final String goals, final String estimatedTotalTime, final String course, final String sessions) {
//		// HINT: this test logs in as a assistant, lists his or her tutorials, 
//		// HINT+ selects one of them, updates it, and then checks that 
//		// HINT+ the update has actually been performed.
//
//		super.signIn("assistant1", "assistant1");
//
//		super.clickOnMenu("Assistant", "List tutorials");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.clickOnListingRecord(recordIndex);
//
//		super.fillInputBoxIn("code", code);
//		super.fillInputBoxIn("title", title);
//		super.fillInputBoxIn("goals", goals);
//		super.fillInputBoxIn("resume", resume);
//		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
//		super.fillInputBoxIn("course", course);
//		super.clickOnSubmit("Update");
//
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.checkColumnHasValue(recordIndex, 0, code);
//
//		super.clickOnListingRecord(recordIndex);
//		super.checkFormExists();
//		super.checkInputBoxHasValue("code", code);
//		super.checkInputBoxHasValue("title", title);
//		super.checkInputBoxHasValue("goals", goals);
//		super.checkInputBoxHasValue("resume", resume);
//		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
//		super.checkInputBoxHasValue("course", course);
//		super.checkInputBoxHasValue("sessions", sessions);
//
//		super.signOut();
//	}
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/assistant/tutorial/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test200Negative(final int recordIndex, final String code, final String title, final String resume, final String goals, final String estimatedTotalTime, final String course, final String sessions) {
//		// HINT: this test attempts to update a tutorial with wrong data.
//
//		super.signIn("assistant1", "assistant1");
//
//		super.clickOnMenu("Assistant", "List tutorials");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.clickOnListingRecord(recordIndex);
//
//		super.checkFormExists();
//		super.fillInputBoxIn("code", code);
//		super.fillInputBoxIn("title", title);
//		super.fillInputBoxIn("goals", goals);
//		super.fillInputBoxIn("resume", resume);
//		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
//		super.fillInputBoxIn("course", course);
//		super.clickOnSubmit("Update");
//
//		super.checkErrorsExist();
//
//		super.signOut();
//	}
//
//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to update a tutorial with a role other than "Assistant",
//		// HINT+ or using a assistant who is not the owner.
//
//		Collection<Tutorial> tutorials;
//		String param;
//
//		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1").stream().limit(3).collect(Collectors.toList());
//		for (final Tutorial tutorial : tutorials) {
//			param = String.format("id=%d", tutorial.getId());
//
//			super.checkLinkExists("Sign in");
//			super.request("/assistant/tutorial/update", param);
//			super.checkPanicExists();
//
//			super.signIn("administrator", "administrator");
//			super.request("/assistant/tutorial/update", param);
//			super.checkPanicExists();
//			super.signOut();
//
//			super.signIn("assistant2", "assistant2");
//			super.request("/assistant/tutorial/update", param);
//			super.checkPanicExists();
//			super.signOut();
//		}
//	}
//
//}
