package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mamute.dao.FlaggableDAO;
import org.mamute.dto.FlaggableAndFlagCount;
import org.mamute.meta.MamuteMetaInformation;
import org.mamute.model.Answer;
import org.mamute.model.Comment;
import org.mamute.model.Flag;
import org.mamute.model.FlagType;
import org.mamute.model.LoggedUser;
import org.mamute.model.News;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.interfaces.Flaggable;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

@RunWith(MockitoJUnitRunner.class)
public class FlaggableDAOTest extends DatabaseTestCase {
	private FlaggableDAO flaggables;
	private User author = user("author author", "author@brutal.com");
	private User anyone = user("anyone", "anyone@brutal.com");
	private Comment commentWithTwo = comment();
	private Comment commentWithOneFlag = comment();
	private Comment flaggedInvisible = comment();
	private Tag java = tag("java");
	private Question question = question(author, java);
	private QuestionDAO questions;

	@Mock private MamuteMetaInformation meta;

	@Before
	public void setup() {
		flaggedInvisible.remove();
		addFlags(commentWithOneFlag, 1, author);
		addFlags(commentWithTwo, 2, author);
		addFlags(flaggedInvisible, 2, author);
		
		session.save(author);
		session.save(anyone);
		session.save(commentWithTwo);
		session.save(flaggedInvisible);
		session.save(commentWithOneFlag);
		session.save(java);
		session.save(question);
		session.flush();

		flaggables = new FlaggableDAO(session, meta);
		questions = new QuestionDAO(session, new InvisibleForUsersRule(new LoggedUser(anyone, null)));
		
		List<Class<? extends Flaggable>> moderatableTypes = new ArrayList<>();
		moderatableTypes.add(Question.class);
		moderatableTypes.add(Answer.class);
		moderatableTypes.add(Comment.class);
		moderatableTypes.add(News.class);
		when(meta.getFlaggableTypes()).thenReturn(moderatableTypes);
	}

	@Test
	public void should_turn_every_question_of_user_into_invisible(){
		Question q1 = question(author, java);
		Question q2 = question(author, java);
		Question q3 = question(anyone, java);

		session.save(q1);
		session.save(q2);
		session.save(q3);
		
		flaggables.turnAllInvisibleWith(author);

		List<Question> visibleQuestions = questions.allVisibleByIds(ImmutableList.of( q1.getId(), q2.getId(), q3.getId()));
		
		assertNotNull(visibleQuestions);
		assertEquals(1, visibleQuestions.size());
		assertEquals(q3.getId(), visibleQuestions.get(0).getId());
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
		int count = flaggables.flaggedButVisibleCount(Comment.class);
		assertEquals(2, count);
	}
	
	@Test
	public void should_get_count_of_questions_with_two_flags() throws Exception {
		Question other = question(author, java);
		addFlags(question, 10, author);
		addFlags(other, 1, author);
		session.save(java);
		session.save(question);
		session.save(other);
		int count = flaggables.flaggedButVisibleCount(Question.class);
		assertEquals(1, count);
	}
	
	@Test
	public void should_get_count_of_answers_with_two_flags() throws Exception {
		Answer flagged = answer("answer answer answer answer answer", question, author);
		Answer other = answer("answer answer answer answer answer", question, author);
		addFlags(flagged, 10, author);
		addFlags(other, 1, author);
		session.save(java);
		session.save(question);
		session.save(other);
		session.save(flagged);
		int count = flaggables.flaggedButVisibleCount(Answer.class);
		assertEquals(1, count);
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
