package org.mamute.integration.scene.vraptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.select.Elements;
import org.junit.Test;
import org.mamute.integration.util.DaoManager;
import org.mamute.model.Question;
import org.mamute.model.User;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class EditQuestionTest extends CustomVRaptorIntegration {

	@Test
	public void should_edit_question_of_other_author() throws Exception {
		Question question = createQuestionWithDao(moderator(),
				"Title title title title title title title",
				"Description description description description description", tag("java"));

		String newTitle = "NEW title title title title title title title";
		String newDescription = "NEW description description description description description";
		UserFlow navigation = login(navigate(), karmaNigga().getEmail());
		navigation = editQuestionWithFlow(navigation, question,
				newTitle, newDescription, "edited question woots!", "java");

		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();

		List<String> messagesList = messagesList(editedQuestion);
		assertTrue(messagesList.contains(message("status.pending")));
	}

	@Test
	public void should_edit_and_automatically_approve_author_edit() throws Exception {
		User karmaNigga = karmaNigga();
		Question question = createQuestionWithDao(karmaNigga,
				"Title title title title title title title",
				"Description description description description description", tag("java"));

		String newTitle = "NEW title title title title title title title";
		String newDescription = "NEW description description description description description";
		UserFlow navigation = login(navigate(), karmaNigga.getEmail());
		navigation = editQuestionWithFlow(navigation, question,
				newTitle, newDescription, "edited question woots!", "java");

		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();

		List<String> messagesList = messagesList(editedQuestion);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		Question fetchQuestion = editedQuestion.getObject("question");
		assertEquals(newTitle, fetchQuestion.getTitle());
		assertEquals(newDescription, fetchQuestion.getDescription());
	}

	@Test
	public void should_edit_and_automatically_approve_moderator() throws Exception {
		Question question = createQuestionWithDao(karmaNigga(),
				"Title title title title title title title",
				"Description description description description description", tag("java"));

		String newTitle = "NEW title title title title title title title";
		String newDescription = "NEW description description description description description";
		UserFlow navigation = login(navigate(), moderator().getEmail());
		navigation = editQuestionWithFlow(navigation, question,
				newTitle, newDescription, "edited question woots!", "java");

		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();

		List<String> messagesList = messagesList(editedQuestion);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		Question fetchQuestion = editedQuestion.getObject("question");
		assertEquals(newTitle, fetchQuestion.getTitle());
		assertEquals(newDescription, fetchQuestion.getDescription());
	}

	@Test
	public void should_touch_question() throws Exception {
		User user = new DaoManager().randomUser();

		Question question = createQuestionWithDao(user(user.getEmail()),
				"Question question question question question",
				"Description description description description description", tag("java"));

		UserFlow navigation = login(navigate(), user.getEmail());
		navigation = editQuestionWithFlow(navigation, question,
				"ASdoA sodi aosido iasod iOASIDoIASOdi",
				"asd oiasudo iausdoi uasoid uaosiduasoduoasi udaiosud oiasud oiasud oisa",
				"so diaos diaosi d", "java");
		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();

		Elements questionElements = getElementsByClass(editedQuestion.getResponseBody(),
				"edited-touch");
		Elements touchImage = getElementsByTag(questionElements.html(), "img");
		assertTrue(touchImage.isEmpty());

		navigation = login(navigate(), moderator().getEmail());
		navigation = editQuestionWithFlow(navigation, question,
				"Question question question question question",
				"Description description description description description",
				"new comment", "java");
		editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();

		questionElements = getElementsByClass(editedQuestion.getResponseBody(),
				"edited-touch");
		touchImage = getElementsByTag(questionElements.html(), "img");
		assertFalse(touchImage.isEmpty());
	}

}
