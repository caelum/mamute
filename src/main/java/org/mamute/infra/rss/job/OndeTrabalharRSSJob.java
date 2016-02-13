package org.mamute.infra.rss.job;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.mamute.infra.rss.RSSType;
import org.mamute.infra.rss.read.FeedsMap;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Controller
public class OndeTrabalharRSSJob implements CronTask{
	private static final String JOBS_BASE_KEY = "jobs";
	private static final Logger LOG = Logger.getLogger(OndeTrabalharRSSJob.class);
	@Inject private Result result;
	@Inject private FeedsMap feedsMap;


	@Override
	@Path("/asjkfnaowo21jkhwe12341")
	public void execute() {
		LOG.debug("executing " + getClass().getSimpleName());
		feedsMap.putOrUpdate(JOBS_BASE_KEY, RSSType.ONDE_TRABALHAR);
		LOG.debug(feedsMap.get(JOBS_BASE_KEY));
		result.nothing();
	}

	@Override
	public String frequency() {
		return "0 */15 * * * ?";//fire every 15 min
	}
}
