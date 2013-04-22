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

	public List<T> by(User user, OrderType orderByWhat) {
		List<T> items = fiveWithUserBy(user, orderByWhat.getOrder());
		return items;		
	}
	
	@SuppressWarnings("unchecked")
	private List<T> fiveWithUserBy(User user, String order) {
		List<T> items = session.createQuery("select p from "+ clazz.getSimpleName() +" as p join p.author a where a = :user " + order)
				.setParameter("user", user)
				.setMaxResults(5)
				.list();
		return items;
	}

	public Long count(User user) {
		return (Long) session.createQuery("select count(p) from "+ clazz.getSimpleName() +" as p join p.author a where a = :user ")
						.setParameter("user", user)
						.uniqueResult();
	}
	
	public enum OrderType {
		ByDate {
			@Override
			public String getOrder() {
				return "order by p.createdAt desc";
			}
		}, ByVotes() {
			@Override
			public String getOrder() {
				return "order by p.voteCount desc";
			}
		};

		public abstract String getOrder();
	}


}
