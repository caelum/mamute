package br.com.caelum.brutal.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.AnswerAndVotes;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Votable;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("rawtypes")
public class VoteDAO {
	private final Session session;

	public VoteDAO(Session session) {
		this.session = session;
	}

	public Vote previousVoteFor(Long id, User currentUser, Class type) {
		String hql = "select v from "
				+ type.getSimpleName()
				+ " q join q.votes as v where v.author = :author and q.id = :votable";
		Query query = session.createQuery(hql);
		return (Vote) query.setParameter("author", currentUser)
				.setParameter("votable", id).setMaxResults(1).uniqueResult();
	}

	public Votable loadVotedOnFor(Class type, Long id) {
		return (Votable) session.load(type, id);
	}

	public void substitute(Vote previous, Vote current, Votable on) {
	    long delta = current.getValue();
	    if (previous != null) {
	        delta -= previous.getValue();
	        session.delete(previous);
	    }
		session.save(current);
		session.createQuery("update User as u set u.karma = u.karma + (:value) where u.id = :user")
		    .setParameter("value", delta)
		    .setParameter("user", on.getAuthor().getId())
		    .executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public AnswerAndVotes previousVotesForAnswers(Question question, User currentUser) {
		Query query = session.createQuery("select a,v from Answer as a join a.votes as v where v.author = :author and a.question = :question");
		query.setParameter("author", currentUser);
		query.setParameter("question", question);
		return new AnswerAndVotes(question, question.getAnswers(), query.list());
	}
}
