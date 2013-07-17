package br.com.caelum.brutal.dao;

import static org.hibernate.criterion.Projections.rowCount;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.brutal.dao.WithUserPaginatedDAO.OrderType;
import br.com.caelum.brutal.dao.util.QuantityOfPagesCalculator;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class WatcherDAO implements PaginatableDAO{

	private static final int PAGE_SIZE = 5;
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
		return session.createCriteria(Question.class, "p")
				.createAlias("p.watchers", "watchers")
				.createAlias("watchers.watcher", "watcher")
				.add(Restrictions.eq("watcher.id", user.getId()))
				.addOrder(Order.desc("watchers.createdAt"));
	}
		
}
