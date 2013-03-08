package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.WithAuthorDAO.OrderType;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.SubscribableDTO;
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
	
	@SuppressWarnings("unchecked")
    public List<SubscribableDTO> getSubscribablesAfter(DateTime timeAgo) {
        Query query = session.createQuery("select distinct new br.com.caelum.brutal.model.SubscribableDTO(answer, author, question) from Answer answer " +
                "join answer.question question " +
        		"join question.answers ans " +
        		"join ans.author author where (answer.createdAt) > :timeAgo and author!=answer.author");
        List<SubscribableDTO> results = query.setParameter("timeAgo", timeAgo).list();
        
        query = session.createQuery("select distinct new br.com.caelum.brutal.model.SubscribableDTO(answer, author, question) from Answer answer " +
                "join answer.question question " +
        		"join question.author author " +
        		"where (answer.createdAt) > :timeAgo and author!=answer.author");
        results.addAll(query.setParameter("timeAgo", timeAgo).list());
        
        return results;
	}
	

	public List<Answer> withAuthorBy(User user, OrderType orderByWhat) {
		return withAuthor.by(user,orderByWhat);
	}
}




