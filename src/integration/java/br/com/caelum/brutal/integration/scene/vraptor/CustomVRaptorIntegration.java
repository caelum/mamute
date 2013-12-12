package br.com.caelum.brutal.integration.scene.vraptor;

import static br.com.caelum.vraptor.test.http.Parameters.initWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.InvisibleForUsersRule;
import br.com.caelum.brutal.dao.LoginMethodDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.integration.util.AppMessages;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.Post;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.util.DataImport;
import br.com.caelum.brutal.util.ScriptSessionCreator;
import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.test.VRaptorIntegration;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;
import br.com.caelum.vraptor.validator.I18nMessage;

public class CustomVRaptorIntegration extends VRaptorIntegration {

	protected final String DEFAULT_PASSWORD = "123456";
	private static boolean runDataImport = true;
	private Session session;

	private AppMessages messages = new AppMessages();
	private Random randomizer = new Random();

	{
		System.setProperty(ServletBasedEnvironment.ENVIRONMENT_PROPERTY, "acceptance");
		if (runDataImport) {
			try {
				new DataImport().run();
			} catch (IOException e) {
				e.printStackTrace();
			}
			runDataImport = false;
		}
		ScriptSessionCreator sessionFactoryCreator = new ScriptSessionCreator();
		session = sessionFactoryCreator.getSession();
		session.beginTransaction();
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

	protected List<String> errorsList(VRaptorTestResult result) {
		List<I18nMessage> errorMessages = result.getObject("errors");
		List<String> messages = new ArrayList<>();
		for (I18nMessage message : errorMessages) {
			messages.add(message.getMessage());
		}
		return messages;
	}

	protected Elements getElementsByClass(String html, String cssClass) {
		Document document = Jsoup.parse(html);
		return document.getElementsByClass(cssClass);
	}

	protected Elements getElementsByTag(String html, String tagName) {
		Document document = Jsoup.parse(html);
		return document.getElementsByTag(tagName);
	}

	/*** USER FLOW LOGIC ***/
	protected UserFlow logout(UserFlow navigation) {
		return navigation.post("/logout");
	}

	protected UserFlow login(UserFlow navigation, String email) {
		return navigation.post("/login",
				initWith("email", email).add("password", DEFAULT_PASSWORD));
	}

	protected UserFlow createQuestionWithFlow(UserFlow navigation,
			String title, String description, String tagNames, boolean watching) {
		return navigation.post("/perguntar",
				initWith("title", title)
					.add("description", description)
					.add("tagNames", tagNames)
					.add("watching", watching));
	}

	protected UserFlow editQuestionWithFlow(UserFlow navigation,
			Question question, String title, String description, String comment,
			String tags) {
		String url = String.format("/pergunta/editar/%s", question.getId());
		return navigation.post(url,
				initWith("original", question)
					.add("title", title)
					.add("description", description)
					.add("comment", comment)
					.add("tagNames", tags));
	}
	
	protected UserFlow answerQuestionWithFlow(UserFlow navigation, Question question,
			String description, boolean watching) {
		String url = String.format("/responder/%s", question.getId());
		return navigation.post(url,
				initWith("question", question)
					.add("description", description)
					.add("watching", watching));
	}

	protected UserFlow editAnswerWithFlow(UserFlow navigation, Answer answer,
			String description, String comment) {
		String url = String.format("/resposta/editar/%s", answer.getId());
		return navigation.post(url,
				initWith("original", answer)
					.add("description", description)
					.add("comment", comment));
	}

	protected UserFlow commentWithFlow(UserFlow navigation, Post post, String comment,
			boolean watching) {
		String onWhat = (post instanceof Question ? "pergunta" : "resposta");
		String url = String.format("/%s/%s/comentar", onWhat, post.getId());
		return navigation.post(url,
				initWith("id", post.getId())
					.add("onWhat", onWhat)
					.add("comment", comment)
					.add("watching", watching));
	}

	protected UserFlow goToQuestionPage(UserFlow navigation, Question question) {
		String url = String.format("/%s-mock", question.getId());
		return navigation.get(url,
				initWith("question", question)
					.add("sluggedTitle", question.getSluggedTitle()));
	}

	/*** DAO LOGIC ***/

	protected QuestionDAO questionDao() {
		InvisibleForUsersRule invisible = new InvisibleForUsersRule(new LoggedUser(null, null));
		return new QuestionDAO(session, invisible);
	}

	protected AnswerDAO answerDao() {
		InvisibleForUsersRule invisible = new InvisibleForUsersRule(new LoggedUser(null, null));
		return new AnswerDAO(session, invisible);
	}

	protected void commit() {
		session.getTransaction().commit();
		session.beginTransaction();
	}

	protected UserDAO userDao() {
		return new UserDAO(session);
	}

	protected Tag tag(String name) {
		return new TagDAO(this.session).findByName(name);
	}

	protected User moderator() {
		return userDao().findByMailAndPassword("moderator@caelum.com.br", DEFAULT_PASSWORD);
	}

	protected User karmaNigga() {
		return userDao().findByMailAndPassword("karma.nigga@caelum.com.br",
				DEFAULT_PASSWORD);
	}

	protected User user(String email) {
		return userDao().findByMailAndPassword(email, DEFAULT_PASSWORD);
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