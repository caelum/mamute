package br.com.caelum.brutal.cron;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class RecentTagsJob implements CronTask {
	
	private final RecentTagsContainer recentTagsContainer;
    private final Session session;
	private final Result result;
	private final static Logger LOG = Logger.getLogger(Logger.class);

	public RecentTagsJob(RecentTagsContainer recentTagsContainer, Session session, Result result) {
		this.recentTagsContainer = recentTagsContainer;
        this.session = session;
		this.result = result;
	}

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
