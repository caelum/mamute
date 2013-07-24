package br.com.caelum.brutal.newsletter;

import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class NormalUserNewsletterJob implements CronTask {
	
	private final Result result;
	private final UserDAO users;
	private final NewsletterMailer newsMailer;
	private final Environment env;
	private static final Logger LOG = Logger.getLogger(NormalUserNewsletterJob.class);
	
	public NormalUserNewsletterJob(Result result, UserDAO users,
			NewsletterMailer newsMailer, Environment env) {
		this.result = result;
		this.users = users;
		this.newsMailer = newsMailer;
		this.env = env;
	}

	@Override
	public void execute() {
		LOG.info("executing " + getClass().getSimpleName());
		if ("true".equals(env.get("newsletter.settings.active"))) {
			LOG.info("sending newsletter emails");
			ScrollableResults results = users.newsletterConfirmed();
			newsMailer.sendTo(results);
			result.notFound();
		}
	}

	@Override
	public String frequency() {
		return "0 30 10 ? * TUE"; //runs weekly at 10:30
	}

}
