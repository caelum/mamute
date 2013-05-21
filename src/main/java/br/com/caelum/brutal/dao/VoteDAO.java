package br.com.caelum.brutal.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.AnswerAndVotes;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.CommentsAndVotes;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("rawtypes")
public class VoteDAO {
	private final Session session;

	public VoteDAO(Session session) {
		this.session = session;
	}

	public Vote previousVoteFor(Serializable id, User currentUser, Class type) {
		String hql = "select v from "
				+ type.getSimpleName()
				+ " q join q.votes as v where v.author = :author and q.id = :votable";
		Query query = session.createQuery(hql);
		return (Vote) query.setParameter("author", currentUser)
				.setParameter("votable", id).setMaxResults(1).uniqueResult();
	}

	public Votable loadVotable(Class type, Long id) {
		return (Votable) session.load(type, id);
	}

	@SuppressWarnings("unchecked")
	public AnswerAndVotes previousVotesForAnswers(Question question, User currentUser) {
		Query query = session.createQuery("select a,v from Answer as a join a.votes as v where v.author = :author and a.question = :question");
		query.setParameter("author", currentUser);
		query.setParameter("question", question);
		return new AnswerAndVotes(question, question.getAnswers(), query.list());
	}
	
	@SuppressWarnings("unchecked")
	public CommentsAndVotes previousVotesForComments(Question question, User currentUser) {
		Query questionQuery = session.createQuery("select c,v from Question as q join q.comments.comments as c join c.votes as v where v.author = :author and q = :question");
		Query answerQuery = session.createQuery("select c,v from Question as q join q.answers as a join a.comments.comments as c join c.votes as v where v.author = :author and q = :question");
		questionQuery.setParameter("author", currentUser);
		answerQuery.setParameter("author", currentUser);
		questionQuery.setParameter("question", question);
		answerQuery.setParameter("question", question);
		List commentsAndVotesList = questionQuery.list();
		commentsAndVotesList.addAll(answerQuery.list());
		return new CommentsAndVotes(commentsAndVotesList);
	}
	
	public Question questionOf(Votable votable) {
		boolean isQuestionOrAnswer = !Comment.class.isAssignableFrom(votable.getClass());
		if (isQuestionOrAnswer) {
			return votable.getQuestion();
		}
		Query query = session.createQuery("select q from Question q join q.comments.comments c where c=:comment");
		return (Question) query.setParameter("comment", votable).uniqueResult();
	}

    public void save(Vote vote) {
        session.save(vote);
    }
}
