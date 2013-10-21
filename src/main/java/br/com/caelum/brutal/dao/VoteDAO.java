package br.com.caelum.brutal.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dto.SuspectMassiveVote;
import br.com.caelum.brutal.model.AnswerAndVotes;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.CommentsAndVotes;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.ReputationEventContext;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.brutal.model.interfaces.Commentable;
import br.com.caelum.brutal.model.interfaces.Votable;

@SuppressWarnings("rawtypes")
public class VoteDAO {
	private Session session;

	@Deprecated
	public VoteDAO() {
	}

	@Inject
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

	public List<SuspectMassiveVote> suspectMassiveVote(VoteType type, DateTime begin, DateTime end) {
		Query query = session.createQuery("select new " + SuspectMassiveVote.class.getCanonicalName() + "(av,count(av),aa) " +
				"from Answer a join a.author aa join a.votes v join v.author av " +
				"where v.type = :type and v.createdAt between :begin and :end " +
				"group by av,aa " +
				"order by count(av) desc");
		query.setParameter("type", type);
		query.setParameter("begin", begin);
		query.setParameter("end", end);
		
		return query.setMaxResults(10).list();
	}
	
	@SuppressWarnings("unchecked")
	public CommentsAndVotes previousVotesForComments(Question question, User currentUser) {
		Query answerQuery = session.createQuery("select c,v from Question as q join q.answers as a join a.comments.comments as c join c.votes as v where v.author = :author and q = :question");
		answerQuery.setParameter("author", currentUser);
		answerQuery.setParameter("question", question);
		List commentsAndVotesList = previousVotesForComments(Question.class, currentUser, question);
		commentsAndVotesList.addAll(answerQuery.list());
		return new CommentsAndVotes(commentsAndVotesList);
	}
	
	@SuppressWarnings("unchecked")
	public CommentsAndVotes previousVotesForComments(News news, User currentUser) {
		return new CommentsAndVotes(previousVotesForComments(News.class, currentUser, news));
		
	}
	
	private List previousVotesForComments(Class<? extends Commentable> clazz, User currentUser,	Commentable commentable) {
		Query newsQuery = session.createQuery("select c,v from "+clazz.getSimpleName()+" as n join n.comments.comments as c join c.votes as v where v.author = :author and n = :commentable");
		newsQuery.setParameter("author", currentUser);
		newsQuery.setParameter("commentable", commentable);
		return newsQuery.list();
	}

	//TODO: refactor?
	public ReputationEventContext contextOf(Votable votable) {
		boolean isNews = News.class.isAssignableFrom(votable.getClass());
		if (isNews) {
			return (ReputationEventContext) votable;
		}
		
		boolean isQuestionOrAnswer = !Comment.class.isAssignableFrom(votable.getClass());
		if (isQuestionOrAnswer) {
			return votable.getQuestion();
		}
		
		Question question = (Question) session
				.createQuery("select q from Question q join q.comments.comments c where c=:comment")
				.setParameter("comment", votable).uniqueResult();
		if (question != null) {
			return question;
		}
		
		question = (Question) session
				.createQuery("select a.question from Answer a join a.comments.comments c where c=:comment")
				.setParameter("comment", votable).uniqueResult();
		if (question != null) {
			return question;
		}
		
		ReputationEventContext ctx = (ReputationEventContext) session
				.createQuery("select news from News news join news.comments.comments c where c=:comment")
				.setParameter("comment", votable).uniqueResult();
		
		return ctx;
	}

    public void save(Vote vote) {
        session.save(vote);
    }

	public List<Vote> getDownVotes(Serializable id, Class type) {
		String hql = "select v from "
				+ type.getSimpleName()
				+ " q join q.votes as v where q.id = :votable and v.type = :voteType";
		Query query = session.createQuery(hql);
		return query.setParameter("votable", id)
				.setParameter("voteType", VoteType.DOWN).list();
	}
}
