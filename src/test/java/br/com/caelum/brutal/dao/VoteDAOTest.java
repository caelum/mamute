package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		
		Comment comment1 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario");
		Comment comment2 = comment(currentUser, "comentariocomentariocomentariocomentariocomentario");
		Comment comment3 = comment(otherUser, "comentariocomentariocomentariocomentariocomentario");
		
		answer.add(comment1);
		answer.add(comment2);
		answer.add(comment3);
		
		
		for (Tag tag : tags) {
			session.save(tag);
		}
		session.save(question);
		session.save(answer);
		session.save(comment1);
		session.save(comment2);
		session.save(comment3);
		
		upvote(comment1, currentUser);
		upvote(comment2, otherUser);
		
		assertTrue(comment1.getVoteCount() == 1l);
		assertTrue(comment2.getVoteCount() == 1l);
		
		CommentsAndVotes previousVotesForComments = votes.previousVotesForComments(answer, currentUser);
		Map<Comment, Vote> mapaLoko = previousVotesForComments.getVotes();
		
		assertEquals(null, mapaLoko.get(comment2));
		assertTrue(mapaLoko.containsKey(comment2));
		
		assertEquals(null, mapaLoko.get(comment3));
		assertTrue(mapaLoko.containsKey(comment3));
		
		assertTrue(mapaLoko.get(comment1) instanceof Vote);
		assertEquals(1, mapaLoko.get(comment1).getCountValue());
		assertEquals(currentUser, mapaLoko.get(comment1).getAuthor());
		
	}

	private void upvote(Comment comment, User user) {
		Vote vote = new Vote(user, VoteType.UP);
		session.save(vote);
		votingMachine.register(comment, vote, Comment.class);
	}

}
