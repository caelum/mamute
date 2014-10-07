package org.mamute.providers;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.hibernate.SessionFactory;
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

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class SessionFactoryCreator {

	public static final String JODA_TIME_TYPE = "org.jadira.usertype.dateandtime.joda.PersistentDateTime";

	private final MamuteDatabaseConfiguration cfg;
	private SessionFactory factory;

	/**
	 * @deprecated CDI eyes only
	 */
	public SessionFactoryCreator() {
		this(null);
	}
	
	@Inject
	public SessionFactoryCreator(MamuteDatabaseConfiguration cfg) {
		this.cfg = cfg;
	}
	
	@PostConstruct
	public void init() {
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
		cfg.getSchema().drop(true, true);
		cfg.getSchema().create(true, true);
		init();
	}

	public void drop() {
		factory.close();
		factory = null;
		cfg.getSchema().drop(true, true);
		init();
	}


}
