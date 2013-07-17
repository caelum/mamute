package br.com.caelum.brutal.dao;

import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.gt;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.brutal.dao.WithUserPaginatedDAO.OrderType;
import br.com.caelum.brutal.dao.WithUserPaginatedDAO.UserRole;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;
@Component
public class NewsDAO implements PaginatableDAO  {
    private static final long SPAM_BOUNDARY = -5;
	private final WithUserPaginatedDAO<News> withAuthor;
	private Session session;
	private QueryFilter invisible;
	private final VisibleNewsFilter visibleFilter;
    
    public NewsDAO(Session session, ModeratorOrVisibleNewsFilter moderatorOrVisible, VisibleNewsFilter visibleFilter) {
        this.session = session;
		this.invisible = moderatorOrVisible;
		this.visibleFilter = visibleFilter;
		this.withAuthor = new WithUserPaginatedDAO<News>(session, News.class, UserRole.AUTHOR, moderatorOrVisible);
    }
    
	@SuppressWarnings("unchecked")
	public List<News> allVisible(Integer initPage, Integer pageSize) {
		Criteria criteria = defaultCriteria(initPage, pageSize);
		return addModeratorOrApprovedFilter(criteria).list();
	}

	@SuppressWarnings("unchecked")
	public List<News> allVisibleAndApproved(Integer initPage, Integer pageSize) {
		Criteria criteria = defaultCriteria(initPage, pageSize);
		return addApprovedFilter(criteria).list();
	}

	private Criteria addApprovedFilter(Criteria criteria) {
		return visibleFilter.addFilter("n", criteria);
	}

	private Criterion criterionSpamFilter() {
		return gt("n.voteCount", SPAM_BOUNDARY);
	}

	private int firstResultOf(Integer initPage, Integer pageSize) {
		return pageSize * (initPage-1);
	}

	private Criteria addModeratorOrApprovedFilter(Criteria criteria) {
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
		Criteria criteria = session.createCriteria(News.class, "n")
				.add(and(criterionSpamFilter(), Restrictions.eq("n.approved", true)))
				.setProjection(rowCount())
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		Long totalItems = (Long) addModeratorOrApprovedFilter(criteria).list().get(0);
		return calculatePages(totalItems, pageSize);
	}
	
	private long calculatePages(Long count, Integer pageSize) {
		long result = count/pageSize.longValue();
		if (count % pageSize.longValue() != 0) {
			result++;
		}
		return result;
	}

	
	private Criteria defaultCriteria(Integer initPage, Integer pageSize) {
		Criteria criteria = session.createCriteria(News.class, "n")
				.createAlias("n.information", "ni")
				.createAlias("n.author", "na")
				.createAlias("n.lastTouchedBy", "nl")
				.add(criterionSpamFilter())
				.addOrder(desc("n.lastUpdatedAt"))
				.setMaxResults(pageSize)
				.setFirstResult(firstResultOf(initPage, pageSize))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}
}
