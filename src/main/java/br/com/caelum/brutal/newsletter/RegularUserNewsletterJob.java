package br.com.caelum.brutal.newsletter;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

import br.com.caelum.brutal.dao.NewsletterSentLogDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Controller
public class RegularUserNewsletterJob implements CronTask {
	
	private static final Logger LOG = Logger.getLogger(RegularUserNewsletterJob.class);
	private Result result;
	private UserDAO users;
	private NewsletterMailer newsMailer;
	private Environment env;
	private NewsletterSentLogDAO newsletterSentLogs;

	@Deprecated
	public RegularUserNewsletterJob() {
	}
	
	@Inject
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
	@Path("/wjh1jkh34jk12hkjehd13kj4h1kjh41jkhwe12341")
	public void execute() {
		LOG.info("executing " + getClass().getSimpleName());
		if (shouldSendNewsletter()) {
			LOG.info("sending newsletter emails");
			ScrollableResults results = users.newsletterConfirmed();
			newsMailer.sendTo(results, false);
			newsletterSentLogs.saveLog();
			result.notFound();
		}
	}

	private boolean shouldSendNewsletter() {
		return "true".equals(env.get("newsletter.settings.active")) && !newsletterSentLogs.wasSentThisWeek();
	}

	@Override
	public String frequency() {
		return "0 30 10 ? * TUE"; //runs weekly at 10:30
	}


}
