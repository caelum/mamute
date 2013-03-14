package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;
import br.com.caelum.brutal.model.VoteType;

public class VoteUpDownTest extends AuthenticatedAcceptanceTest {
    
    private QuestionPage questionPage;
    
    @Before
    public void login() {
    	loginRandomly();
    	createQuestion();
    	logout();
    	loginRandomly();
    	home().toFirstQuestionPage().answer("blablablablablablablablablablablablablab");
    	logout();
    	loginWithALotOfKarma();
        questionPage = home().toFirstQuestionWithAnswerPage();
    }
    
	@Test
    public void should_vote_question_up() throws Exception {
        int initialQuestionVoteCount = questionPage.questionVoteCount();
        int countAfter = questionPage
            .voteQuestion(VoteType.UP)
            .questionVoteCount();
        assertEquals(initialQuestionVoteCount + 1, countAfter);
    }
    
    @Test
    public void should_vote_question_down() throws Exception {
        int initialQuestionVoteCount = questionPage.questionVoteCount();
        int countAfter = questionPage
                .voteQuestion(VoteType.DOWN)
                .questionVoteCount();
        
        assertEquals(initialQuestionVoteCount - 1, countAfter);
    }
    
    @Test
    public void should_vote_answer_up() throws Exception {
        int firstAnswerVoteCount = questionPage.firstAnswerVoteCount();
        int countAfter = questionPage
                .voteFirstAnswer(VoteType.UP)
                .firstAnswerVoteCount();
        
        assertEquals(firstAnswerVoteCount + 1, countAfter);
    }
    
    @Test
    public void should_vote_answer_down() throws Exception {
        int firstAnswerVoteCount = questionPage.firstAnswerVoteCount();
        int countAfter = questionPage
                .voteFirstAnswer(VoteType.DOWN)
                .firstAnswerVoteCount();
        
        assertEquals(firstAnswerVoteCount - 1, countAfter);
    }
    

}
