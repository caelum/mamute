package br.com.caelum.brutal.integration.scene;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.NewQuestionPage;

public class CreateQuetionTest extends AuthenticatedAcceptanceTest {

	@Before
	public void setup(){
		login();
	}

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
	
	@Test
    public void should_show_hints_when_editing_form() throws Exception {
	    NewQuestionPage newQuestionPage = home()
	        .toNewQuestionPage()
	        .typeDescription("description");
	    boolean descriptionHintIsVisible = newQuestionPage.descriptionHintIsVisible();
	    boolean titleHintIsVisible = newQuestionPage.titleHintIsVisible();
	    assertTrue(descriptionHintIsVisible);
	    assertFalse(titleHintIsVisible);
	    
	    Thread.sleep(1000);
	    
	    newQuestionPage.typeTitle("title");
	    descriptionHintIsVisible = newQuestionPage.descriptionHintIsVisible();
	    titleHintIsVisible = newQuestionPage.titleHintIsVisible();
	    assertFalse(descriptionHintIsVisible);
	    assertTrue(titleHintIsVisible);
    }

}
