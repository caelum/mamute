package org.mamute.providers;

import java.net.URL;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ValidatorFactory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.mamute.meta.MamuteMetaInformation;
import org.mamute.model.interfaces.Flaggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;

@ApplicationScoped
public class MamuteDatabaseConfiguration {
	public static final String DATABASE_PROPERTY = "mamute.database";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MamuteDatabaseConfiguration.class);

	private final Environment env;
	private final ValidatorFactory vf;
	private final MamuteMetaInformation meta;
	private Configuration cfg;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public MamuteDatabaseConfiguration() {
		this(null, null, null);
	}
	
	@Inject
	public MamuteDatabaseConfiguration(Environment env, ValidatorFactory vf, MamuteMetaInformation meta) {
		this.env = env;
		this.vf = vf;
		this.meta = meta;
	}

	@PostConstruct
	public void init(){
		String db = System.getProperty(DATABASE_PROPERTY);
		String hibernateCfg;

		if(db == null || db.equals("mysql")) {
			hibernateCfg = "/hibernate.cfg.xml";
		}else {
			hibernateCfg = "/hibernate-"+db+".cfg.xml";
		}
		
		
		URL xml = env.getResource(hibernateCfg);
		LOGGER.info("Loading hibernate xml from " + xml);
		
		cfg = new Configuration().configure(xml);
		
		if (this.vf != null) {
			Map<Object, Object> properties = cfg.getProperties();
			properties.put("javax.persistence.validation.factory", this.vf);
		}

		String url = System.getenv("JDBC_URL");
		if (url != null) {
			String user = System.getenv("USER");
			String password = System.getenv("PASSWORD");

			LOGGER.info("reading database config from environment: " + url);
			cfg.setProperty("hibernate.connection.url", url);
			cfg.setProperty("hibernate.connection.username", user);
			cfg.setProperty("hibernate.connection.password", password);
		}
		
	}

	public Map<Object, Object> getProperties() {
		return cfg.getProperties();
	}

	public void addAnnotatedClass(Class<?> clazz) {
		if(meta != null && Flaggable.class.isAssignableFrom(clazz)){
			meta.add((Class<? extends Flaggable>) clazz);
		}
		cfg.addAnnotatedClass(clazz);
	}

	public SessionFactory buildSessionFactory() {
		return cfg.buildSessionFactory();
	}

	public SchemaExport getSchema() {
		return new SchemaExport(cfg);
	}

	public SchemaUpdate getSchemaUpdate() {
		return new SchemaUpdate(cfg);
	}

}
