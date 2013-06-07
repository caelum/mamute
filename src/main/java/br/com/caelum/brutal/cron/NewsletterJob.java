package br.com.caelum.brutal.cron;

import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.quartzjob.CronTask;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import br.com.caelum.vraptor.view.Results;

@Resource
public class NewsletterJob implements CronTask {
	
	private final QuestionDAO questions;
	private final Result result;
	private final TemplateMailer templates;
	private final Mailer mailer;
	private final UserDAO users;
	private static final Logger LOG = Logger.getLogger(NewsletterJob.class);

	public NewsletterJob(QuestionDAO questions, Result result, 
			Mailer mailer, TemplateMailer templates, UserDAO users) {
		this.questions = questions;
		this.result = result;
		this.mailer = mailer;
		this.templates = templates;
		this.users = users;
	}

	@Override
	public void execute() {
		DateTime pastWeek = new DateTime().minusWeeks(1);
		List<Question> hotQuestions = questions.hot(pastWeek, 5);
		DateTime twelveHoursAgo = new DateTime().minusHours(12);
		List<Question> unanswered = questions.randomUnanswered(twelveHoursAgo, 5);
		
		ScrollableResults results = users.list();
		while (results.next()) {
			User user = (User) results.get()[0];
			Email email = templates.template("newsletter_mail")
					.with("hotQuestions", hotQuestions)
					.with("unansweredQuestions", unanswered)
					.to(user.getName(), user.getEmail());
			try {
				mailer.send(email);
			} catch (EmailException e) {
				LOG.error("could not send email", e);
			}
		}
		
		result.use(Results.http()).body("" + hotQuestions + unanswered);
	}

	@Override
	public String frequency() {
		return "0 14 * * 0";
	}

}
