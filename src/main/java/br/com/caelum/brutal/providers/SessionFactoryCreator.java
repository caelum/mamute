package br.com.caelum.brutal.providers;

import java.net.URL;
import java.util.Properties;

import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.brutal.components.HerokuDatabaseInformation;
import br.com.caelum.brutal.migration.DatabaseManager;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsInformation;
import br.com.caelum.brutal.model.NewsletterSentLog;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.UserSession;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class SessionFactoryCreator implements ComponentFactory<SessionFactory> {
	
	public static final String JODA_TIME_TYPE= "org.jadira.usertype.dateandtime.joda.PersistentDateTime";

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
			Properties heroku = info.exportToProperties();
			LOGGER.info(heroku.toString());
			cfg.addProperties(heroku);
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
		if(!factory.isClosed()) {
		factory.close();
		}
		factory = null;
	}

	public void dropAndCreate() {
		destroy();
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
	
	public void createFromScratch() {
		final Session session = factory.openSession();
		new DatabaseManager(session).importAll("/db_structure.sql");
	}
}