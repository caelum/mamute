package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.model.User;

public class WithAuthorDAO<T> {

	private static final Integer PAGE_SIZE = 5;
	private final Session session;
	private final Class<T> clazz;

	public WithAuthorDAO(Session session, Class<T> clazz) {
		this.session = session;
		this.clazz = clazz;
	}

	public List<T> by(User user, OrderType orderByWhat, Integer page) {
		List<T> items = withUserBy(user, orderByWhat.getOrder(), page);
		return items;		
	}
	
	@SuppressWarnings("unchecked")
	private List<T> withUserBy(User user, String order, Integer page) {
		List<T> items = session.createQuery("select p from "+ clazz.getSimpleName() +" as p join p.author a where a = :user " + order)
				.setParameter("user", user)
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(PAGE_SIZE * (page-1))
				.list();
		return items;
	}
	
	

	public Long count(User user) {
		return (Long) session.createQuery("select count(p) from "+ clazz.getSimpleName() +" as p join p.author a where a = :user ")
						.setParameter("user", user)
						.uniqueResult();
	}
	
	public long numberOfPagesTo(User user) {
		Long count = count(user);
		return calculatePages(count);
	}
	
	private long calculatePages(Long count) {
		long result = count/PAGE_SIZE.longValue();
		if (count % PAGE_SIZE.longValue() != 0) {
			result++;
		}
		return result;
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
