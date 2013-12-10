package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.integration.util.DaoManager;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.util.ScriptSessionCreator;

public class CommentQuestionTest extends AuthenticatedAcceptanceTest {

	@Before
	public void login() {
		ScriptSessionCreator sessionFactoryCreator = new ScriptSessionCreator();
		Session session = sessionFactoryCreator.getSession();
		DaoManager manager = new DaoManager(session);

		User author = manager.randomUser();
		manager.createQuestion(author);
	}

	@Test
	public void should_comment_question_after_login() throws Exception {
		this.loginWithALotOfKarma();

		String comment = "my comment my comment my comment";
		home().toFirstQuestionPage()
			.commentQuestion(comment);

		List<String> comments = home()
				.toFirstQuestionPage()
				.questionComments();
		assertTrue(comments.contains(comment));
	}

}
