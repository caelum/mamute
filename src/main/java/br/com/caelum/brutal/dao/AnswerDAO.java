package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.dao.WithUserPaginatedDAO.OrderType;
import br.com.caelum.brutal.dao.WithUserPaginatedDAO.UserRole;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.User;

@Component
public class AnswerDAO implements PaginatableDAO{

	private final Session session;
	private final WithUserPaginatedDAO<Answer> withAuthor;

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
}




