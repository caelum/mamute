package org.mamute.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.quartzjob.CronTask;
import org.mamute.search.IndexSyncJob;

import javax.inject.Inject;

@Controller
public class IndexSyncController implements CronTask {
	public static final String DEFAULT_SYNC = "0 0 0/1 1/1 * ? *";

	@Inject IndexSyncJob job;
	@Inject Result result;

	@Post("/sdfajsdfjaoiji")
	@Override
	public void execute() {
		job.execute();
		result.nothing();
	}

	@Override
	public String frequency() {
		return DEFAULT_SYNC;
	}
}
