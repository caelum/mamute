package org.mamute.dao;

import static java.util.Collections.EMPTY_LIST;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.gt;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.isNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.mamute.dao.WithUserPaginatedDAO.OrderType;
import org.mamute.dao.WithUserPaginatedDAO.UserRole;
import org.mamute.model.*;
import org.mamute.model.interfaces.RssContent;

@SuppressWarnings("unchecked")
public class QuestionDAO implements PaginatableDAO {
    protected static final Integer PAGE_SIZE = 35;
	public static final long SPAM_BOUNDARY = -5;
	
	private Session session;
    private WithUserPaginatedDAO<Question> withAuthor;
	private InvisibleForUsersRule invisible;

	@Deprecated
	public QuestionDAO() {
	}

	@Inject
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
				.createCriteria("q.solution.information", JoinType.LEFT_OUTER_JOIN)
				.addOrder(desc("q.lastUpdatedAt"))
				.setFirstResult(firstResultOf(page))
				.setMaxResults(PAGE_SIZE);

		return addInvisibleFilter(criteria).list();
	}

	public List<Question> unsolvedVisible(Integer page) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.add(isNull("q.solution"))
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

	public List<Question> withTagVisible(Tag tag, Integer page, boolean semRespostas) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.createAlias("q.information.tags", "t")
				.add(Restrictions.eq("t.id", tag.getId()))
				.addOrder(Order.desc("q.lastUpdatedAt"))
				.setFirstResult(firstResultOf(page))
				.setMaxResults(50)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if (semRespostas) {
			criteria.add(Restrictions.eq("q.answerCount", 0l));
		}

		
		return addInvisibleFilter(criteria).list();
	}
	
	public List<Question> ofUserPaginatedBy(User user, OrderType orderByWhat, Integer page) {
		return withAuthor.by(user,orderByWhat, page);
	}

	public List<RssContent> orderedByCreationDate(int maxResults) {
		return session.createCriteria(Question.class, "q")
				.add(Restrictions.eq("q.moderationOptions.invisible", false))
				.addOrder(Order.desc("q.createdAt"))
				.setMaxResults(maxResults)
				.list();
	}
	
	public List<RssContent> orderedByCreationDate(int maxResults, Tag tag) {
		return session.createCriteria(Question.class, "q")
				.createAlias("q.information.tags", "tags")
				.add(Restrictions.and(Restrictions.eq("q.moderationOptions.invisible", false), Restrictions.eq("tags.id", tag.getId())))
				.addOrder(Order.desc("q.createdAt"))
				.setMaxResults(maxResults)
				.list();
	}
	

	public List<Question> getRelatedTo(Question question) {
		if (question.hasTags()) {
			return session.createCriteria(Question.class, "q")
					.createAlias("q.information.tags", "tags")
					.add(Restrictions.eq("tags.id", question.getMostImportantTag().getId()))
					.addOrder(Order.desc("q.createdAt"))
					.setMaxResults(5)
					.list();
		}
		return EMPTY_LIST;
	}

	public List<Question> hot(DateTime since, int count) {
		return session.createCriteria(Question.class, "q")
				.add(gt("q.createdAt", since))
				.add(and(Restrictions.eq("q.moderationOptions.invisible", false)))
				.addOrder(Order.desc("q.voteCount"))
				.setMaxResults(count)
				.list();
	}
	
	public List<Question> top(String section, int count, DateTime since) {
		Order order;
		if (section.equals("viewed")) {
			order = Order.desc("q.views");
		}
		else if (section.equals("answered")) {
			order = Order.desc("q.answerCount");
		}
		else /*if (section.equals("voted"))*/ {
			order = Order.desc("q.voteCount");
		}
		return session.createCriteria(Question.class, "q")
				.add(and(Restrictions.eq("q.moderationOptions.invisible", false)))
				.add(gt("q.createdAt", since))
				.addOrder(order)
				.setMaxResults(count)
				.list();
	}
	
	public List<Question> randomUnanswered(DateTime after, DateTime before, int count) {
		return session.createCriteria(Question.class, "q")
				.add(and(isNull("q.solution"), Restrictions.between("q.createdAt", after, before)))
				.add(and(Restrictions.eq("q.moderationOptions.invisible", false)))
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
				.setProjection(rowCount());
		Long totalItems = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(totalItems);
	}

	public long numberOfPages(Tag tag) {
		Criteria criteria = session.createCriteria(Question.class, "q")
				.createAlias("q.information", "qi")
				.createAlias("qi.tags", "t")
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

	private Criteria addInvisibleFilter(Criteria criteria) {
		return invisible.addFilter("q", criteria);
	}

	public List<Question> withTagVisible(Tag tag, int page) {
		return withTagVisible(tag, page, false);
	}

	public Question fromCommentId (Long id) {
		String sql = "SELECT Question_id FROM Question_Comments " +
				"WHERE comments_id = :id ";
		Query query = session.createSQLQuery(sql);
		List<BigInteger> idList = query.setParameter("id", id).list();
		Long questionId = null;
		if (idList.size() > 0) {
			questionId = idList.get(0).longValue();
			return getById(questionId);
		}
		return null;
	}

	/**
	 * Query for the set of question IDs. Order is preserved in the returned list.
	 */
	public List<Question> allVisibleByIds(List<Long> ids) {
		List<Question> questions = new ArrayList<>();
		if(ids != null && ids.size() >0) {
			addInvisibleFilter(session.createCriteria(Question.class, "q").add(in("id", ids))).list();
			for (Long id : ids) {
				Criteria criteria = session.createCriteria(Question.class, "q").add(Restrictions.eq("id", id));
				Question question = (Question) addInvisibleFilter(criteria).uniqueResult();
				if(question != null) questions.add(question);
			}
		}
		return questions;
	}


	public Question questionWith(Attachment attachment) {
		return (Question) session.createQuery("from Question where :a in elements(attachments)")
				.setParameter("a", attachment)
				.uniqueResult();
	}

	public void delete(Question question) {
		session.delete(question);
	}


	private void deleteAttachments(Question question) {
		ArrayList<Long> ids = new ArrayList<>();
		for (Attachment attachment : question.getAttachments()) {
			ids.add(attachment.getId());
		}
		if (!ids.isEmpty()) {
			session.createQuery("delete from Attachment where id in :ids")
				.setParameterList("ids", ids)
				.executeUpdate();
		}
	}

	public void deleteFully(Question question, User user) {

		this.delete(question);
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) {
			session.delete(answer);
		}
		List<Flag> flags = question.getFlags();
		for (Flag flag : flags) {
			session.delete(flag);
		}
		List<Comment> comments = question.getVisibleCommentsFor(user);
		for (Comment comment : comments) {
			session.delete(comment);
		}

		session.createSQLQuery("update ReputationEvent set deleted=1 where context_id=:id and context_type='QUESTION'")
				.setParameter("id", question.getId()).executeUpdate();

	}

	public void deleteQuestionsOf(User user) {
		List<Question> questions = session.createQuery("from Question q where q.author=:user")
				.setParameter("user", user)
				.list();
		for (Question question : questions) {
			deleteFully(question, user);
		}
	}

	private class QuestionQuery {
		private final Question question;

		public QuestionQuery(Question question) {
			this.question = question;
		}

		public void runFor(String query) {
			session.createQuery(query).setParameter("id", question.getId())
				.executeUpdate();
		}

		public void execute(String query) {
			session.createSQLQuery(query)
					.setParameter("id", question.getId())
					.executeUpdate();
		}
	}
}
