package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Commentable;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("rawtypes")
public class CommentDAO {

	private final Session session;

	public CommentDAO(Session session) {
		this.session = session;
	}

	public Commentable load(Class type, Long id) {
		return (Commentable) session.load(type, id);
	}

	public void save(Comment comment) {
		session.save(comment);
	}

}
