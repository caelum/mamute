package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.dao.WithAuthorDAO.OrderType;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("unchecked")
public class QuestionDAO {
	
    public static final long SPAM_BOUNDARY = -5;
	private final Session session;
    private final WithAuthorDAO<Question> withAuthor;
	private final InvisibleForUsersRule invisible;

    public QuestionDAO(Session session, InvisibleForUsersRule invisible) {
        this.session = session;
		this.invisible = invisible;
		this.withAuthor = new WithAuthorDAO<Question>(session, Question.class);
    }
    
    public void save(Question q) {
        session.save(q);
    }

	public Question getById(Long questionId) {
		return (Question) session.load(Question.class, questionId);
	}
	
	public List<Question> allVisible() {
		String hql = "from Question as q join fetch q.information qi" +
				" join fetch q.author qa" +
				" join fetch q.lastTouchedBy qa" +
				" join fetch qi.tags t" +
				" left join fetch q.solution s" +
				" left join fetch q.solution.information si" +
				" "+ invisibleFilter("and") +" " + spamFilter() +" order by q.lastUpdatedAt desc";
		return session.createQuery(hql).setMaxResults(50).list();
	}

	public List<Question> unsolvedVisible() {
		return session.createQuery("from Question as q "+invisibleFilter("and")+" (q.solution is null) order by q.lastUpdatedAt desc").setMaxResults(50).list();
	}

	public Question load(Question question) {
		return getById(question.getId());
	}

	public List<Question> withTagVisible(Tag tag) {
		List<Question> questions = session.createQuery("select q from Question as q " +
				"join q.information.tags t " + 
				invisibleFilter("and") + " t = :tag order by q.lastUpdatedAt desc")
				.setParameter("tag", tag)
				.setMaxResults(50)
				.list();
		return questions;
	}
	
	public List<Question> withAuthorBy(User user, OrderType orderByWhat) {
		return withAuthor.by(user,orderByWhat);
	}

	private String spamFilter() {
		return "q.voteCount > "+SPAM_BOUNDARY;
	}
	
	private String invisibleFilter() {
		return invisibleFilter("");
	}
	
	private String invisibleFilter(String connective) {
		return invisible.getInvisibleOrNotFilter("q", connective);
	}
}

