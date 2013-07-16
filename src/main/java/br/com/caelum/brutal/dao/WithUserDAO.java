package br.com.caelum.brutal.dao;

import static org.hibernate.criterion.Projections.rowCount;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.brutal.model.User;

public class WithUserDAO<T> {

	private static final Integer PAGE_SIZE = 5;
	private final Session session;
	private final Class<T> clazz;
	private final String role;
	private final QueryFilter generalFilter;
	
	public WithUserDAO(Session session, Class<T> clazz, UserRole role) {
		this(session, clazz, role, null);
	}

	public WithUserDAO(Session session, Class<T> clazz, UserRole role, QueryFilter generalFilter) {
		this.session = session;
		this.clazz = clazz;
		this.generalFilter = generalFilter;
		this.role = role.getRole();
	}

	@SuppressWarnings("unchecked")
	public List<T> by(User user, OrderType orderByWhat, Integer page) {
		Criteria criteria = defaultCriteria(user)
				.addOrder(orderByWhat.getOrder())
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(PAGE_SIZE * (page-1));
		return addFilter(criteria).list();
	}

	public Long count(User user) {
		Criteria criteria = defaultCriteria(user)
				.setProjection(rowCount());
		return (Long) addFilter(criteria).list().get(0);
	}

	private Criteria defaultCriteria(User user) {
		Criteria criteria = session.createCriteria(clazz, "p")
				.createAlias("p." + role, "r")
				.add(Restrictions.eq("r.id", user.getId()))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria;
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

	private Criteria addFilter(Criteria criteria) {
		if(generalFilter != null) criteria = generalFilter.addFilter("p", criteria);
		return criteria;
	}
	
	public static enum OrderType {
		ByDate {
			@Override
			public Order getOrder() {
				return Order.desc("p.createdAt");
			}
		}, ByVotes() {
			@Override
			public Order getOrder() {
				return Order.desc("p.voteCount");
			}
		};

		public abstract Order getOrder();
	}
	
	public static enum UserRole {
		AUTHOR {
			@Override
			public String getRole() {
				return "author";
			}
		},
		WATCHER {
			@Override
			public String getRole() {
				return "watcher";
			}
		};
		
		public abstract String getRole();
	}
	
}

