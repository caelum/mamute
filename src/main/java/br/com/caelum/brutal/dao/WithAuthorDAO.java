package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.model.User;

public class WithAuthorDAO<T> {

	private final Session session;
	private final Class<T> clazz;

	public WithAuthorDAO(Session session, Class<T> clazz) {
		this.session = session;
		this.clazz = clazz;
	}

	public List<T> byVotes(User user) {
		List<T> items = fiveWithUserBy(user, "order by q.voteCount desc");
		return items;		
	}
	
	public List<T> byDate(User user) {
		List<T> items = fiveWithUserBy(user, "order by q.createdAt desc");
		return items;
	}

	private List<T> fiveWithUserBy(User user, String order) {
		List<T> items = session.createQuery(selectWithUser()+order)
				.setParameter("user", user)
				.setMaxResults(5)
				.list();
		return items;
	}
	

	private String selectWithUser(){
		return "select q from "+ clazz.getSimpleName() +" as q join q.author a where a = :user ";
	}

}
