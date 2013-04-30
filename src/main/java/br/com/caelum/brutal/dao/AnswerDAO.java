package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.dao.WithAuthorDAO.OrderType;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AnswerDAO {

	private final Session session;
	private WithAuthorDAO<Answer> withAuthor;

	public AnswerDAO(Session session) {
		this.session = session;
		withAuthor = new WithAuthorDAO<Answer>(session, Answer.class);
	}
	
	public Answer getById(Long id) {
		return (Answer) session.load(Answer.class, id);
	}

	public void save(Answer answer) {
		this.session.save(answer);
	}
	

	public List<Answer> allWithAuthorBy(User user, OrderType orderByWhat) {
		return withAuthor.by(user, orderByWhat);
	}

	public Long countWithAuthor(User user) {
		return withAuthor.count(user);
	}
}




