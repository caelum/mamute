package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class QuestionTest extends AuthTestBase{
	
	@Before
	public void set_up(){
		home().toLoginPage().login("leonardo.wolter@caelum.com.br", "123456");
	}
	
	@Test
	public void should_make_a_question(){
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
	public void should_edit_a_question(){
		String title = "New Question Title";
		String description = "New Question Description about java";
		String tags = "java new";
		boolean hasInformation = home().toFirstQuestionPage()
				.toEditQuestionPage()
				.edit(title, description, tags)
				.hasInformation(title, description, tags);
		assertTrue(hasInformation);
	}
	
	@Test
	public void should_suggest_auto_complete() throws InterruptedException{
		boolean hasAutoCompleteSuggestion = home()
				.toNewQuestionPage()
				.typeTags("ja")
				.hasAutoCompleteSuggestion("java");
		assertTrue(hasAutoCompleteSuggestion);
	}

}
