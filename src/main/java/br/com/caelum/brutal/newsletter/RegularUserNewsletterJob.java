package br.com.caelum.brutal.newsletter;

import static org.joda.time.format.DateTimeFormat.forPattern;

import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.NewsletterSentLogDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class RegularUserNewsletterJob implements CronTask {
	
	private final Result result;
	private final UserDAO users;
	private final NewsletterMailer newsMailer;
	private final Environment env;
	private static final Logger LOG = Logger.getLogger(RegularUserNewsletterJob.class);
	public static final DateTime DAY_OF_WEEK_TO_SEND = new DateTime().withDayOfWeek(2);
	private final NewsletterSentLogDAO newsletterSentLogs;
	
	public RegularUserNewsletterJob(Result result, UserDAO users,
			NewsletterMailer newsMailer, Environment env,
			NewsletterSentLogDAO newsletterSentLogs) {
		this.result = result;
		this.users = users;
		this.newsMailer = newsMailer;
		this.env = env;
		this.newsletterSentLogs = newsletterSentLogs;
	}

	@Override
	public void execute() {
		LOG.info("executing " + getClass().getSimpleName());
		if (shouldSendNewsletter()) {
			LOG.info("sending newsletter emails");
			ScrollableResults results = users.newsletterConfirmed();
			newsMailer.sendTo(results);
			newsletterSentLogs.saveLog();
			result.notFound();
		}
	}

	private boolean shouldSendNewsletter() {
		return "true".equals(env.get("newsletter.settings.active")) && !newsletterSentLogs.wasSentThisWeek();
	}

	@Override
	public String frequency() {
		return "0 30 10 ? * "+ getTextDay(DAY_OF_WEEK_TO_SEND); //runs weekly at 10:30
	}

	private String getTextDay(DateTime dayOfWeekToSend) {
		return dayOfWeekToSend.toString(forPattern("E")); //returns MON for dayOfWeek 01, TUE for dayOfWeek 02, etc
	}

}
