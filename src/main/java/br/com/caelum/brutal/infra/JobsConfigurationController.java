package br.com.caelum.brutal.infra;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import br.com.caelum.vraptor.quartzjob.QuartzController;
import br.com.caelum.vraptor4.Get;

@Resource
public class JobsConfigurationController {
	static final String CONFIG_PATH = "/configureJobs";
	private static final Logger LOG = Logger.getLogger(JobsConfigurationController.class);
	
	private final QuartzController controller;
	private final Result result;

	public JobsConfigurationController(QuartzController controller, Result result) {
		this.controller = controller;
		this.result = result;
	}

	@Get(CONFIG_PATH)
	public void configure() throws SchedulerException {
		LOG.debug("configuring cron jobs");
		controller.config();
		result.nothing();
	}

}
