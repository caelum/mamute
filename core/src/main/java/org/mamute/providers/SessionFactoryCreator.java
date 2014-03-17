package org.mamute.providers;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.validation.ValidatorFactory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.mamute.components.HerokuDatabaseInformation;
import org.mamute.model.Answer;
import org.mamute.model.AnswerInformation;
import org.mamute.model.Comment;
import org.mamute.model.Flag;
import org.mamute.model.LoginMethod;
import org.mamute.model.News;
import org.mamute.model.NewsInformation;
import org.mamute.model.NewsletterSentLog;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.ReputationEvent;
import org.mamute.model.Tag;
import org.mamute.model.TagPage;
import org.mamute.model.User;
import org.mamute.model.UserSession;
import org.mamute.model.Vote;
import org.mamute.model.watch.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class SessionFactoryCreator {
	
	public static final String JODA_TIME_TYPE= "org.jadira.usertype.dateandtime.joda.PersistentDateTime";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SessionFactoryCreator.class);
	
	private Configuration cfg;
	private SessionFactory factory;
	private Environment env;
	private ValidatorFactory vf;

	@Deprecated
	public SessionFactoryCreator() {
	}

	@Inject
	public SessionFactoryCreator(Environment env, ValidatorFactory vf) {
		this.env = env;
		this.vf = vf;
	}

	@PostConstruct
	public void init() {
		URL xml = env.getResource("/hibernate.cfg.xml");
		LOGGER.info("Loading hibernate xml from " + xml);
		this.cfg = new Configuration().configure(xml);
		
		if (this.vf != null) {
			Map<Object, Object> properties = cfg.getProperties();
			properties.put("javax.persistence.validation.factory", this.vf);
			
		}
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
		cfg.addAnnotatedClass(TagPage.class);

		this.factory = cfg.buildSessionFactory();
		
	}

	@Produces
	@javax.enterprise.context.ApplicationScoped
	public SessionFactory getInstance() {
		return factory;
	}

	void destroy(@Disposes SessionFactory factory) {
		if (!factory.isClosed()) {
			factory.close();
		}
		factory = null;
	}

	public void dropAndCreate() {
		destroy(this.factory);
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