package org.mamute.vraptor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import br.com.caelum.vraptor.environment.Environment;

@ApplicationScoped
@Named("brutalEnv")
public class Env {

	@Inject private Environment env;
	@Inject private ServletContext context;

	public Env in(String name, Runnable toExecute) {
		if (env.getName().equals(name)) {
			toExecute.run();
		}
		return this;
	}

	public String host() {
		return env.get("host");
	}

	public String s3Host() {
		return env.get("s3.host");
	}

	public String getHostAndContext() {
		return host() + context.getContextPath();
	}
}