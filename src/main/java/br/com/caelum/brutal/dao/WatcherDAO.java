package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class WatcherDAO {

	private final Session session;

	public WatcherDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<Watcher> of(Question question) {
		String query = "select watch from Watcher watch join watch.watcher watcher" +
				" where watch.active = true and" +
				" watcher.isSubscribed = true and" +
				" watch.watchedQuestion = :question";
		Query selectWatchers = session.createQuery(query)
							  .setParameter("question", question);
		return selectWatchers.list();
	}

	public void add(Watcher watcher) {
		if (findByQuestionAndUser(watcher.getWatchedQuestion(), watcher.getWatcher()) == null)
			session.save(watcher);
	}

	public void ping(Question question, User user) {
		Watcher watcher = findByQuestionAndUser(question, user);
		if (watcher != null) 
			watcher.activate();
	}

	private Watcher findByQuestionAndUser(Question question, User user) {
		Watcher watch = (Watcher) session.createQuery("from Watcher where watchedQuestion = :question and watcher = :user")
				.setParameter("question", question)
				.setParameter("user", user)
				.uniqueResult();
		return watch;
	}

}
