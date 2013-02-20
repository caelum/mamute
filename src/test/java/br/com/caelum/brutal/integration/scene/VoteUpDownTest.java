package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;
import br.com.caelum.brutal.model.VoteType;

public class VoteUpDownTest extends AcceptanceTestBase {
    
    private int initialVoteCount;

    @Before
    public void setup() {
        loginRandomly();
    }
    @After
    public void logout() {
        home().logOut();
    }
    
    @Test
    public void should_vote_question_up() throws Exception {
        QuestionPage questionPage = home()
                .toFirstQuestionPage();
        
        initialVoteCount = questionPage
                .getQuestionVoteCount();
        
        int countAfter = questionPage
            .voteQuestion(VoteType.UP)
            .getQuestionVoteCount();
        
        assertEquals(initialVoteCount + 1, countAfter);
    }
    
    @Test
    public void should_vote_question_down() throws Exception {
        QuestionPage questionPage = home()
                .toFirstQuestionPage();
        
        int countBefore = questionPage
                .getQuestionVoteCount();
        
        int countAfter = questionPage
                .voteQuestion(VoteType.DOWN)
                .getQuestionVoteCount();
        
        assertEquals(countBefore - 1, countAfter);
    }
    
    @Test
    public void should_vote_answer_up() throws Exception {
        QuestionPage questionPage = home()
                .toFirstQuestionPage();
        
        int countBefore = questionPage
                .getQuestionVoteCount();
        
        int countAfter = questionPage
                .voteQuestion(VoteType.DOWN)
                .getQuestionVoteCount();
        
        assertEquals(countBefore - 1, countAfter);
    }

    private void loginRandomly() {
        home().toSignUpPage()
                .signUp("chico sokol", "chico"+new Random().nextLong()+"@brutal.com", "123456", "123456");
    }
    
}
