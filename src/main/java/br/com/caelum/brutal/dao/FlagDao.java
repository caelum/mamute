package br.com.caelum.brutal.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FlagDao {

	private final Session session;

	public FlagDao(Session session) {
		this.session = session;
	}
	
	public void save(Flag f) {
		session.save(f);
	}

	public boolean alreadyFlagged(User author, Long commentId) {
		String hql = "select f from Comment c join c.flags f where f.author=:author and c.id=:commentId";
		Query query = session.createQuery(hql);
		
		Object flag = query.setParameter("author", author)
				.setParameter("commentId", commentId)
				.uniqueResult();

		return flag != null;
	}
}
