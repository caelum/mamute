package org.mamute.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.Comment;
import org.mamute.model.CommentsAndVotes;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;

public class CommentAndVotesTest extends TestCase{
	
	@Test
	public void should_fill_value_with_null_only_comments_that_has_no_currentUserVote() {
		User zeh = user("Zé", "x@x.com", 1l);
		Vote vote1 = vote(null, VoteType.UP, 1l);
		Vote vote2 = vote(null, VoteType.UP, 2l);
		
		Comment comment1 = comment(zeh, "blablablablablabla");
		Comment comment2 = comment(zeh, "blablablablablabla");
		Object[] commentAndVote1 = new Object[]{comment1, vote1};
		Object[] commentAndVote2 = new Object[]{comment2, vote2};
		
		List<Object[]> commentsWithVotes = Arrays.asList(commentAndVote1, commentAndVote2);
		
		CommentsAndVotes commentsAndVotes = new CommentsAndVotes(commentsWithVotes);
		
		assertEquals(vote1, commentsAndVotes.getVotes(comment1));
		assertEquals(vote2, commentsAndVotes.getVotes(comment2));
	}

}
