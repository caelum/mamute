package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("unchecked")
public class QuestionDAO implements VotableDAO {
    
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
	
	public List<Question> all() {
		return session.createQuery("from Question order by lastUpdatedAt desc").setMaxResults(50).list();
	}

	public List<Question> unanswered() {
		return session.createQuery("from Question as  q where (q.solution is null) order by q.lastUpdatedAt desc").setMaxResults(50).list();
	}

	public Question load(Question question) {
		return getById(question.getId());
	}

	public List<Question> withTag(Tag tag) {
		List<Question> questions = session.createQuery("select q from Question as q join q.tags t where (q.solution is null) and t = :tag order by q.lastUpdatedAt desc")
				.setParameter("tag", tag)
				.setMaxResults(50)
				.list();
		return questions;
	}
	
	@Override
    public boolean alreadyVoted(Long questionId, User author, VoteType type) {
        Query query = session.createQuery("select v from Question q " +
        		"join q.votes v " +
        		"where q.id=:questionId and v.author.id=:authorId and v.type=:type");
        query.setParameter("type", type);
        query.setParameter("authorId", author.getId());
        query.setParameter("questionId", questionId);
        boolean voted = !query.list().isEmpty();
        return voted;
    }
}



