package br.com.caelum.brutal.migration.all;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.util.ScriptSessionFactoryCreator;

public class InsertQuestionInteraction {
	
	private ScriptSessionFactoryCreator sessionProvider = new ScriptSessionFactoryCreator();
	private Session session;
	
	public InsertQuestionInteraction() {
		session = sessionProvider.getSession();
	}
	
	public static void main(String[] args) {
		new InsertQuestionInteraction().run();
	}

	private void run() {
		try {
			session.beginTransaction();
			
			List<Question> list = session.createCriteria(Question.class).list();
			for (Question question : list) {
				List<User> userAnswer = session.createQuery("select a.author from Answer a where a.question = :question").setParameter("question", question).list();
				question.addUserInteractions(userAnswer);
				
				List<User> userComment = session.createQuery("select comments.author from Question q join q.comments.comments comments where q = :question").setParameter("question", question).list();
				question.addUserInteractions(userComment);
				
				List<User> userVote = session.createQuery("select votes.author from Question q join q.votes votes where q = :question").setParameter("question", question).list();
				question.addUserInteractions(userVote);
				
				List<User> userVoteAnswer = session.createQuery("select votes.author from Answer a join a.votes votes where a.question = :question").setParameter("question", question).list();
				question.addUserInteractions(userVoteAnswer);
				
				question.addUserInteraction(question.getAuthor());
			}
			
			session.getTransaction().commit();
		}catch (Exception e) {
			session.getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}
}
