package org.mamute.dao;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.mamute.dao.WithUserPaginatedDAO.OrderType;
import org.mamute.dao.WithUserPaginatedDAO.UserRole;
import org.mamute.model.Answer;
import org.mamute.model.Attachment;
import org.mamute.model.Comment;
import org.mamute.model.User;

@SuppressWarnings("unchecked")
public class AnswerDAO implements PaginatableDAO{

	private Session session;
	private WithUserPaginatedDAO<Answer> withAuthor;

	@Deprecated
	public AnswerDAO() {}
	
	@Inject
	public AnswerDAO(Session session, InvisibleForUsersRule invisible) {
		this.session = session;
		withAuthor = new WithUserPaginatedDAO<Answer>(session, Answer.class, UserRole.AUTHOR, invisible);
	}
	
	public Answer getById(Long id) {
		return (Answer) session.load(Answer.class, id);
	}

	public void save(Answer answer) {
		this.session.save(answer);
	}
	
	public List<Answer> ofUserPaginatedBy(User user, OrderType orderByWhat, Integer page) {
		return withAuthor.by(user, orderByWhat, page);
	}
	
	public Long countWithAuthor(User user) {
		return withAuthor.count(user);
	}
	
	public Long numberOfPagesTo(User user) {
		return withAuthor.numberOfPagesTo(user);
	}

	public Answer fromCommentId (Long id) {
		String sql = "SELECT Answer_id FROM Answer_Comments " +
				"WHERE comments_id = :id ";
		Query query = session.createSQLQuery(sql);
		List<BigInteger> idList = query.setParameter("id", id).list();
		Long answerId = null;
		if (idList.size() > 0) {
			answerId = idList.get(0).longValue();
			return getById(answerId);
		}
		return null;
	}

	public Answer answerWith(Attachment attachment) {
		return (Answer) session.createQuery("from Answer where :a in elements(attachments)")
				.setParameter("a", attachment)
				.uniqueResult();
	}

	public void delete(Answer answer) {
		answer.getQuestion().subtractAnswer();
		session.delete(answer);
		for (Comment comment : answer.getAllComments()) {
			session.delete(comment);
		}
	}

	public void deleteAnswersOf(User user) {
		List<Answer> answers = session.createQuery("from Answer a where a.author=:user")
				.setParameter("user", user)
				.list();
		for (Answer answer : answers) {
			delete(answer);
		}
	}
}




