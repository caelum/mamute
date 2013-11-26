package br.com.caelum.brutal.integration.scene.vraptor;

import static br.com.caelum.vraptor.test.http.Parameters.initWith;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;
import br.com.caelum.vraptor.validator.I18nMessage;

public class EditAnswerTest extends CustomVRaptorIntegration {

	@Test
	public void should_edit_answer_of_other_author() throws Exception {
		LoggedUser author = new LoggedUser(moderator(), null);
		QuestionInformation questionInformation = new QuestionInformation("Titulo da questao hahaha", "Descricao da questao longa demais", 
				author, Arrays.asList(tag("java")), "new question");
		Question question = new Question(questionInformation, moderator());
		questionDao().save(question);
		commit();
		
		UserFlow navigation = login(navigate(), "karma.nigga@caelum.com.br", "123456")
				.post("/pergunta/editar/" + question.getId(), 
					initWith("title", "new edited title")
					.add("description", "new edited description new edited description")
					.add("comment", "edit comment")
					.add("tagNames", "java"));
		VRaptorTestResult questionEdited = navigation.followRedirect().execute();
		
		questionEdited.wasStatus(200).isValid();
		
		List<I18nMessage> confirmationMessages = questionEdited.getObject("messages");
		Assert.assertEquals(1, confirmationMessages.size());
	}

}
