package org.mamute.integration.scene;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.className;

import org.junit.Before;
import org.junit.Test;
import org.mamute.integration.pages.NewQuestionPage;

public class CreateQuestionTest extends AuthenticatedAcceptanceTest {

	@Before
	public void setup(){
	    logout();
		login();
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
	    
	    newQuestionPage.typeTitle("title");
	    descriptionHintIsVisible = newQuestionPage.descriptionHintIsVisible();
	    titleHintIsVisible = newQuestionPage.titleHintIsVisible();
	    assertFalse(descriptionHintIsVisible);
	    assertTrue(titleHintIsVisible);
    }
	
	@Test
	public void should_disallow_user_to_create_tag() {
		NewQuestionPage newQuestionPage = home()
		        .toNewQuestionPage()
		        .typeDescription("description description description description description")
		        .typeTitle("title title title title")
		        .typeTags("unexistant-tag-blabla");
		newQuestionPage.submit();
		String message = message("question.errors.tags.unexistant");
		assertTrue(newQuestionPage.containsErrorMessageLike(message));
	}
	
	@Test
	public void should_return_question_data_from_server_side() {
		String title = "";
		String description = "description description description description description";
		String tags = "java";
		NewQuestionPage newQuestionPage = home()
				.toNewQuestionPage();
		removeBindsFromElement(className("validated-form"));
		newQuestionPage.newQuestion(title, description, tags);
		
		assertTrue(newQuestionPage.hasInformation(title, description, tags));
		
	}

	
}
