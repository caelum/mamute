package br.com.caelum.brutal.cron;

import org.joda.time.DateTime;

import br.com.caelum.brutal.components.RecentTags;
import br.com.caelum.vraptor.quartzjob.CronTask;

public class RecentTagsJob implements CronTask{
	
	private RecentTags recentTags;

	public RecentTagsJob(RecentTags recentTags) {
		this.recentTags = recentTags;
	}

	@Override
	public void execute() {
		recentTags.updateRecentTags();
	}

	@Override
	public String frequency() {
		return "0 0 0/3 * * ?";
	}

}
