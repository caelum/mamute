package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerAndSubscribedUser;
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
	
	public List<AnswerAndSubscribedUser> getRecentAnswersAndSubscribedUsers(int hoursAgo) {
	    Long milisecAgo = (long) (hoursAgo * (60 * 60 * 1000));  
        DateTime timeAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        
        Query query = session.createQuery("select distinct new br.com.caelum.brutal.model.AnswerAndSubscribedUser(answer, author) from Answer answer " +
        		"join answer.question.answers ans " +
        		"join ans.author author where (answer.createdAt) > :timeAgo");
        
        query.setParameter("timeAgo", timeAgo);
        session.getTransaction().commit();
        return query.list();
	}
	

}




