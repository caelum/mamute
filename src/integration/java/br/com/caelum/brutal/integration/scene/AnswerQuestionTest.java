package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AnswerQuestionTest extends AuthenticatedAcceptanceTest{

	@Test
	public void should_answer_when_logged_in(){
		login();
		
		String newDescription = "My very very awsome Answer big enough :)";
		boolean firstAnswerHasDescription = home().toNewQuestionPage()
				.newQuestion("my question my question my question",
				"my description my description my description", "java")
				.answer(newDescription)
				.firstAnswerHasDescription(newDescription);
		
		assertTrue(firstAnswerHasDescription);
	}
	
	@Test
	public void should_not_display_answer_form_when_not_logged_in(){
		login();
		
		home().toNewQuestionPage()
			.newQuestion("my question my question my question",
			"my description my description my description", "java");

		logout();
		boolean isAnswerFormDisplayed = home()
			.toFirstQuestionPage()
			.isAnswerFormDisplayed();

		assertFalse(isAnswerFormDisplayed);
	}
	
	@Test
	public void should_not_display_answer_form_when_already_answered(){
		login();

		String newDescription = "My very very awsome Answer big enough :)";
		boolean isAnswerFormDisplayed = home().toNewQuestionPage()
				.newQuestion("my question my question my question",
				"my description my description my description", "java")
				.answer(newDescription)		
				.isAnswerFormDisplayed();
		
		assertFalse(isAnswerFormDisplayed);
	}
}