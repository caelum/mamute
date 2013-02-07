package br.com.caelum.brutal.providers;

import java.net.URL;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.brutal.components.HerokuDatabaseInformation;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class SessionFactoryCreator implements ComponentFactory<SessionFactory> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SessionFactoryCreator.class);
	private Configuration cfg;

	public SessionFactoryCreator(Environment env) {
		URL xml = env.getResource("/hibernate.cfg.xml");
		LOGGER.info("Loading hibernate xml from " + xml);
		this.cfg = new Configuration().configure(xml);
		
		String databaseUrl = System.getenv("DATABASE_URL");
		LOGGER.info("env got " + databaseUrl);
		if (databaseUrl != null) {
			LOGGER.info("ready to use heroku database");
			HerokuDatabaseInformation info = new HerokuDatabaseInformation(
					databaseUrl);
			Map<String, String> heroku = info.exportToProperties();
			for(String key : heroku.keySet()) {
				cfg.setProperty(key, heroku.get(key));
			}
		}

		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Question.class);
		cfg.addAnnotatedClass(AnswerInformation.class);
		cfg.addAnnotatedClass(Answer.class);
		cfg.addAnnotatedClass(Tag.class);
		cfg.addAnnotatedClass(Vote.class);
		cfg.addAnnotatedClass(Comment.class);
		cfg.addAnnotatedClass(QuestionInformation.class);

		init();
	}

	private void init() {
		this.factory = cfg.buildSessionFactory();
	}

	private SessionFactory factory;

	public SessionFactory getInstance() {
		return factory;
	}

	@PreDestroy
	void destroy() {
		factory.close();
	}

	public void dropAndCreate() {
		factory.close();
		factory = null;
		new SchemaExport(cfg).drop(true, true);
		new SchemaExport(cfg).create(true, true);
		init();
	}
	public void drop() {
		factory.close();
		factory = null;
		new SchemaExport(cfg).drop(true, true);
		init();
	}
	
	public Configuration getCfg() {
		return cfg;
	}
}