package br.com.caelum.brutal.dao;

import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.gt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.dao.WithUserDAO.UserRole;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
@Component
public class NewsDAO implements PaginatableDAO  {
    private static final long SPAM_BOUNDARY = -5;
	private final WithUserDAO<News> withAuthor;
	private Session session;
	private InvisibleForUsersRule invisible;
    
    public NewsDAO(Session session, InvisibleForUsersRule invisible) {
        this.session = session;
		this.invisible = invisible;
		this.withAuthor = new WithUserDAO<News>(session, News.class, UserRole.AUTHOR, invisible);
    }
    
	@SuppressWarnings("unchecked")
	public List<News> allVisible(Integer initPage, Integer pageSize) {
		Criteria criteria = session.createCriteria(News.class, "n")
				.createAlias("n.information", "ni")
				.createAlias("n.author", "na")
				.createAlias("n.lastTouchedBy", "nl")
				.add(criterionSpamFilter())
				.addOrder(desc("n.lastUpdatedAt"))
				.setMaxResults(pageSize)
				.setFirstResult(firstResultOf(initPage, pageSize));
		return addInvisibleFilter(criteria).list();
	}

	private Criterion criterionSpamFilter() {
		return gt("n.voteCount", SPAM_BOUNDARY);
	}

	private int firstResultOf(Integer initPage, Integer pageSize) {
		return pageSize * (initPage-1);
	}

	private Criteria addInvisibleFilter(Criteria criteria) {
		return invisible.addFilter("n", criteria);
	}

	public List<News> postsToPaginateBy(User user, OrderType orderByWhat, Integer page) {
		return withAuthor.by(user,orderByWhat, page);
	}
	
	@Override
	public Long countWithAuthor(User author) {
		return withAuthor.count(author);
	}

	@Override
	public Long numberOfPagesTo(User author) {
		return withAuthor.numberOfPagesTo(author);
	}

	public void save(News news) {
		session.save(news);
	}

	public long numberOfPages(Integer pageSize) {
		Criteria criteria = session.createCriteria(News.class)
				.add(and(criterionSpamFilter(), Restrictions.eq("n.approved", true)))
				.setProjection(rowCount());
		Long totalItems = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(totalItems, pageSize);
	}
	
	private long calculatePages(Long count, Integer pageSize) {
		long result = count/pageSize.longValue();
		if (count % pageSize.longValue() != 0) {
			result++;
		}
		return result;
	}

	public List<News> allVisibleAndApproved(Integer page, int pageSize) {
		List<News> allVisible = allVisible(page, pageSize);
		return filterApproved(allVisible);
	}

	private List<News> filterApproved(List<News> allVisible) {
		Collection<News> filtered = Collections2.filter(allVisible, new Predicate<News>() {
			@Override
			public boolean apply(final News news) {
				return news.isApproved();
			}
		});
		return new ArrayList<>(filtered);
	}
}
