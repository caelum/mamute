package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class QuestionDAO {
    
    private final Session session;

    public QuestionDAO(Session session) {
        this.session = session;
    }
    
    public void save(Question q) {
        session.save(q);
    }

	public Question getById(Long questionId) {
		return (Question) session.load(Question.class, questionId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Question> all() {
		return session.createQuery("from  Question order by lastUpdatedAt desc").setMaxResults(50).list();
	}
}
