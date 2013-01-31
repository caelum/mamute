package br.com.caelum.pagpag.components;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class EntityManagerFactoryCreator implements
		ComponentFactory<EntityManagerFactory> {

	private static final String DEFAULT_UNIT_NAME = "default";
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityManagerFactoryCreator.class);
	private EntityManagerFactory factory;
	private final Environment env;

	public EntityManagerFactoryCreator(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void create() {
		String unitName = getUnitName();
		factory = Persistence.createEntityManagerFactory(unitName);
	}

	private String getUnitName() {
		String unitName = grabBasicUnitName();
		LOGGER.info("database connection: reading basic unit " + unitName);
		herokuAnalysis(unitName);
		return unitName;
	}

	private String grabBasicUnitName() {
		String unitName = env.get("database.persistence-unit");
		if (unitName == null) {
			unitName = DEFAULT_UNIT_NAME;
		}
		return unitName;
	}

	private void herokuAnalysis(String currentUnit) {
		if (currentUnit.equals("heroku")) {
			LOGGER.info("ready to use heroku database");
			String databaseUrl = System.getenv("DATABASE_URL");
			HerokuDatabaseInformation info = new HerokuDatabaseInformation(
					databaseUrl);
			info.exportToSystem();
		} else {
			LOGGER.info("not using heroku database");
		}
	}

	@Override
	public EntityManagerFactory getInstance() {
		return factory;
	}

	@PreDestroy
	public void destroy() {
		factory.close();
	}

}