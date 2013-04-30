package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Post;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("rawtypes")
public class CommentDAO {

	private final Session session;

	public CommentDAO(Session session) {
		this.session = session;
	}

	public Post load(Class type, Long id) {
		return (Post) session.load(type, id);
	}

	public void save(Comment comment) {
		session.save(comment);
	}
	
	public Comment getById(Long id) {
		return (Comment) session.load(Comment.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Comment> flagged(Long minFlagCount) {
		String hql = "select comment from Comment comment left join comment.flags flags group by comment having count(flags) >= :min";
		Query query = session.createQuery(hql);
		return query.setParameter("min", minFlagCount).list();
	}

}
