package br.com.caelum.brutal.newsletter;

import org.hibernate.ScrollableResults;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class ModeratorsNewsletterJob implements CronTask {
	
	private final Result result;
	private final UserDAO users;
	private final NewsletterMailer newsMailer;
	
	public ModeratorsNewsletterJob(Result result, UserDAO users,
			NewsletterMailer newsMailer) {
		this.result = result;
		this.users = users;
		this.newsMailer = newsMailer;
	}

	@Override
	public void execute() {
		ScrollableResults results = users.moderators();
		newsMailer.sendTo(results);
		result.notFound();
	}

	@Override
	public String frequency() {
		return "0 30 10 ? * MON"; //runs weekly at 10:30
	}

}
