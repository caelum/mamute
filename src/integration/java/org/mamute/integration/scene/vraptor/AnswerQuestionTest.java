package org.mamute.integration.scene.vraptor;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.mamute.model.Answer;
import org.mamute.model.AnswerAndVotes;
import org.mamute.model.Question;
import org.mamute.model.User;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class AnswerQuestionTest extends CustomVRaptorIntegration {

	@Test
	public void should_answer_when_logged_in() {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		String description = "Resposta da questao do teste de edicao";

		UserFlow navigation = login(navigate(), karmaNigga().getEmail());
		navigation = answerQuestionWithFlow(navigation, question, description, false);

		VRaptorTestResult questionAnswered = navigation.followRedirect().execute();
		questionAnswered.wasStatus(200).isValid();

		AnswerAndVotes answerAndVotes = questionAnswered.getObject("answers");
		List<Answer> answers = new ArrayList<Answer>(answerAndVotes.getVotes().keySet());
		Assert.assertEquals(description, answers.get(0).getDescription());
	}

	@Test
	public void should_not_display_answer_form_when_not_logged_in() {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		UserFlow navigation = goToQuestionPage(navigate(), question);

		VRaptorTestResult questionPage = navigation.followRedirect().execute();
		questionPage.wasStatus(200).isValid();

		Elements answerForm = getElementsByClass(questionPage.getResponseBody(), "answer-form");
		assertTrue(answerForm.isEmpty());
	}

	@Test
	public void should_not_display_answer_form_when_already_answered() {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		User karmaNigga = karmaNigga();
		answerQuestionWithDao(karmaNigga, question,
				"Resposta da questao do teste de edicao", false);

		UserFlow navigation = login(navigate(), karmaNigga.getEmail());
		navigation = goToQuestionPage(navigation, question);

		VRaptorTestResult questionPage = navigation.followRedirect().execute();
		questionPage.wasStatus(200).isValid();

		Elements answerForm = getElementsByClass(questionPage.getResponseBody(), "answer-form");
		assertTrue(answerForm.isEmpty());
	}

}
