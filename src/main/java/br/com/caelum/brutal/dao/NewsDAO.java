package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.dao.WithUserDAO.UserRole;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;
@Component
public class NewsDAO implements PaginatableDAO  {
    private static final int SPAM_BOUNDARY = -5;
	private static final int PAGE_SIZE = 3;
	private final WithUserDAO<News> withAuthor;
	private Session session;
	private InvisibleForUsersRule invisible;
    
    public NewsDAO(Session session, InvisibleForUsersRule invisible) {
        this.session = session;
		this.invisible = invisible;
		this.withAuthor = new WithUserDAO<News>(session, News.class, UserRole.AUTHOR);
    }
    
	public List<News> allVisible(Integer page) {
		String hql = "from News as n join fetch n.information ni" +
				" join fetch n.author na" +
				" join fetch n.lastTouchedBy na" +
				" "+ invisibleFilter("and") +" " + spamFilter() +" order by n.lastUpdatedAt desc";
		Query query = session.createQuery(hql);
		return query.setMaxResults(PAGE_SIZE)
			.setFirstResult(firstResultOf(page))
			.list();
	}

	private int firstResultOf(Integer page) {
		return PAGE_SIZE * (page-1);
	}

	private String spamFilter() {
		return "n.voteCount > "+SPAM_BOUNDARY;
	}

	private String invisibleFilter(String connective) {
		return invisible.getInvisibleOrNotFilter("n", connective);
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

}
