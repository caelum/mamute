package br.com.caelum.brutal.infra.rss.converter;

import org.apache.log4j.Logger;

import br.com.caelum.brutal.infra.rss.read.FeedsMap;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class OndeTrabalharRSSJob implements CronTask{
	private static final String JOBS_BASE_KEY = "jobs";
	private final Result result;
	private static final Logger LOG = Logger.getLogger(OndeTrabalharRSSJob.class);
	private final FeedsMap feedsMap;
	private final Environment env;

	public OndeTrabalharRSSJob(Result result, FeedsMap feedsMap, Environment env) {
		this.result = result;
		this.feedsMap = feedsMap;
		this.env = env;
	}

	@Override
	@Path("/asjkfnaowo21jkhwe12341")
	public void execute() {
		LOG.info("executing " + getClass().getSimpleName());
		feedsMap.putOrUpdate(JOBS_BASE_KEY);
		LOG.info(feedsMap.get(JOBS_BASE_KEY));
		result.notFound();
	}

	@Override
	public String frequency() {
		return "*/59 * * ? * *";//every x seconds, defined at environment.properties 
	}
}
