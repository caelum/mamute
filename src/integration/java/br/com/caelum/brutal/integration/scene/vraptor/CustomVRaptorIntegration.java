package br.com.caelum.brutal.integration.scene.vraptor;

import static br.com.caelum.vraptor.test.http.Parameters.initWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.InvisibleForUsersRule;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.integration.util.AppMessages;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.util.DataImport;
import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.test.VRaptorIntegration;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;
import br.com.caelum.vraptor.validator.I18nMessage;

public class CustomVRaptorIntegration extends VRaptorIntegration {

	private DataImport dataImport;
	private AppMessages messages = new AppMessages();

	{
		System.setProperty(ServletBasedEnvironment.ENVIRONMENT_PROPERTY,
				"acceptance");
		dataImport = new DataImport();
		importData();
		dataImport.getSession().beginTransaction();
	}

	private void importData() {
		try {
			dataImport.run();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected String message(String key) {
		return messages.getMessage(key);
	}

	protected List<String> messagesList(VRaptorTestResult result) {
		List<I18nMessage> confirmationMessages = result.getObject("messages");
		List<String> messages = new ArrayList<>();
		for (I18nMessage message : confirmationMessages) {
			messages.add(message.getMessage());
		}
		return messages;
	}

	/*** USER FLOW LOGIC ***/
	protected UserFlow logout(UserFlow navigation) {
		return navigation.post("/logout");
	}

	protected UserFlow login(UserFlow navigation, String email, String password) {
		return navigation.post("/login",
				initWith("email", email).add("password", password));
	}

	protected UserFlow createQuestionWithFlow(UserFlow navigation,
			String title, String description, String tagNames, boolean watching) {
		return navigation.post(
				"/perguntar",
				initWith("title", title).add("description", description)
						.add("tagNames", tagNames).add("watching", watching));
	}

	protected UserFlow editQuestionWithFlow(UserFlow navigation,
			Long questionId, String title, String description, String comment,
			String tags) {
		return navigation.post(
				"/pergunta/editar/" + questionId,
				initWith("title", title).add("description", description)
						.add("comment", comment).add("tagNames", tags));
	}

	protected UserFlow answerQuestionWithFlow(UserFlow navigation,
			Question question, String description, boolean watching) {
		return navigation.post("/responder/" + question.getId(),
				initWith("question", question).add("description", description)
						.add("watching", watching));
	}

	protected UserFlow editAnswerWithFlow(UserFlow navigation, Answer answer,
			String description, String comment) {
		return navigation.post("/resposta/editar/" + answer.getId(),
				initWith("original", answer).add("description", description)
						.add("comment", comment));
	}

	/*** DAO LOGIC ***/
	protected QuestionDAO questionDao() {
		InvisibleForUsersRule invisible = new InvisibleForUsersRule(
				new LoggedUser(null, null));
		return new QuestionDAO(dataImport.getSession(), invisible);
	}

	protected AnswerDAO answerDao() {
		InvisibleForUsersRule invisible = new InvisibleForUsersRule(
				new LoggedUser(null, null));
		return new AnswerDAO(dataImport.getSession(), invisible);
	}

	protected void commit() {
		Session session = this.dataImport.getSession();
		session.getTransaction().commit();
		session.beginTransaction();
	}

	protected UserDAO userDao() {
		return new UserDAO(dataImport.getSession());
	}

	protected Tag tag(String name) {
		return new TagDAO(this.dataImport.getSession()).findByName(name);
	}

	protected User moderator() {
		UserDAO users = new UserDAO(dataImport.getSession());
		return users.findByMailAndPassword("moderator@caelum.com.br", "123456");
	}

	protected User karmaNigga() {
		UserDAO users = new UserDAO(dataImport.getSession());
		return users.findByMailAndPassword("karma.nigga@caelum.com.br",
				"123456");
	}

	protected User user(String email) {
		UserDAO users = new UserDAO(dataImport.getSession());
		return users.findByMailAndPassword(email, "123456");
	}

	protected Question createQuestionWithDao(User author, String title,
			String description, Tag... tags) {
		Question question = new QuestionBuilder().withTitle(title)
				.withDescription(description).withTags(Arrays.asList(tags))
				.withAuthor(author).build();
		questionDao().save(question);
		commit();
		return question;
	}

	protected Answer answerQuestionWithDao(User author, Question question,
			String description, boolean watching) {
		LoggedUser loggedUser = new LoggedUser(author, null);
		AnswerInformation information = new AnswerInformation(description,
				loggedUser, "new answer");
		Answer answer = new Answer(information, question, author);
		answerDao().save(answer);
		commit();
		return answer;
	}

}
