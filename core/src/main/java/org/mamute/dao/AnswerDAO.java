package org.mamute.dao;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.mamute.dao.WithUserPaginatedDAO.OrderType;
import org.mamute.dao.WithUserPaginatedDAO.UserRole;
import org.mamute.model.Answer;
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
	
	public List<Answer> postsToPaginateBy(User user, OrderType orderByWhat, Integer page) {
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

}




