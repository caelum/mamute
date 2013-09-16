package br.com.caelum.brutal.infra;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.quartzjob.QuartzController;

@Controller
public class JobsConfigurationController {
	static final String CONFIG_PATH = "/configureJobs";
	private static final Logger LOG = Logger.getLogger(JobsConfigurationController.class);
	
	@Inject private Result result;
	@Inject private QuartzController controller;


	@Get(CONFIG_PATH)
	public void configure() throws SchedulerException {
		LOG.debug("configuring cron jobs");
		controller.config();
		result.nothing();
	}

}
