package br.com.caelum.brutal.integration.scene.vraptor;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerAndVotes;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class AnswerQuestionTest extends CustomVRaptorIntegration {

	@Test
	public void should_answer_when_logged_in() {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		String description = "Resposta da questao do teste de edicao";

		UserFlow navigation = login(navigate(), "karma.nigga@caelum.com.br",
				"123456");
		navigation = answerQuestionWithFlow(navigation, question, description, false);

		VRaptorTestResult questionAnswered = navigation.followRedirect().execute();
		questionAnswered.wasStatus(200).isValid();

		AnswerAndVotes answerAndVotes = questionAnswered.getObject("answers");
		List<Answer> answers = new ArrayList<Answer>(answerAndVotes.getVotes().keySet());
		Assert.assertEquals(description, answers.get(0).getDescription());
	}
	
	@Test
	public void should_not_display_answer_form_when_not_logged_in(){
	}
	
	@Test
	public void should_not_display_answer_form_when_already_answered(){
	}
	
}
