package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;

public class CommentQuestionTest extends AuthenticatedAcceptanceTest {
    
    @Before
    public void setup() {
    }

    @Test
    public void should_comment_question_after_login() throws Exception {
        this.loginWithALotOfKarma();
        
        home()
            .toNewQuestionPage()
            .newQuestion("title title title title", "description description description description ", "java");
        
        String comment = "my comment my comment my comment";
        QuestionPage questionPage = home()
            .toFirstQuestionPage()
            .commentQuestion(comment);
        
        List<String> comments = questionPage.questionComments();
        assertTrue(comments.contains(comment));
    }

}
