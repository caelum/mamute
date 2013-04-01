package br.com.caelum.brutal.infra;

import org.quartz.SchedulerException;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.quartzjob.QuartzController;

@Resource
public class JobsConfigurationController {
	
	private final QuartzController controller;

	public JobsConfigurationController(QuartzController controller) {
		this.controller = controller;
	}
	
	@Get("/configureJobs")
	public void configure() throws SchedulerException {
		controller.config();
	}

}
