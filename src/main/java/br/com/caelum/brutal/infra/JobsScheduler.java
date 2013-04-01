package br.com.caelum.brutal.infra;

import static br.com.caelum.brutal.infra.JobsConfigurationController.CONFIG_PATH;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class JobsScheduler {

	private static final Logger LOG = Logger.getLogger(JobsScheduler.class);
	private final Environment env;

	public JobsScheduler(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void makeRequest() throws HttpException, IOException {
		String url = (env.get("host") + CONFIG_PATH);
		new Thread(new StartQuartz(url)).start();
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
			Thread.sleep(10000);
		}
	}
}