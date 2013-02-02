package br.com.caelum.brutal.dao;

import org.hibernate.Query;
import org.hibernate.Session;

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

	public void substitute(Vote previous, Vote current) {
		if (previous != null)
			session.delete(previous);
		session.save(current);
	}
}
