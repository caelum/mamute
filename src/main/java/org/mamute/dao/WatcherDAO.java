package org.mamute.dao;

import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.mamute.dao.WithUserPaginatedDAO.OrderType;
import org.mamute.dao.util.QuantityOfPagesCalculator;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.interfaces.Watchable;
import org.mamute.model.watch.Watcher;

public class WatcherDAO implements PaginatableDAO {
	private static final int PAGE_SIZE = 5;

	private Session session;
	private InvisibleForUsersRule invisible;

	@Deprecated
	public WatcherDAO() {
	}

	@Inject
	public WatcherDAO(Session session, InvisibleForUsersRule invisible) {
		this.session = session;
		this.invisible = invisible;
	}

	@SuppressWarnings("unchecked")
	public List<Watcher> of(Watchable watchable) {
		String query = "select watchers from "+ watchable.getType().getSimpleName() +" watchable join watchable.watchers watchers" +
				" join watchers.watcher watcher" +
				" where watchers.active = true and" +
				" watchable = :watchable";
		Query selectWatchers = session.createQuery(query)
							  .setParameter("watchable", watchable);
		return selectWatchers.list();
	}

	public void add(Watchable watchable, Watcher watcher) {
		if (!alreadyWatching(watchable, watcher)){
			watchable.add(watcher);
			session.save(watcher);
		}
	}

	public boolean ping(Watchable watchable, User user) {
		Watcher watcher = findByWatchableAndUser(watchable, user);
		if (watcher != null) { 
			watcher.activate();
			return true;
		}
		return false;
	}

	private Watcher findByWatchableAndUser(Watchable watchable, User user) {
		Watcher watch = (Watcher) session.createQuery("select watcher from "+ watchable.getType().getSimpleName() +" watchable" +
				" join watchable.watchers watcher" +
				" where watcher.watcher = :user and watchable = :watchable")
				.setParameter("watchable", watchable)
				.setParameter("user", user)
				.uniqueResult();
		return watch;
	}

	public void addOrRemove(Watchable watchable, Watcher watcher) {
		if(!alreadyWatching(watchable, watcher)) {
			add(watchable, watcher);
		} else {
			removeIfWatching(watchable, watcher);
		}
	}

	public void removeIfWatching(Watchable watchable, Watcher watcher) {
		Watcher managed = findByWatchableAndUser(watchable, watcher.getWatcher());
		if (managed != null) {
			watchable.remove(managed);
		}
	}

	private boolean alreadyWatching(Watchable watchable, Watcher watcher) {
		return findByWatchableAndUser(watchable, watcher.getWatcher()) != null;
	}

	public List<Question> ofUserPaginatedBy(User user, OrderType orderType, Integer page) {
		Criteria criteria = defaultCriteria(user)
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(PAGE_SIZE * (page-1));
		return  criteria.list();
	}

	public Long countWithAuthor(User user) {
		return (Long) defaultCriteria(user).setProjection(rowCount()).list().get(0);
	}

	public Long numberOfPagesTo(User user) {
		return QuantityOfPagesCalculator.calculatePages(countWithAuthor(user), PAGE_SIZE);
	}

	private Criteria defaultCriteria(User user) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.createAlias("q.watchers", "watchers")
				.createAlias("watchers.watcher", "watcher")
				.add(eq("watcher.id", user.getId()))
				.addOrder(desc("watchers.createdAt"));
		return  addInvisibleFilter(criteria);
	}

	private Criteria addInvisibleFilter(Criteria criteria) {
		return invisible.addFilter("q", criteria);
	}

	public Watchable findWatchable(Long watchableId, Class<?> watchableType) {
		return (Watchable) session.get(watchableType, watchableId);
	}
		
}
