package org.mamute.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.mamute.model.Comment;
import org.mamute.model.Post;

public class CommentDAO {

	private Session session;

	@Deprecated
	public CommentDAO() {
	}

	@Inject
	public CommentDAO(Session session) {
		this.session = session;
	}

	public Post loadCommentable(Class<?> type, Long id) {
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
