package br.com.caelum.brutal.newsletter;

import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.joda.time.DateTime;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.notification.NotificationMailer;
import br.com.caelum.brutal.notification.NotificationMailer.LinkToHelper;
import br.com.caelum.brutal.vraptor.Linker;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;

@Component
public class NewsletterMailer {
	
	private QuestionDAO questions;
	private Mailer mailer;
	private TemplateMailer templates;
	private Linker linker;
	private static final PolicyFactory POLICY = new HtmlPolicyBuilder().toFactory();
	private static final Logger LOG = Logger.getLogger(ModeratorsNewsletterJob.class);

	public NewsletterMailer(QuestionDAO questions, Result result, 
			Mailer mailer, TemplateMailer templates, 
			UserDAO users, Linker linker) {
		this.questions = questions;
		this.mailer = mailer;
		this.templates = templates;
		this.linker = linker;
	}

	public void sendTo(ScrollableResults results) {
		DateTime pastWeek = new DateTime().minusWeeks(1);
		DateTime twelveHoursAgo = new DateTime().minusHours(12);
		
		List<Question> hotQuestions = questions.hot(pastWeek, 5);
		List<Question> unanswered = questions.randomUnanswered(pastWeek, twelveHoursAgo, 5);
		LinkToHelper linkToHelper = new NotificationMailer.LinkToHelper(linker);
		
		while (results.next()) {
			User user = (User) results.get()[0];
			try {
				Email email = templates.template("newsletter_mail")
						.with("hotQuestions", hotQuestions)
						.with("unansweredQuestions", unanswered)
						.with("unansweredQuestions", unanswered)
						.with("unsubscribeLink", linkToHelper.unsubscribeLink(user))
						.with("linkToHelper", linkToHelper)
						.with("sanitizer", POLICY)
						.to(user.getName(), user.getEmail());
				mailer.send(email);
			} catch (Exception e) {
				LOG.error("could not send email", e);
			}
		}

	}

}
