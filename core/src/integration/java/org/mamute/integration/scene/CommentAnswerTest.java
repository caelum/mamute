package org.mamute.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mamute.integration.util.DaoManager;
import org.mamute.model.Question;
import org.mamute.model.User;

public class CommentAnswerTest extends AuthenticatedAcceptanceTest{
	
    @Before
    public void login() {
		DaoManager manager = new DaoManager();

		User author = manager.randomUser();
		Question question = manager.createQuestion(author);
		manager.answerQuestion(author, question);
    }

    @Test
	public void should_comment_answer_after_login() throws Exception {
		this.loginWithALotOfKarma();

		String comment = "my comment my comment my comment";
		home().toFirstQuestionPage()
				.commentFirstAnswer(comment);

		List<String> comments = home().toFirstQuestionPage()
				.firstAnswerComments();
		assertTrue(comments.contains(comment));
	}

}
