package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Answer;
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

}