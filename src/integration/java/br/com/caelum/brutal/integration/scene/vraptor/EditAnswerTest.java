package br.com.caelum.brutal.integration.scene.vraptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerAndVotes;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class EditAnswerTest extends CustomVRaptorIntegration {

	@Test
	public void should_edit_answer_of_other_author() throws Exception {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		Answer answer = answerQuestionWithDao(moderator(), question,
				"Resposta da questao do teste de edicao", false);

		String newDescription = "my new description of the first answer";

		UserFlow navigation = login(navigate(), "karma.nigga@caelum.com.br",
				"123456");
		navigation = editAnswerWithFlow(navigation, answer, newDescription,
				"comment");

		VRaptorTestResult answerEdited = navigation.followRedirect().execute();
		answerEdited.wasStatus(200).isValid();

		List<String> messagesList = messagesList(answerEdited);
		assertTrue(messagesList.contains(message("status.pending")));
	}

	@Test
	public void should_edit_and_automatically_approve_author_edit()
			throws Exception {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		Answer answer = answerQuestionWithDao(karmaNigga(), question,
				"Resposta da questao do teste de edicao", false);

		String newDescription = "my new description of the first answer";

		UserFlow navigation = login(navigate(), "karma.nigga@caelum.com.br",
				"123456");
		navigation = editAnswerWithFlow(navigation, answer, newDescription,
				"comment");

		VRaptorTestResult answerEdited = navigation.followRedirect().execute();
		answerEdited.wasStatus(200).isValid();

		List<String> messagesList = messagesList(answerEdited);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		AnswerAndVotes answerAndVotes = answerEdited.getObject("answers");
		List<Answer> answers = new ArrayList<Answer>(answerAndVotes.getVotes().keySet());
		assertEquals(newDescription, answers.get(0).getDescription());
	}

	@Test
    public void should_edit_and_automatically_approve_moderator() throws Exception {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		Answer answer = answerQuestionWithDao(karmaNigga(), question,
				"Resposta da questao do teste de edicao", false);

		String newDescription = "my new description of the first answer";

		UserFlow navigation = login(navigate(), "moderator@caelum.com.br",
				"123456");
		navigation = editAnswerWithFlow(navigation, answer, newDescription,
				"comment");

		VRaptorTestResult answerEdited = navigation.followRedirect().execute();
		answerEdited.wasStatus(200).isValid();

		List<String> messagesList = messagesList(answerEdited);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		AnswerAndVotes answerAndVotes = answerEdited.getObject("answers");
		List<Answer> answers = new ArrayList<Answer>(answerAndVotes.getVotes().keySet());
		assertEquals(newDescription, answers.get(0).getDescription());
    }

}
