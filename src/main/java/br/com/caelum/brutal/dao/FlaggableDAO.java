package br.com.caelum.brutal.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.dto.CommentAndFlagCount;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FlaggableDAO {

	private final Session session;

	public FlaggableDAO(Session session) {
		this.session = session;
	}
	
	public Flaggable getById(Long flaggableId, Class<?> clazz) {
		Flaggable flaggable = (Flaggable) session.createQuery("from "+clazz.getSimpleName()+" where id = :id")
				.setLong("id", flaggableId)
				.uniqueResult();
		return flaggable;
	}
	
	@SuppressWarnings("unchecked")
	public List<CommentAndFlagCount> flagged(Class<?> model, Long minFlagCount) {
		String dtoName = CommentAndFlagCount.class.getName();
		String hql = "select new " + dtoName + "(comment, count(flags)) from " + model.getSimpleName() + " comment left join comment.flags flags group by comment having count(flags) >= :min";
		Query query = session.createQuery(hql);
		return query.setParameter("min", minFlagCount).list();
	}

}
