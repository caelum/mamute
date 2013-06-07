package br.com.caelum.brutal.cron;

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
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.quartzjob.CronTask;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;

@Resource
public class NewsletterJob implements CronTask {
	
	private final QuestionDAO questions;
	private final Result result;
	private final TemplateMailer templates;
	private final Mailer mailer;
	private final UserDAO users;
	private static final Logger LOG = Logger.getLogger(NewsletterJob.class);
	private final Linker linker;
	private static final PolicyFactory POLICY = new HtmlPolicyBuilder().toFactory();

	public NewsletterJob(QuestionDAO questions, Result result, 
			Mailer mailer, TemplateMailer templates, UserDAO users, Linker linker) {
		this.questions = questions;
		this.result = result;
		this.mailer = mailer;
		this.templates = templates;
		this.users = users;
		this.linker = linker;
	}

	@Override
	public void execute() {
		DateTime pastWeek = new DateTime().minusWeeks(1);
		List<Question> hotQuestions = questions.hot(pastWeek, 5);
		DateTime twelveHoursAgo = new DateTime().minusHours(12);
		List<Question> unanswered = questions.randomUnanswered(twelveHoursAgo, 5);
		LinkToHelper linkToHelper = new NotificationMailer.LinkToHelper(linker);
		
		ScrollableResults results = users.list();
		while (results.next()) {
			User user = (User) results.get()[0];
			try {
				Email email = templates.template("newsletter_mail")
						.with("hotQuestions", hotQuestions)
						.with("unansweredQuestions", unanswered)
						.with("unansweredQuestions", unanswered)
						.with("linkToHelper", linkToHelper)
						.with("sanitizer", POLICY)
						.to(user.getName(), user.getEmail());
				mailer.send(email);
				return; //TODO
			} catch (Exception e) {
				LOG.error("could not send email", e);
			}
		}
		
		result.notFound();
	}

	@Override
	public String frequency() {
		return "0 14 * * 0";
	}

}
