package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;
import br.com.caelum.brutal.integration.util.DaoManager;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.util.ScriptSessionCreator;

public class CommentAnswerTest extends AuthenticatedAcceptanceTest{
	
    @Before
    public void login() {
		ScriptSessionCreator sessionFactoryCreator = new ScriptSessionCreator();
		Session session = sessionFactoryCreator.getSession();
		DaoManager manager = new DaoManager(session);

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
