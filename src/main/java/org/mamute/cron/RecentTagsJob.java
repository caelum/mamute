package org.mamute.cron;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.mamute.components.RecentTagsContainer;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Controller
public class RecentTagsJob implements CronTask {
	
	@Inject private RecentTagsContainer recentTagsContainer;
    @Inject private Session session;
	@Inject private Result result;
	private static Logger LOG = Logger.getLogger(Logger.class);

	@Override
	public void execute() {
		LOG.info("executing RecentTagsJob...");
		recentTagsContainer.update(session);
		result.nothing();
	}

	@Override
	public String frequency() {
		return "0 0 0/1 * * ?";
	}

}
