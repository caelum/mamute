package org.mamute.mail;

import br.com.caelum.vraptor.environment.Property;
import br.com.caelum.vraptor.simplemail.AsyncMailer;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.simplemail.template.TemplateMail;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import org.apache.commons.mail.Email;
import org.mamute.controllers.QuestionController;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.vraptor.Linker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class NewQuestionMailer {
	private static final Logger LOG = LoggerFactory.getLogger(NewQuestionMailer.class);
	private AsyncMailer mailer;
	private TemplateMailer templates;
	private BundleFormatter bundle;
	private String emailLogo;
	private Linker linker;

	@Deprecated
	NewQuestionMailer() {
	}

	@Inject
	public NewQuestionMailer(AsyncMailer mailer, TemplateMailer templates,
							 BundleFormatter bundle, Linker linker,
							 @Property("mail_logo_url") String emailLogo) {
		this.mailer = mailer;
		this.templates = templates;
		this.bundle = bundle;
		this.linker = linker;
		this.emailLogo = emailLogo;
	}

	public void send(List<User> subscribed, Question question)
	{
		linker.linkTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
		String questionLink = linker.get();
		TemplateMail template = templates.template("new_question_notification")
				.with("question", question)
				.with("bundle", bundle)
				.with("questionLink", questionLink)
				.with("logoUrl", emailLogo);
		for (User user : subscribed)
		{
			boolean notSameAuthor = !user.equals(question.getAuthor());
			if (notSameAuthor)
			{
				Email email = template.to(user.getName(), user.getEmail());
				try
				{
					mailer.asyncSend(email);
				}
				catch (Exception e)
				{
					LOG.error("unable to send email", e);
					// do not rethrow as this is non critical
				}
			}
		}
	}
}
