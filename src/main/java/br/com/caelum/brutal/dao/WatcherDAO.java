package br.com.caelum.brutal.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.dao.WithUserDAO.UserRole;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class WatcherDAO implements PaginatableDAO{

	private final Session session;
	private final WithUserDAO<Watcher> withUser;

	public WatcherDAO(Session session) {
		this.session = session;
		this.withUser = new WithUserDAO<>(session, Watcher.class, UserRole.WATCHER);
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
		if (!alreadyWatching(watcher))
			session.save(watcher);
	}

	public boolean ping(Question question, User user) {
		Watcher watcher = findByQuestionAndUser(question, user);
		if (watcher != null) { 
			watcher.activate();
			return true;
		}
		return false;
	}

	private Watcher findByQuestionAndUser(Question question, User user) {
		Watcher watch = (Watcher) session.createQuery("from Watcher where watchedQuestion = :question and watcher = :user")
				.setParameter("question", question)
				.setParameter("user", user)
				.uniqueResult();
		return watch;
	}

	public void addOrRemove(Watcher watcher) {
		if(!alreadyWatching(watcher)) {
			add(watcher);
		} else {
			removeIfWatching(watcher);
		}
	}

	public void removeIfWatching(Watcher watcher) {
		Watcher managed = findByQuestionAndUser(watcher.getWatchedQuestion(), watcher.getWatcher());
		if (managed != null) {
			session.delete(managed);
		}
	}

	private boolean alreadyWatching(Watcher watcher) {
		return findByQuestionAndUser(watcher.getWatchedQuestion(), watcher.getWatcher()) != null;
	}

	public List<Question> postsToPaginateBy(User user, OrderType orderType, Integer page) {
		List<Watcher> watchers = withUser.by(user, OrderType.ByDate, page);
		ArrayList<Question> questions = new ArrayList<>();
		for (Watcher watcher : watchers) {
			questions.add(watcher.getWatchedQuestion());
		}
		return questions;
	}
	
	public Long countWithAuthor(User user) {
		return withUser.count(user);
	}

	public Long numberOfPagesTo(User user) {
		return withUser.numberOfPagesTo(user);
	}
	
}
