package br.com.caelum.brutal.integration.scene;

import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;

public class EditQuestionTest extends AuthenticatedAcceptanceTest {

    @Test
    public void should_edit_question_of_other_author() throws Exception {
        loginRandomly();
        QuestionPage questionPage = home().toFirstQuestionPage()
            .toEditQuestionPage()
            .edit("new title new title new title", 
                "new description new description new description new description new description", 
                "java");
        
    }
    
}
