package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;

public class EditQuestionTest extends AuthenticatedAcceptanceTest {

    @Test
    public void should_edit_question_of_other_author() throws Exception {
    	loginRandomly();
    	home().toNewQuestionPage()
        .newQuestion("Titulo Bonitao Lindo Maravilhso muito Bacana", 
            "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription", 
            "java");
    	logout();
        loginWithALotOfKarma();
        QuestionPage questionPage = home().toFirstQuestionPage()
            .toEditQuestionPage()
            .edit("new title new title new title", 
                "new description new description new description new description new description", 
                "java");
        boolean containsConfirmationMessage = questionPage
            .containsConfirmationMessageLike(message("status.pending"));
        
        assertTrue(containsConfirmationMessage);
    }
    
    @Test
    public void should_edit_and_automatically_approve_author_edit() throws Exception {
        loginRandomly();
        QuestionPage questionPage = home().toNewQuestionPage()
            .newQuestion("question title question title question title", 
                "question description question description question description question description ", 
                "java");
        
        String newTitle = "edited by author question title";
        String newDescription = "new description new description new description new description";
        String newTags = "ruby";
        questionPage = questionPage.toEditQuestionPage()
            .edit(newTitle, 
                newDescription, 
                newTags);
        
        questionPage.hasInformation(newTitle, newDescription, newTags);
        questionPage.confirmationMessages().contains(message("status.no_need_to_approve"));
    }
    
    @Test
    public void should_edit_and_automatically_approve_moderator() throws Exception {
        loginRandomly();
        createQuestion();
        logout();
        loginAsModerator();
        
        String newTitle = "I'm the moderator, nigga";
        String newDescription = "yeah yeah yeah yeah yeah yeah yeah yeah";
        String newTags = "ruby";
        QuestionPage questionPage = home().toFirstQuestionPage().toEditQuestionPage()
            .edit(newTitle, 
                    newDescription, 
                    newTags);
        
        questionPage.confirmationMessages().contains(message("status.no_need_to_approve"));
        questionPage.hasInformation(newTitle, newDescription, newTags);
        
    }

}
