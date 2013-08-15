package br.com.caelum.brutal.infra;

import static br.com.caelum.brutal.infra.JobsConfigurationController.CONFIG_PATH;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor4.ioc.ApplicationScoped;

@ApplicationScoped
public class JobsScheduler {

	private static final Logger LOG = Logger.getLogger(JobsScheduler.class);
	@Inject private Environment env;

	@PostConstruct
	public void makeRequest(){
		String url = (env.get("host") + CONFIG_PATH);
		LOG.info("Verifying if should schedule jobs on startup");
		if ("true".equals(env.get("schedule_jobs_on_startup"))) {
			LOG.info("Starting thread to schedule jobs");
			new Thread(new StartQuartz(url)).start();
		}
	}

	class StartQuartz implements Runnable {
		private final String url;

		public StartQuartz(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			try {
				waitForServerStartup();
				LOG.info("Invoking quartz configurator at " + url);
				HttpClient http = new HttpClient();
				int status = http.executeMethod(new GetMethod(url));
				if (status != 200) {
					throw new RuntimeException("could not configure quartz, "
							+ url + " answered with " + status);
				}
			} catch (Exception e) {
				LOG.error("Could not start quartz!", e);
			}
		}

		private void waitForServerStartup() throws InterruptedException {
			Thread.sleep(20000);
		}
	}
}