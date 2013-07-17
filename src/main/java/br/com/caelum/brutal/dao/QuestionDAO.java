package br.com.caelum.brutal.dao;

import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.gt;
import static org.hibernate.criterion.Restrictions.isNull;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.WithUserPaginatedDAO.OrderType;
import br.com.caelum.brutal.dao.WithUserPaginatedDAO.UserRole;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("unchecked")
public class QuestionDAO implements PaginatableDAO {
	
    private static final Integer PAGE_SIZE = 50;
	public static final long SPAM_BOUNDARY = -5;
	private final Session session;
    private final WithUserPaginatedDAO<Question> withAuthor;
	private final InvisibleForUsersRule invisible;

    public QuestionDAO(Session session, InvisibleForUsersRule invisible) {
        this.session = session;
		this.invisible = invisible;
		this.withAuthor = new WithUserPaginatedDAO<Question>(session, Question.class, UserRole.AUTHOR, invisible);
    }
    
    public void save(Question q) {
        session.save(q);
    }

	public Question getById(Long questionId) {
		return (Question) session.load(Question.class, questionId);
	}
	
	public List<Question> allVisible(Integer page) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.createAlias("q.information", "qi")
				.createAlias("q.author", "qa")
				.createAlias("q.lastTouchedBy", "ql")
				.createAlias("q.solution", "s", Criteria.LEFT_JOIN)
				.createAlias("q.solution.information", "si", Criteria.LEFT_JOIN)
				.add(criterionSpamFilter())
				.addOrder(desc("q.lastUpdatedAt"))
				.setFirstResult(firstResultOf(page))
				.setMaxResults(PAGE_SIZE)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return addInvisibleFilter(criteria).list();
	}

	public List<Question> unsolvedVisible(Integer page) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.add(Restrictions.and(criterionSpamFilter(), isNull("q.solution")))
				.addOrder(Order.desc("q.lastUpdatedAt"))
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(firstResultOf(page))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				
		return addInvisibleFilter(criteria).list();
	}
	
	public List<Question> unanswered(Integer page) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.add(Restrictions.eq("q.answerCount", 0l))
				.addOrder(Order.desc("q.lastUpdatedAt"))
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(firstResultOf(page))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return addInvisibleFilter(criteria).list();
	}

	public Question load(Question question) {
		return getById(question.getId());
	}

	public List<Question> withTagVisible(Tag tag, Integer page) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.createAlias("q.information.tags", "t")
				.add(Restrictions.eq("t.id", tag.getId()))
				.addOrder(Order.desc("q.lastUpdatedAt"))
				.setFirstResult(firstResultOf(page))
				.setMaxResults(50)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		
		return addInvisibleFilter(criteria).list();
	}
	
	public List<Question> postsToPaginateBy(User user, OrderType orderByWhat, Integer page) {
		return withAuthor.by(user,orderByWhat, page);
	}

	public List<Question> orderedByCreationDate(int maxResults) {
		return session.createCriteria(Question.class, "q")
				.add(Restrictions.eq("q.moderationOptions.invisible", false))
				.addOrder(Order.desc("q.createdAt"))
				.setMaxResults(maxResults)
				.list();
	}
	
	public List<Question> orderedByCreationDate(int maxResults, Tag tag) {
		return session.createCriteria(Question.class, "q")
				.createAlias("q.information.tags", "tags")
				.add(Restrictions.and(Restrictions.eq("q.moderationOptions.invisible", false), Restrictions.eq("tags.id", tag.getId())))
				.addOrder(Order.desc("q.createdAt"))
				.setMaxResults(maxResults)
				.list();
	}

	public List<Question> hot(DateTime since, int count) {
		return session.createCriteria(Question.class, "q")
				.add(gt("q.createdAt", since))
				.addOrder(Order.desc("q.voteCount"))
				.setMaxResults(count)
				.list();
	}
	
	public List<Question> randomUnanswered(DateTime after, DateTime before, int count) {
		return session.createCriteria(Question.class, "q")
				.add(and(isNull("q.solution"), Restrictions.between("q.createdAt", after, before)))
				.add(Restrictions.sqlRestriction("1=1 order by rand()"))
				.setMaxResults(count)
				.list();
	}

	public Long countWithAuthor(User user) {
		return withAuthor.count(user);
	}

	public Long numberOfPagesTo(User user) {
		return withAuthor.numberOfPagesTo(user);
	}
	
	public long numberOfPages() {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.add(criterionSpamFilter())
				.setProjection(rowCount());
		Long totalItems = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(totalItems);
	}

	public long numberOfPages(Tag tag) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.createAlias("q.information", "qi")
				.createAlias("qi.tags", "t")
				.add(criterionSpamFilter())
				.add(eq("t.id", tag.getId()))
				.setProjection(rowCount());
		Long totalItems = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(totalItems);
	}

	public Long totalPagesUnsolvedVisible() {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.add(isNull("q.solution"))
				.setProjection(rowCount());
		Long result = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(result);
	}
	
	public Long totalPagesWithoutAnswers() {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.add(Restrictions.eq("q.answerCount", 0l))
				.setProjection(rowCount());
		Long result = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(result);
	}

	private int firstResultOf(Integer page) {
		return PAGE_SIZE * (page-1);
	}

	private long calculatePages(Long count) {
		long result = count/PAGE_SIZE.longValue();
		if (count % PAGE_SIZE.longValue() != 0) {
			result++;
		}
		return result;
	}

	private Criterion criterionSpamFilter() {
		return Restrictions.gt("q.voteCount", SPAM_BOUNDARY);
	}
	
	private Criteria addInvisibleFilter(Criteria criteria) {
		return invisible.addFilter("q", criteria);
	}
}

