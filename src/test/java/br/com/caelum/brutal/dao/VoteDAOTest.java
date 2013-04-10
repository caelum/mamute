package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.CommentsAndVotes;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.brutal.model.VotingMachine;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;

public class VoteDAOTest extends DatabaseTestCase{
	
	private VoteDAO votes;
	private User currentUser;
	private User otherUser;
	private VotingMachine votingMachine;
	
	@Before
	public void beforeTest() {
		votes = new VoteDAO(session);
		currentUser = user("Current User", "currentUser@caelum.com");
		otherUser = user("Other User", "otherUser@caelum.com");
		session.save(otherUser);
		session.save(currentUser);
		votingMachine = new VotingMachine(votes, new KarmaCalculator());
	}
	
	@Test
	public void should_return_right_comments_and_currentUser_votes_map() {
		
		List<Tag> tags = new ArrayList<>();
		tags.add(tag("bla"));
		
		Question question = question(currentUser, tags);
		
		Answer answer = answer("blablablablablablablablablablbalblabla", question, currentUser);
		
		question.add(answer);
		
		Comment answerComment1 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario");
		Comment answerComment2 = comment(currentUser, "comentariocomentariocomentariocomentariocomentario");
		Comment answerComment3 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario");
		
		answer.add(answerComment1);
		answer.add(answerComment2);
		answer.add(answerComment3);
		
		Comment questionComment1 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario");
		Comment questionComment2 = comment(currentUser, "comentariocomentariocomentariocomentariocomentario");
		Comment questionComment3 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario");
		
		question.add(questionComment1);
		question.add(questionComment2);
		question.add(questionComment3);
		
		
		Vote currentUserUpVote1 = upvote(answerComment1, currentUser);
		Vote currentUserUpVote2 = upvote(questionComment1, currentUser);
		
		upvote(answerComment2, otherUser);
		upvote(questionComment2, otherUser);
		
		for (Tag tag : tags) {
			session.save(tag);
		}
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

	private Vote upvote(Comment comment, User user) {
		Vote vote = new Vote(user, VoteType.UP);
		session.save(vote);
		votingMachine.register(comment, vote, Comment.class);
		return vote;
	}

}
