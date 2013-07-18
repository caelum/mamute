package br.com.caelum.brutal.dao;

import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.dao.WithUserPaginatedDAO.OrderType;
import br.com.caelum.brutal.dao.util.QuantityOfPagesCalculator;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Watchable;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class WatcherDAO implements PaginatableDAO{

	private static final int PAGE_SIZE = 5;
	private final Session session;
	private final InvisibleForUsersRule invisible;

	public WatcherDAO(Session session, InvisibleForUsersRule invisible) {
		this.session = session;
		this.invisible = invisible;
	}

	@SuppressWarnings("unchecked")
	public List<Watcher> of(Watchable watchable, Class<? extends Watchable> watchableType) {
		String query = "select watchers from "+ watchableType.getSimpleName() +" watchable join watchable.watchers watchers" +
				" join watchers.watcher watcher" +
				" where watchers.active = true and" +
				" watcher.isSubscribed = true and" +
				" watchable = :watchable";
		Query selectWatchers = session.createQuery(query)
							  .setParameter("watchable", watchable);
		return selectWatchers.list();
	}

	public void add(Watchable watchable, Watcher watcher, Class<? extends Watchable> watchableType) {
		if (!alreadyWatching(watchable, watcher, watchableType)){
			watchable.add(watcher);
			session.save(watcher);
		}
	}

	public boolean ping(Watchable watchable, User user, Class<? extends Watchable> watchableType) {
		Watcher watcher = findByWatchableAndUser(watchable, user, watchableType);
		if (watcher != null) { 
			watcher.activate();
			return true;
		}
		return false;
	}

	private Watcher findByWatchableAndUser(Watchable watchable, User user, Class<? extends Watchable> watchableType) {
		Watcher watch = (Watcher) session.createQuery("select watcher from "+ watchableType.getSimpleName() +" watchable" +
				" join watchable.watchers watcher" +
				" where watcher.watcher = :user and watchable = :watchable")
				.setParameter("watchable", watchable)
				.setParameter("user", user)
				.uniqueResult();
		return watch;
	}

	public void addOrRemove(Watchable watchable, Watcher watcher, Class<? extends Watchable> watchableType) {
		if(!alreadyWatching(watchable, watcher, watchableType)) {
			add(watchable, watcher, watchableType);
		} else {
			removeIfWatching(watchable, watcher, watchableType);
		}
	}

	public void removeIfWatching(Watchable watchable, Watcher watcher, Class<? extends Watchable> watchableType) {
		Watcher managed = findByWatchableAndUser(watchable, watcher.getWatcher(), watchableType);
		if (managed != null) {
			session.delete(managed);
			watchable.remove(watcher);
		}
	}

	private boolean alreadyWatching(Watchable watchable, Watcher watcher, Class<? extends Watchable> watchableType) {
		return findByWatchableAndUser(watchable, watcher.getWatcher(), watchableType) != null;
	}

	public List<Question> postsToPaginateBy(User user, OrderType orderType, Integer page) {
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
		
}
