package org.mamute.util;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.EnvironmentType;
import br.com.caelum.vraptor.hibernate.ServiceRegistryCreator;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.mamute.providers.CustomConfigurationCreator;

import java.io.IOException;

import static br.com.caelum.vraptor.environment.ServletBasedEnvironment.ENVIRONMENT_PROPERTY;
import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class ScriptSessionProvider {

	private static final Logger LOG = Logger.getLogger(ScriptSessionProvider.class);
	private static SessionFactory factory;
	public static final String DATAIMPORT_ENV = "DATAIMPORT_ENV";

	public Session session;
	private static Configuration cfg;

	public ScriptSessionProvider() {
		this.init();
	}

	private void init(){
		Environment env = buildEnv();

		cfg = new CustomConfigurationCreator(env).getInstance();
		ServiceRegistry serviceRegistry = new ServiceRegistryCreator(cfg).getInstance();
		factory = cfg.buildSessionFactory(serviceRegistry);
	}

	public Session getInstance() {
		return factory.openSession();
	}

	private static Environment buildEnv() {
		Environment env;
		try {
			String envName = System.getenv(DATAIMPORT_ENV);
			if (isNullOrEmpty(envName)) {
				envName = firstNonNull(System.getProperty(ENVIRONMENT_PROPERTY), "development");
			}
			env = new DefaultEnvironment(new EnvironmentType(envName));
			LOG.info("using env '" + envName + "' for script session creator");
			return env;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void dropAndCreate() {
		destroy(factory);
		new SchemaExport(cfg).drop(true, true);
		new SchemaExport(cfg).create(true, true);
		init();
	}

	private static void destroy(SessionFactory factory) {
		if (!factory.isClosed()) {
			factory.close();
		}
	}
}
