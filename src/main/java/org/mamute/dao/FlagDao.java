package org.mamute.dao;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.mamute.model.Flag;
import org.mamute.model.User;

public class FlagDao {

	private Session session;

	@Deprecated
	public FlagDao() {
	}

	@Inject
	public FlagDao(Session session) {
		this.session = session;
	}
	
	public void save(Flag f) {
		session.save(f);
	}

	public boolean alreadyFlagged(User author, Long flaggableId, Class<?> flaggableType) {
		String hql = "select f from "+flaggableType.getSimpleName()+" c join c.flags f where f.author=:author and c.id=:flaggableId";
		Query query = session.createQuery(hql);
		
		Object flag = query.setParameter("author", author)
				.setParameter("flaggableId", flaggableId)
				.uniqueResult();

		return flag != null;
	}
}
