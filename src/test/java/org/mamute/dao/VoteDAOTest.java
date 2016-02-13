package org.mamute.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.NewsBuilder;
import org.mamute.controllers.RetrieveKarmaDownvote;
import org.mamute.dao.InvisibleForUsersRule;
import org.mamute.dao.ReputationEventDAO;
import org.mamute.dao.VoteDAO;
import org.mamute.dto.SuspectMassiveVote;
import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.CommentsAndVotes;
import org.mamute.model.LoggedUser;
import org.mamute.model.News;
import org.mamute.model.Question;
import org.mamute.model.ReputationEventContext;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;
import org.mamute.model.interfaces.Votable;
import org.mamute.model.vote.MassiveVote;
import org.mamute.model.vote.VotingMachine;
import org.mamute.reputation.rules.KarmaCalculator;

public class VoteDAOTest extends DatabaseTestCase{
	
	private VoteDAO votes;
	private User currentUser;
	private User otherUser;
	private VotingMachine votingMachine;
	private List<Tag> tags = new ArrayList<>();
	
	@Before
	public void beforeTest() {
		votes = new VoteDAO(session);
		currentUser = user("Current User", "currentUser@email.com");
		otherUser = user("Other User", "otherUser@email.com");
		session.save(otherUser);
		session.save(currentUser);
		tags.add(tag("bla"));
		for (Tag tag : tags) {
			session.save(tag);
		}
		InvisibleForUsersRule invisibleRule = new InvisibleForUsersRule(new LoggedUser(currentUser, null));
		votingMachine = new VotingMachine(votes, new KarmaCalculator(), new ReputationEventDAO(session, invisibleRule), new MassiveVote(), new RetrieveKarmaDownvote());
	}
	
	@Test
	public void should_return_right_comments_and_currentUser_votes_map() {
		
		Question question = question(currentUser, tags);
		
		Answer answer = answer("blablablablablablablablablablbalblabla", question, currentUser);
		
		Comment answerComment1 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario", DateTime.now().minusHours(1));
		Comment answerComment2 = comment(currentUser, "comentariocomentariocomentariocomentariocomentario", DateTime.now().minusHours(2));
		Comment answerComment3 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario", DateTime.now().minusHours(3));
		
		answer.add(answerComment1);
		answer.add(answerComment2);
		answer.add(answerComment3);
		
		Comment questionComment1 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario", DateTime.now().minusHours(4));
		Comment questionComment2 = comment(currentUser, "comentariocomentariocomentariocomentariocomentario", DateTime.now().minusHours(5));
		Comment questionComment3 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario", DateTime.now().minusHours(6));
		
		question.add(questionComment1);
		question.add(questionComment2);
		question.add(questionComment3);
		
		Vote currentUserUpVote1 = upvote(answerComment1, currentUser);
		Vote currentUserUpVote2 = upvote(questionComment1, currentUser);
		
		upvote(answerComment2, otherUser);
		upvote(questionComment2, otherUser);
		
		session.save(question);
		session.save(answer);
		session.save(answerComment1);
		session.save(answerComment2);
		session.save(answerComment3);
		
		CommentsAndVotes commentsAndVotes = votes.previousVotesForComments(question, currentUser);
		
		assertEquals(currentUserUpVote1, commentsAndVotes.getVotes(answerComment1));
		assertEquals(currentUserUpVote2, commentsAndVotes.getVotes(questionComment1));
		
		assertEquals(null, commentsAndVotes.getVotes(questionComment2));
		assertEquals(null, commentsAndVotes.getVotes(answerComment2));
		
		assertEquals(null, commentsAndVotes.getVotes(questionComment3));
		assertEquals(null, commentsAndVotes.getVotes(answerComment3));
	}
	
	@Test
	public void should_find_question_from_votable() throws Exception {
		Question question = question(currentUser, tags);
		Answer answer = answer("answer answer answer answer answer", question, currentUser);
		Comment comment = comment(currentUser, "blabla blabla blabla blabla blabla blabla");
		question.add(comment);
		
		session.save(question);
		session.save(answer);
		session.save(comment);
		
		assertEquals(question, votes.contextOf(question));
		assertEquals(question, votes.contextOf(answer));
		assertEquals(question, votes.contextOf(comment));
	}
	
	@Test
	public void should_find_news_from_votable() throws Exception {
		News news = new NewsBuilder().build();
		ReputationEventContext context = votes.contextOf(news);
		assertEquals(news, context);
	}
	
	@Test
	public void testName() throws Exception {
		Question question = question(currentUser, tags);
		Answer answer = answer("answer answer answer answer answer", question, currentUser);
		
		Question question2 = question(currentUser, tags);
		Answer answer2 = answer("answer answer answer answer answer", question2, currentUser);
		
		Question question3 = question(currentUser, tags);
		Answer answer3 = answer("answer answer answer answer answer", question3, currentUser);
		
		session.save(question);
		session.save(question2);
		session.save(question3);
		
		session.save(answer);
		session.save(answer2);
		session.save(answer3);

		upvote(answer, otherUser);
		upvote(answer2, otherUser);
		upvote(answer3, otherUser);
		
		List<SuspectMassiveVote> answers = votes.suspectMassiveVote(VoteType.UP, new DateTime().minusHours(1), new DateTime());
		
		assertEquals(otherUser,answers.get(0).getVoteAuthor());
		assertEquals(currentUser,answers.get(0).getAnswerAuthor());
		assertEquals(3l,answers.get(0).getMassiveVoteCount().longValue());
		
	}
	

	private Vote upvote(Votable votable, User user) {
		Vote vote = new Vote(user, VoteType.UP);
		session.save(votable);
		session.save(vote);
		votingMachine.register(votable, vote, Comment.class);
		return vote;
	}
}
