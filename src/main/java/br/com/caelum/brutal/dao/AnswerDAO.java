package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AnswerDAO {

	private final Session session;

	public AnswerDAO(Session session) {
		this.session = session;
	}

	public void save(Answer answer) {
		this.session.save(answer);
	}

	public void create(Answer answer, Question question, User author) {
		Question questionLoaded = (Question)session.load(Question.class, question.getId());
		answer.setQuestion(questionLoaded);
		
		User authorLoaded = (User)session.load(User.class, author.getId());
		answer.setAuthor(authorLoaded);
		
		
		
		save(answer);
	}
}
