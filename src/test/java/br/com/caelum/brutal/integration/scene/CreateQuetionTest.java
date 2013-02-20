package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class CreateQuetionTest extends AuthenticatedAcceptanceTest {
	
	@Test
	public void should_make_a_question() {
		String title = "My new question about java";
		String description = "just a question that i have about java hahahhaha";
		String tags = "java";
		boolean isTheQuestion = home()
			.toNewQuestionPage()
			.newQuestion(title, description, tags)
			.hasInformation(title, description, tags);
		assertTrue(isTheQuestion);
	}
	
	@Test
	public void should_suggest_auto_complete() throws InterruptedException {
		boolean hasAutoCompleteSuggestion = home()
				.toNewQuestionPage()
				.typeTags("ja")
				.hasAutoCompleteSuggestion("java");
		assertTrue(hasAutoCompleteSuggestion);
	}
	
	@Test
	public void should_auto_complete_when_click() throws InterruptedException {
		String completedTag = "java";
		String tagChunk = "ja";
		boolean autoCompleted = home()
				.toNewQuestionPage()
				.typeTags(tagChunk)
				.selectAutoCompleteSuggestion(completedTag)
				.hasTag(completedTag);
		assertTrue(autoCompleted);
	}

}
