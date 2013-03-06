package br.com.caelum.brutal.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.dao.TestCase;

public class CommentTest extends TestCase {

	@Test
	public void should_verify_that_user_flagged_a_comment() {
		User author = user("name", "email@email", 1l);
		User other = user("other", "other@brutal.com", 2l);
		User commentAuthor = user("name", "email@email", 1l);
		Comment comment = comment(commentAuthor, "comment");
		
		Flag flag = new Flag(FlagType.RUDE, author);
		comment.add(flag);
		
		assertTrue(comment.alreadyFlaggedBy(author));
		assertFalse(comment.alreadyFlaggedBy(other));
	}

}
