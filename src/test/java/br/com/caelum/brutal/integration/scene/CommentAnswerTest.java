package br.com.caelum.brutal.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.integration.pages.QuestionPage;

public class CommentAnswerTest extends AuthenticatedAcceptanceTest{
	
	@Test
	public void should_comment_answer_after_login() throws Exception {
		this.loginWithALotOfKarma();

		home().toNewQuestionPage()
				.newQuestion("title title title title",
				"description description description description ", "java")
				.answer("answer answer answer answer answer answer");

		String comment = "my comment my comment my comment";
		QuestionPage questionPage = home().toFirstQuestionPage()
				.commentFirstAnswer(comment);

		List<String> comments = questionPage.firstAnswerComments();
		assertTrue(comments.contains(comment));
	}

}
