package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class QuestionDAO {
    
    private Session session;

    public QuestionDAO(Session session) {
        this.session = session;
    }
    
    public void save(Question q) {
        session.save(q);
    }

	public Question getById(Long questionId) {
		return (Question) session.load(Question.class, questionId);
	}

}
