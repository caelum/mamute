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
public class InfoQRSSJob implements CronTask{
	private static final String INFOQ_BASE_KEY = "infoq";
	private static final Logger LOG = Logger.getLogger(InfoQRSSJob.class);
	@Inject private Result result;
	@Inject private FeedsMap feedsMap;


	@Override
	@Path("/jdnakfh3nfis39103f1")
	public void execute() {
		LOG.debug("executing " + getClass().getSimpleName());
		feedsMap.putOrUpdate(INFOQ_BASE_KEY, RSSType.INFO_Q);
		LOG.debug(feedsMap.get(INFOQ_BASE_KEY));
		result.nothing();
	}

	@Override
	public String frequency() {
		return "0 */15 * * * ?";//fire every 15 min
	}
}
