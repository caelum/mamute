package br.com.caelum.brutal.newsletter;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.mail.Email;
import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.joda.time.DateTime;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import br.com.caelum.brutal.dao.NewsDAO;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.notification.NotificationMailer;
import br.com.caelum.brutal.notification.NotificationMailer.LinkToHelper;
import br.com.caelum.brutal.util.BrutalDateFormat;
import br.com.caelum.brutal.vraptor.Env;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import br.com.caelum.vraptor4.core.Localization;
import br.com.caelum.vraptor4.http.route.Router;

public class NewsletterMailer {
	
	private static final PolicyFactory POLICY = new HtmlPolicyBuilder().toFactory();
	private static final Logger LOG = Logger.getLogger(ModeratorsNewsletterJob.class);
	@Inject private QuestionDAO questions;
	@Inject private Mailer mailer;
	@Inject private TemplateMailer templates;
	@Inject private Router router;
	@Inject private Localization localization;
	@Inject private NewsDAO news;
	@Inject private BrutalDateFormat brutalDateFormat;
	@Inject private Env brutalEnv;

	public void sendTo(ScrollableResults results) {
		DateTime pastWeek = new DateTime().minusWeeks(1);
		DateTime twelveHoursAgo = new DateTime().minusHours(12);
		List<News> hotNews = news.hotNews();
		List<Question> hotQuestions = questions.hot(pastWeek, 8);
		List<Question> unanswered = questions.randomUnanswered(pastWeek, twelveHoursAgo, 8);
		LinkToHelper linkToHelper = new NotificationMailer.LinkToHelper(router, brutalEnv);
		String siteName = localization.getMessage("site.name");
		String date = brutalDateFormat.getInstance("date.joda.newsletter.pattern").print(new DateTime());
		
		while (results.next()) {
			User user = (User) results.get()[0];
			try {
				Email email = templates.template("newsletter_mail", date, siteName)
						.with("hotNews", hotNews)
						.with("hotQuestions", hotQuestions)
						.with("unansweredQuestions", unanswered)
						.with("unsubscribeLink", linkToHelper.unsubscribeLink(user))
						.with("linkToHelper", linkToHelper)
						.with("l10n", localization)
						.with("sanitizer", POLICY)
						.with("siteName", siteName)
						.with("date", date)
						.to(user.getName(), user.getEmail());
				email.setCharset("utf-8");
				mailer.send(email);
			} catch (Exception e) {
				LOG.error("could not send email", e);
			}
		}

	}

}
