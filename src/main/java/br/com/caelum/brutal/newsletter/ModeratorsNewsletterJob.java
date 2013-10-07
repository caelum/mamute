package br.com.caelum.brutal.newsletter;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.ScrollableResults;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Controller
public class ModeratorsNewsletterJob implements CronTask {

	private static final Logger LOG = Logger.getLogger(ModeratorsNewsletterJob.class);
	@Inject private Result result;
	@Inject private UserDAO users;
	@Inject private NewsletterMailer newsMailer;
	@Inject private Environment env;
	
	@Override
	public void execute() {
		LOG.info("executing " + getClass().getSimpleName());
		if ("true".equals(env.get("newsletter.settings.active"))) {
			LOG.info("sending newsletter emails");
			ScrollableResults results = users.moderators();
			newsMailer.sendTo(results, true);
			result.notFound();
		}
	}

	@Override
	public String frequency() {
		return "0 30 10 ? * MON"; //runs weekly at 10:30
	}

}
