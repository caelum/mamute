package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dto.FlaggableAndFlagCount;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Flaggable;

public class FlaggableDAOTest extends DatabaseTestCase {
	private FlaggableDAO flaggables;
	private User author = user("author author", "author@brutal.com");
	private Comment commentWithTwo = comment();
	private Comment commentWithOneFlag = comment();
	private Comment flaggedInvisible = comment();
	private Tag tag;
	private Question question;

	@Before
	public void setup() {
		tag = tag("java");
		question = question(author, tag);
		flaggables = new FlaggableDAO(session);
		flaggedInvisible.remove();
		addFlags(commentWithOneFlag, 1, author);
		addFlags(commentWithTwo, 2, author);
		addFlags(flaggedInvisible, 2, author);
		session.save(author);
		session.save(commentWithTwo);
		session.save(flaggedInvisible);
		session.save(commentWithOneFlag);
		session.save(tag);
		session.save(question);
	}
	
	@Test
	public void should_get_visible_comments_with_two_flags() throws Exception {
		List<FlaggableAndFlagCount> flagged = flaggables.flaggedButVisible(Comment.class);
		assertEquals(1, flagged.size());
		assertEquals(2l, flagged.get(0).getFlagCount());
	}
	
	@Test
	public void should_get_count_of_comments_with_two_flags() throws Exception {
		Comment comment = comment();
		addFlags(comment, 10, author);
		session.save(comment);
		Long count = flaggables.flaggedButVisibleCount(Comment.class);
		assertEquals(2l, count.longValue());
	}
	
	@Test
	public void should_get_count_of_questions_with_two_flags() throws Exception {
		Question other = question(author, tag);
		addFlags(question, 10, author);
		addFlags(other, 1, author);
		session.save(tag);
		session.save(question);
		session.save(other);
		Long count = flaggables.flaggedButVisibleCount(Question.class);
		assertEquals(1l, count.longValue());
	}
	
	@Test
	public void should_get_count_of_answers_with_two_flags() throws Exception {
		Answer flagged = answer("answer answer answer answer answer", question, author);
		Answer other = answer("answer answer answer answer answer", question, author);
		addFlags(flagged, 10, author);
		addFlags(other, 1, author);
		session.save(tag);
		session.save(question);
		session.save(other);
		session.save(flagged);
		Long count = flaggables.flaggedButVisibleCount(Answer.class);
		assertEquals(1l, count.longValue());
	}

	private void addFlags(Flaggable comment, int n, User author) {
		for (int i = 0; i < n; i++) {
			Flag flag = flag(FlagType.RUDE, author);
			session.save(flag);
			comment.add(flag);
		}
	}

	private Comment comment() {
		return comment(author, "comment comment comment comment");
	}

}
