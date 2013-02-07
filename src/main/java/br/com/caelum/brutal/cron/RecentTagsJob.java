package br.com.caelum.brutal.cron;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class RecentTagsJob implements CronTask{
	
	private RecentTagsContainer recentTagsContainer;

	public RecentTagsJob(RecentTagsContainer recentTagsContainer) {
		this.recentTagsContainer = recentTagsContainer;
	}

	@Override
	public void execute() {
		recentTagsContainer.updateRecentTagsUsage();
	}

	@Override
	public String frequency() {
		return "0 0 0/3 * * ?";
	}

}
