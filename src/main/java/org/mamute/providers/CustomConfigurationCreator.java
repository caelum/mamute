package org.mamute.providers;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.hibernate.ConfigurationCreator;
import org.hibernate.cfg.Configuration;
import org.mamute.model.*;
import org.mamute.model.watch.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.validation.ValidatorFactory;
import java.net.URL;
import java.util.Map;

@Specializes
public class CustomConfigurationCreator extends ConfigurationCreator {

	public static final String JODA_TIME_TYPE = "org.jadira.usertype.dateandtime.joda.PersistentDateTime";
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomConfigurationCreator.class);

	@Inject
	private Environment env;

	@Inject
	private ValidatorFactory vf;

	/**
	 * @deprecated cdi eyes only
	 */
	public CustomConfigurationCreator() {
	}

	public CustomConfigurationCreator(Environment env) {
		this.env = env;
	}

	@Override
	protected URL getHibernateCfgLocation() {
		return env.getResource("/hibernate.cfg.xml");
	}

	@Override
	@Produces
	@ApplicationScoped
	public Configuration getInstance() {
		URL location = getHibernateCfgLocation();
		LOGGER.debug("building configuration using {} file", location);

		Configuration cfg = new Configuration().configure(location);

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

		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Question.class);
		cfg.addAnnotatedClass(AnswerInformation.class);
		cfg.addAnnotatedClass(Answer.class);
		cfg.addAnnotatedClass(Tag.class);
		cfg.addAnnotatedClass(Vote.class);
		cfg.addAnnotatedClass(Comment.class);
		cfg.addAnnotatedClass(QuestionInformation.class);
		cfg.addAnnotatedClass(Flag.class);
		cfg.addAnnotatedClass(LoginMethod.class);
		cfg.addAnnotatedClass(UserSession.class);
		cfg.addAnnotatedClass(Watcher.class);
		cfg.addAnnotatedClass(ReputationEvent.class);
		cfg.addAnnotatedClass(News.class);
		cfg.addAnnotatedClass(NewsInformation.class);
		cfg.addAnnotatedClass(NewsletterSentLog.class);
		cfg.addAnnotatedClass(TagPage.class);

		return cfg;
	}

}
