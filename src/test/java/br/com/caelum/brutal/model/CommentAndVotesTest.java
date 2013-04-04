package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;

public class CommentAndVotesTest extends TestCase{
	
	@Test
	public void should_fill_value_with_null_only_comments_that_has_no_currentUserVote() {
		User zeh = user("Zé", "x@x.com", 1l);
		Object[] comment1 = new Object[]{comment(zeh, "blablablablablabla"), mock(Vote.class)};
		Object[] comment2 = new Object[]{comment(zeh, "blablablablablabla"), mock(Vote.class)};
		
		Comment comment3 = comment(zeh, "blablablablablabla");
		
		List<Comment> answers = new ArrayList<>();
		answers.add((Comment) comment1[0]);
		answers.add((Comment) comment2[0]);
		answers.add(comment3);
		
		List<Object[]> commentsWithVotes = new ArrayList<>();
		commentsWithVotes.add(comment1);
		commentsWithVotes.add(comment2);
		
		Map<Comment, Vote> mapaDoido = new CommentsAndVotes(answers, commentsWithVotes).getVotes();
		assertEquals(comment1[1], mapaDoido.get(comment1[0]));
		assertEquals(comment2[1], mapaDoido.get(comment2[0]));
		assertEquals(null, mapaDoido.get(comment3));
		assertTrue(mapaDoido.containsKey(comment3));
	}

}
