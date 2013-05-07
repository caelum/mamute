package br.com.caelum.brutal.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.dto.FlaggableAndFlagCount;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FlaggableDAO {

	private static final long MIN_FLAGS = 2l;
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
	public List<FlaggableAndFlagCount> flaggedButVisible(Class<?> model) {
		String dtoName = FlaggableAndFlagCount.class.getName();
		String hql = "select new " + dtoName + "(flaggable, count(flags)) from " + model.getSimpleName() + " flaggable " +
				"left join flaggable.flags flags " +
				"where flaggable.moderationOptions.invisible = false " +
				"group by flaggable " +
				"having count(flags) >= :min ";
		Query query = session.createQuery(hql);
		return query.setParameter("min", MIN_FLAGS).list();
	}
	
	public Long flaggedButVisibleCount() {
		Long comments = flaggedButVisibleCount(Comment.class);
		Long questions = flaggedButVisibleCount(Question.class);
		Long answers = flaggedButVisibleCount(Answer.class);
		return comments + questions + answers;
	}

	Long flaggedButVisibleCount(Class<? extends Flaggable> model) {
		String hql = "select count(*) from "+model.getSimpleName()+" c where c in (select flaggable from " + model.getSimpleName() + " flaggable " +
				"left join flaggable.flags flags " +
				"where flaggable.moderationOptions.invisible = false " +
				"group by flaggable " +
				"having count(flags) >= :min)";
		Query query = session.createQuery(hql);
		return (Long) query.setParameter("min", MIN_FLAGS).uniqueResult();
	}


}
