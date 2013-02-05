package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.CommentAndSubscribedUser;
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
	
	@SuppressWarnings("unchecked")
    public List<CommentAndSubscribedUser> getRecentAnswersAndSubscribedUsers(int hoursAgo) {
        Long milisecAgo = (long) (hoursAgo * (60 * 60 * 1000));  
        DateTime timeAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        
        Query query = session.createQuery("select distinct new br.com.caelum.brutal.model.CommentAndSubscribedUser(comment, author) from Question question " +
                "join question.comments comment " +
                "join question.answers answer " +
                "join answer.author author " +
                "where (comment.createdAt) > :timeAgo");
        List<CommentAndSubscribedUser> results = query.setParameter("timeAgo", timeAgo).list();
        
        query = session.createQuery("select distinct new br.com.caelum.brutal.model.CommentAndSubscribedUser(comment, author) from Question question " +
        		"join question.author author " +
        		"join question.comments comment " +
        		"join comment.author comment_author " +
        		"where (comment.createdAt) > :timeAgo and author != comment_author");
        results.addAll(query.setParameter("timeAgo", timeAgo).list());
        
        return results;
    }

}
