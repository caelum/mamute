package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.dto.CommentAndFlagCount;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.User;

public class FlaggableDAOTest extends DatabaseTestCase {

	@Test
	public void should_get_comments_with_three_flags() throws Exception {
		FlaggableDAO flaggables = new FlaggableDAO(session);
    	User author = user("author author", "author@brutal.com");
		Comment comment = comment(author, "comment comment comment comment");
		Comment other = comment(author, "comment comment comment comment");
		Flag flag1 = flag(FlagType.RUDE, author);
		Flag flag2 = flag(FlagType.RUDE, author);
		Flag flag3 = flag(FlagType.RUDE, author);
		Flag flag4 = flag(FlagType.RUDE, author);
		
		comment.add(flag1);
		comment.add(flag2);
		comment.add(flag3);
		other.add(flag4);
		
		session.save(author);
		session.save(flag1);
		session.save(flag2);
		session.save(flag3);
		session.save(flag4);
		session.save(comment);
		session.save(other);
		
		List<CommentAndFlagCount> flagged = flaggables.flagged(Comment.class, 3l);
		assertEquals(1, flagged.size());
		assertEquals(3l, flagged.get(0).getFlagCount());
		
	}


}
