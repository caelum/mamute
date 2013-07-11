package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.WithUserDAO.OrderType;
import br.com.caelum.brutal.dao.WithUserDAO.UserRole;
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
    private final WithUserDAO<Question> withAuthor;
	private final InvisibleForUsersRule invisible;

    public QuestionDAO(Session session, InvisibleForUsersRule invisible) {
        this.session = session;
		this.invisible = invisible;
		this.withAuthor = new WithUserDAO<Question>(session, Question.class, UserRole.AUTHOR);
    }
    
    public void save(Question q) {
        session.save(q);
    }

	public Question getById(Long questionId) {
		return (Question) session.load(Question.class, questionId);
	}
	
	public List<Question> allVisible(Integer page) {
		String hql = "from Question as q join fetch q.information qi" +
				" join fetch q.author qa" +
				" join fetch q.lastTouchedBy qa" +
				" left join fetch q.solution s" +
				" left join fetch q.solution.information si" +
				" "+ invisibleFilter("and") +" " + spamFilter() +" order by q.lastUpdatedAt desc";
		Query query = session.createQuery(hql);
		return query.setMaxResults(PAGE_SIZE)
			.setFirstResult(firstResultOf(page))
			.list();
	}

	public List<Question> unsolvedVisible(Integer page) {
		return session.createQuery("from Question as q "+invisibleFilter("and")+" (q.solution is null) order by q.lastUpdatedAt desc")
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(firstResultOf(page))
				.list();
	}
	
	public Long totalPagesUnsolvedVisible() {
		Long result = (Long) session.createQuery("select count(*) from Question as q "+invisibleFilter("and")+" (q.solution is null)").uniqueResult();
		return calculatePages(result);
	}
	
	public List<Question> unanswered(Integer page) {
		return session.createQuery("from Question as q " +invisibleFilter("and")+" q.answerCount = 0 order by q.lastUpdatedAt desc")
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(firstResultOf(page))
				.list();
	}
	
	public Long totalPagesWithoutAnswers() {
		Long result = (Long) session.createQuery("select count(*) from Question as q " +invisibleFilter("and")+" q.answerCount = 0").uniqueResult();
		return calculatePages(result);
	}

	public Question load(Question question) {
		return getById(question.getId());
	}

	public List<Question> withTagVisible(Tag tag, Integer page) {
		List<Question> questions = session.createQuery("select q from Question as q " +
				"join q.information.tags t " + 
				invisibleFilter("and") + " t = :tag order by q.lastUpdatedAt desc")
				.setParameter("tag", tag)
				.setFirstResult(firstResultOf(page))
				.setMaxResults(50)
				.list();
		return questions;
	}
	
	public List<Question> postsToPaginateBy(User user, OrderType orderByWhat, Integer page) {
		return withAuthor.by(user,orderByWhat, page);
	}
	
	public Long numberOfPagesTo(User user) {
		return withAuthor.numberOfPagesTo(user);
	}

	private String spamFilter() {
		return "q.voteCount > "+SPAM_BOUNDARY;
	}
	
	private String invisibleFilter(String connective) {
		return invisible.getInvisibleOrNotFilter("q", connective);
	}

	public Long countWithAuthor(User user) {
		return withAuthor.count(user);
	}
	
	public long numberOfPages() {
		String hql = "select count(*) from Question q " + invisibleFilter("and") + " " + spamFilter();
		Long totalItems = (Long) session.createQuery(hql).uniqueResult();
		long result = calculatePages(totalItems);
		return result;
	}

	private long calculatePages(Long count) {
		long result = count/PAGE_SIZE.longValue();
		if (count % PAGE_SIZE.longValue() != 0) {
			result++;
		}
		return result;
	}

	public long numberOfPages(Tag tag) {
		String hql = "select count(q.id) from Question q join q.information.tags tag " + invisibleFilter("and") + " " + spamFilter() + " and tag=:tag";
		Long totalItems = (Long) session.createQuery(hql).setParameter("tag", tag).uniqueResult();
		long result = calculatePages(totalItems);
		return result;
	}

	public List<Question> orderedByCreationDate(int maxResults) {
		String hql = "select q from Question q where q.moderationOptions.invisible = false order by q.createdAt desc";
		Query query = session.createQuery(hql);
		return query.setMaxResults(maxResults).list();
	}
	
	public List<Question> orderedByCreationDate(int maxResults, Tag tag) {
		String hql = "select q from Question q " +
				"join q.information.tags tags " +
				"where q.moderationOptions.invisible = false and tags=:tag " +
				"order by q.createdAt desc";
		Query query = session.createQuery(hql);
		return query.setParameter("tag", tag).setMaxResults(maxResults).list();
	}
	
	private int firstResultOf(Integer page) {
		return PAGE_SIZE * (page-1);
	}

	public List<Question> hot(DateTime since, int count) {
		String hql = "select q from Question q where q.createdAt > :since order by q.voteCount desc";
		return session.createQuery(hql).setParameter("since", since).setMaxResults(count).list();
	}
	
	public List<Question> randomUnanswered(DateTime after, DateTime before, int count) {
		String hql = "select q from Question q where q.solution is null and q.createdAt between :after and :before order by rand()";
		return session.createQuery(hql)
				.setParameter("before", before)
				.setParameter("after", after)
				.setMaxResults(count).list();
	}

}

