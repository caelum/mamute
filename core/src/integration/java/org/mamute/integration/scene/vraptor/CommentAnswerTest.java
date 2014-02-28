package org.mamute.integration.scene.vraptor;

import static org.junit.Assert.assertEquals;

import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;
import org.mamute.model.Answer;
import org.mamute.model.Question;
import org.mamute.model.User;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

@Ignore
public class CommentAnswerTest extends CustomVRaptorIntegration {

	@Test
	public void should_comment_answer_after_login() throws Exception {
		User moderator = moderator();
		Question question = createQuestionWithDao(moderator, "Should comment answer after login",
				"Yeah, definitely should, who're you to say otherwise?", tag("java"));
		Answer answer = answerQuestionWithDao(karmaNigga(), question,
				"I'm de guy with lots of karma, nigga! So get over here!", false);

		String comment = "Oh, right, then I can't do a thing about it.";
		UserFlow navigation = login(navigate(), moderator.getEmail());
		navigation = commentWithFlow(navigation, answer, comment, false);
		VRaptorTestResult commentResult = navigation.followRedirect().execute();
		commentResult.wasStatus(200).isValid(); // COULD NOT COMPILE JSP

		navigation = login(navigate(), moderator.getEmail());
		navigation = goToQuestionPage(navigation, question);
		VRaptorTestResult commentedAnswer = navigation.followRedirect().execute();
		commentedAnswer.wasStatus(200).isValid();

		Elements commentElement = getElementsByClass(commentedAnswer.getResponseBody(),
				"comment-container");
		Elements commentSpan = getElementsByTag(commentElement.html(), "span");
		Elements commentParagraph = getElementsByTag(commentSpan.html(), "p");
		assertEquals(comment, commentParagraph.html());
	}

}