package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.SubscribableAndUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AnswerDAO {

	private final Session session;

	public AnswerDAO(Session session) {
		this.session = session;
	}
	
	public Answer getById(Long id) {
		return (Answer) session.load(Answer.class, id);
	}

	public void save(Answer answer) {
		this.session.save(answer);
	}
	
	@SuppressWarnings("unchecked")
    public List<SubscribableAndUser> getSubscribablesAfter(DateTime timeAgo) {
        Query query = session.createQuery("select distinct new br.com.caelum.brutal.model.SubscribableAndUser(answer, author) from Answer answer " +
        		"join answer.question.answers ans " +
        		"join ans.author author where (answer.createdAt) > :timeAgo and author!=answer.author");
        List<SubscribableAndUser> results = query.setParameter("timeAgo", timeAgo).list();
        
        query = session.createQuery("select distinct new br.com.caelum.brutal.model.SubscribableAndUser(answer, author) from Answer answer " +
        		"join answer.question.author author " +
        		"where (answer.createdAt) > :timeAgo and author!=answer.author");
        results.addAll(query.setParameter("timeAgo", timeAgo).list());
        
        return results;
	}
}




