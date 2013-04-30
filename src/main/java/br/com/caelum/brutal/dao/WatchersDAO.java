package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watch;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class WatchersDAO {

	private final Session session;

	public WatchersDAO(Session session) {
		this.session = session;
	}

	public List<User> of(Question question) {
		String query = "select watcher from Watch watch join watch.watcher watcher" +
				" where watch.active = true and" +
				" watcher.isSubscribed = true and" +
				" watch.watchedQuestion = :question";
		Query selectWatchers = session.createQuery(query)
							  .setParameter("question", question);
		return selectWatchers.list();
	}

	public void add(Watch watch) {
		session.save(watch);
	}
	

}
