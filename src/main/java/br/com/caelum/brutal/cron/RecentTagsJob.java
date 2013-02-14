package br.com.caelum.brutal.cron;

import org.hibernate.Session;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class RecentTagsJob implements CronTask{
	
	private RecentTagsContainer recentTagsContainer;
    private final Session session;

	public RecentTagsJob(RecentTagsContainer recentTagsContainer, Session session) {
		this.recentTagsContainer = recentTagsContainer;
        this.session = session;
	}

	@Override
	public void execute() {
		recentTagsContainer.update(session);
	}

	@Override
	public String frequency() {
		return "0/30 * * * * ?";
	}

}
