package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;

public class EditAnswerTest extends AuthenticatedAcceptanceTest {

    @Test
    public void should_edit_answer_of_other_author() throws Exception {
    	loginAsModerator();
    	home().toNewQuestionPage().newQuestion("question question question question", 
    			"description description description description description", "java")
    			.answer("answer answer answer answer answer answer answer answer");
    	logout();
    	
        loginWithALotOfKarma();
        
        QuestionPage questionPage = home().toFirstQuestionPage()
            .toEditFirstAnswerPage()
            .edit("new answer with more than 30 characters aw  yeah !!!", 
                "any comment");
        
        boolean containsConfirmationMessage = questionPage
            .confirmationMessages()
            .contains(message("status.pending"));
        
        assertTrue(containsConfirmationMessage);
    }
    
    @Test
    public void should_edit_and_automatically_approve_author_edit() throws Exception {
        loginWithALotOfKarma();
        
        String newDescription = "my new description of the first answer also with more than 30 characters";
        home().toNewQuestionPage()
        	.newQuestion("question title question title question title",
        			"question description question description question description question description ",
        			"java")
        	.answer("my new answer with more than 30 characters hahahahaha");
        QuestionPage questionPage = home().toFirstQuestionPage()
        	.toEditFirstAnswerPage()
            .edit(newDescription, "'cause I have to test it");
        
        boolean firstAnswerHasDescription = questionPage.firstAnswerHasDescription(newDescription);
        
        boolean containsConfirmationMessage = questionPage.confirmationMessages()
                .contains(message("status.no_need_to_approve"));

        assertTrue(firstAnswerHasDescription);
        assertTrue(containsConfirmationMessage);
    }
    
    @Test
    public void should_edit_and_automatically_approve_moderator() throws Exception {
    	loginWithALotOfKarma();
        home().toNewQuestionPage()
            .newQuestion("question title question title question title", 
                "question description question description question description question description ", 
                "java")
            .answer("my new answer with more than 30 characters hahahahaha");
        logout();
        loginAsModerator();
        
        String newDescription = "yeah yeah yeah yeah yeah yeah yeah yeah";
        QuestionPage questionPage = home().toFirstQuestionWithAnswerPage()
        		.toEditFirstAnswerPage()
            .edit(newDescription,"'cause I need to test it!");
        
        questionPage.confirmationMessages().contains(message("status.no_need_to_approve"));
        questionPage.firstAnswerHasDescription(newDescription);
    }
}
