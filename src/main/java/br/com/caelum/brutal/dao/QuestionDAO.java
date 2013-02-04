package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("unchecked")
public class QuestionDAO {
	
    private final Session session;

	private final User currentLoggedUser;

    public QuestionDAO(Session session, User currentLoggedUser) {
        this.session = session;
		this.currentLoggedUser = currentLoggedUser;
    }
    
    public void save(Question q) {
        session.save(q);
    }

	public Question getById(Long questionId) {
		return (Question) session.load(Question.class, questionId);
	}
	
	public List<Question> all() {
		String hql = "from Question as q where " + spamFilter() + " order by lastUpdatedAt desc";
		return session.createQuery(hql).setMaxResults(50).list();
	}

	private String spamFilter() {
		String content = "q.voteCount > ";
		int value = (currentLoggedUser != null && currentLoggedUser.isModerator()) ? -10 : -5;
		return content + value;
	}

	public List<Question> unanswered() {
		return session.createQuery("from Question as  q where " + spamFilter() + " and (q.solution is null) order by q.lastUpdatedAt desc").setMaxResults(50).list();
	}

	public Question load(Question question) {
		return getById(question.getId());
	}

	public List<Question> withTag(Tag tag) {
		List<Question> questions = session.createQuery("select q from Question as q join q.tags t where " + spamFilter() + " and (q.solution is null) and t = :tag order by q.lastUpdatedAt desc")
				.setParameter("tag", tag)
				.setMaxResults(50)
				.list();
		return questions;
	}

}
