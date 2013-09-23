package br.com.caelum.brutal.infra.rss.converter;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.com.caelum.brutal.infra.rss.read.FeedsMap;
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
		feedsMap.putOrUpdate(JOBS_BASE_KEY);
		LOG.debug(feedsMap.get(JOBS_BASE_KEY));
		result.nothing();
	}

	@Override
	public String frequency() {
		return "0 0/15 * * * ?";//fire every hour 
	}
}
