package org.mamute.integration.scene.vraptor;

import static br.com.caelum.vraptor.test.http.Parameters.initWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.mamute.integration.util.AppMessages;
import org.mamute.integration.util.DaoManager;
import org.mamute.model.Answer;
import org.mamute.model.Post;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.util.DataImport;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.test.VRaptorIntegration;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;
import br.com.caelum.vraptor.validator.I18nMessage;

public class CustomVRaptorIntegration extends VRaptorIntegration {

	protected final String DEFAULT_PASSWORD = "123456";
	private boolean runDataImport = true;

	private AppMessages messages = new AppMessages();
	private DaoManager daoManager;

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
		daoManager = new DaoManager();
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
		if(errorMessages!= null) {
			for (I18nMessage message : errorMessages) {
				messages.add(message.getMessage());
			}
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

	/*** DAO LOGIC ***/
	protected User moderator() {
		return daoManager.moderator();
	}

	protected User karmaNigga() {
		return daoManager.karmaNigga();
	}

	protected User user(String email) {
		return daoManager.user(email);
	}

	protected User randomUser() {
		return daoManager.randomUser();
	}

	protected Question createQuestionWithDao(User author) {
		return daoManager.createQuestion(author);
	}

	protected Question createQuestionWithDao(User author, String title, String description, Tag... tags) {
		return daoManager.createQuestion(author, title, description, tags);
	}

	protected Answer answerQuestionWithDao (User author, Question question) {
		return daoManager.answerQuestion(author, question);
	}

	protected Answer answerQuestionWithDao (User author, Question question, String description, boolean watching) {
		return daoManager.answerQuestion(author, question, description, watching);
	}

	protected Tag tag (String tagName) {
		return daoManager.tag(tagName);
	}

	/*** USER FLOW LOGIC ***/
	protected UserFlow logout(UserFlow navigation) {
		return navigation.post("/logout");
	}

	protected UserFlow login(UserFlow navigation, String email) {
		return navigation.post("/login",
				initWith("email", email).add("password", DEFAULT_PASSWORD));
	}
	
	protected UserFlow loginWithPassword(UserFlow navigation, String email, String password) {
		return navigation.post("/login",
				initWith("email", email).add("password", password));
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

}