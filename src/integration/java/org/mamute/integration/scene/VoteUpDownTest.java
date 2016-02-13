package org.mamute.integration.scene;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mamute.integration.pages.QuestionPage;
import org.mamute.integration.util.DaoManager;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.VoteType;

public class VoteUpDownTest extends AuthenticatedAcceptanceTest {
    
    private QuestionPage questionPage;
    
    
    @Before
    public void login() {
		DaoManager manager = new DaoManager();

		User author = manager.randomUser();
		Question question = manager.createQuestion(author);
		manager.answerQuestion(author, question);

		loginWithALotOfKarma();
		questionPage = home()
				.toFirstQuestionPage();
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
