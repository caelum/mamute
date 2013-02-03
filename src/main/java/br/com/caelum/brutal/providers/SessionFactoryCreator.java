package br.com.caelum.brutal.providers;

import java.net.URL;

import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.UpdateHistory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class SessionFactoryCreator implements ComponentFactory<SessionFactory> {

	private static final Logger logger = LoggerFactory
			.getLogger(SessionFactoryCreator.class);
	private Configuration cfg;

	public SessionFactoryCreator(Environment env) {
		URL xml = env.getResource("/hibernate.cfg.xml");
		logger.info("Loading hibernate xml from " + xml);
		this.cfg = new Configuration().configure(xml);

		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Question.class);
		cfg.addAnnotatedClass(Answer.class);
		cfg.addAnnotatedClass(Tag.class);
		cfg.addAnnotatedClass(Vote.class);
		cfg.addAnnotatedClass(Comment.class);
		cfg.addAnnotatedClass(UpdateHistory.class);

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