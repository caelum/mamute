package org.mamute.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.UpdateStatus.PENDING;
import static org.mockito.Mockito.mock;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mamute.brutauth.auth.rules.EnvironmentKarma;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.Answer;
import org.mamute.model.Flag;
import org.mamute.model.FlagType;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.QuestionInformationBuilder;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;
import org.mamute.vraptor.environment.MamuteEnvironment;

import javax.servlet.ServletContext;
import java.io.IOException;

public class QuestionTest  extends TestCase{
	private QuestionBuilder question = new QuestionBuilder();
	private Updater updater;

	@Before
	public void setup() throws IOException {
		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new MamuteEnvironment(ctx));
		this.updater = new Updater(env);
	}

	@Test(expected = RuntimeException.class)
	public void can_not_be_marked_as_solved_by_the_an_answer_that_is_not_mine() {
		Question shouldILiveForever = question.build();
		Answer yes = answer("", null, null);
		shouldILiveForever.markAsSolvedBy(yes);
	}

	@Test
	public void can_be_marked_as_solved_by_the_an_answer_that_is_mine() {
		Question shouldILiveForever = question.build();
		Answer yes = answer("my answer", shouldILiveForever, null);
		
		shouldILiveForever.markAsSolvedBy(yes);
		
		assertEquals(yes, shouldILiveForever.getSolution());
	}
	
	@Test
	public void should_not_be_touched_when_marked_as_solved() {
		Question shouldILiveForever = question.build();
		User touchedBy = shouldILiveForever.getLastTouchedBy();
		User leo = user("", "");
		Answer yes = answer("my answer", shouldILiveForever, leo);
		
		assertEquals(User.GHOST, shouldILiveForever.getLastTouchedBy());

		shouldILiveForever.markAsSolvedBy(yes);
		
		assertEquals(touchedBy, shouldILiveForever.getLastTouchedBy());
	}

	@Test
	public void should_remove_vote_values_and_update_vote_count() {
		Question myQuestion = question.build();
		assertEquals(0l, myQuestion.getVoteCount());
		Vote firstVote = new Vote(null, VoteType.UP);
		myQuestion.substitute(null, firstVote);
		assertEquals(1l, myQuestion.getVoteCount());
		myQuestion.substitute(firstVote, new Vote(null, VoteType.DOWN));
		assertEquals(-1l, myQuestion.getVoteCount());
		myQuestion.substitute(null, new Vote(null, VoteType.DOWN));
		assertEquals(-2l, myQuestion.getVoteCount());
	}
	
	@Test
	public void should_be_touched_by_original_author_after_edit() throws Exception {
		User artur = user("artur", "artur@x.com");
		artur.setId(1l);
		Question comoFaz = question.withTitle("titulo").withDescription("descricao").withAuthor(artur).build();
		User leo = user("leo", "leo@x.com");
		leo.setId(2l);
		QuestionInformation info = new QuestionInformationBuilder().with(leo).build();
		comoFaz.updateWith(info, updater);
		assertEquals(comoFaz.getLastTouchedBy().getId(), artur.getId());
	}
	
	@Test
	public void should_update_information_status_and_last_touched_by() throws Exception {
		User artur = user("", "");
		User leo = user("", "");
		artur.setId(1l);
		leo.setId(2l);
		
		Question comoFaz = question.withTitle("titulo").withDescription("descricao").withAuthor(artur).build();
		QuestionInformation comoFazEditedInformation = new QuestionInformationBuilder().with(leo).build();
		comoFaz.updateWith(comoFazEditedInformation, updater);
		comoFaz.approve(comoFazEditedInformation);
		
		assertEquals(comoFaz.getLastTouchedBy().getId(), leo.getId());
	}
	
	@Test
	public void should_set_author_only_once() throws Exception {
		Question q = question.build();
		User original = user("original", "original@brutal.com");
		q.setAuthor(original);
		User other = user("other", "other@brutal.com");
		q.setAuthor(other);
		
		assertEquals(original, q.getAuthor());
	}
	
	@Test
	public void should_verify_that_a_user_already_flagged_question() throws Exception {
		Question q = question.build();
		User author = user("author", "author@brutal.com", 1l);
		User other = user("other", "other@brutal.com", 2l);
		Flag flag = flag(FlagType.OTHER, author);
		q.add(flag);
		
		assertTrue(q.alreadyFlaggedBy(author));
		assertFalse(q.alreadyFlaggedBy(other));
	}
	
	@Test
	public void should_verify_that_a_user_already_answered_question() throws Exception {
		Question q = question.build();
		User author = user("author", "author@brutal.com", 1l);
		User other = user("other", "other@brutal.com", 2l);
		answer("my answer", q, author);
		
		assertTrue(q.alreadyAnsweredBy(author));
		assertFalse(q.alreadyAnsweredBy(other));
	}
	
	@Test
	public void should_update_information_and_tag_usage_count() throws Exception {
		Tag ruby = tag("ruby");
		Tag java = tag("java");
		Question q = question.withTag(ruby).build();
		QuestionInformation approved = new QuestionInformationBuilder().withTag(java).build();
		q.updateApproved(approved);
		
		assertEquals(0l, ruby.getUsageCount().longValue());
		assertEquals(1l, java.getUsageCount().longValue());
	}
	
	@Test
	public void should_return_true_if_question_has_pending_edits() throws Exception {
		Tag ruby = tag("ruby");
		Tag java = tag("java");
		Question q = question.withTag(ruby).build();
		assertFalse(q.hasPendingEdits());

		QuestionInformation approved = new QuestionInformationBuilder().withTag(java).build();
		q.enqueueChange(approved, PENDING);
		
		assertTrue(q.hasPendingEdits());
	}

	@Test
	public void should_verify_if_is_visible_for_author() {
		User author = user("leo", "leo@leo");
		Question shouldILiveForever = question.withAuthor(author).build();
		shouldILiveForever.remove();
		boolean isVisibleForAuthor = shouldILiveForever.isVisibleFor(author);
		assertTrue(isVisibleForAuthor);
	}
	
	@Test
	public void should_verify_if_is_visible_for_moderator() {
		Question shouldILiveForever = question.build();
		shouldILiveForever.remove();
		boolean isVisibleForModerator = shouldILiveForever.isVisibleFor(user("leo", "leo@leo").asModerator());
		assertTrue(isVisibleForModerator);
	}
	
	@Test
	public void should_verify_if_is_visible_for_null() {
		Question shouldILiveForever = question.build();
		shouldILiveForever.remove();
		boolean isVisibleForNull = shouldILiveForever.isVisibleFor(null);
		assertFalse(isVisibleForNull);
	}
	
	@Test
	public void should_verify_if_is_visible_when_visible() {
		Question shouldILiveForever = question.build();
		boolean isVisibleForModerator = shouldILiveForever.isVisibleFor(null);
		assertTrue(isVisibleForModerator);
	}
	
	@Test
	public void should_not_trim_short_meta() throws Exception {
		String title = "0123456789";
		String description = "description";
		Question q = question
				.withTitle(title)
				.withDescription(description)
				.build();
		assertTrue(q.getMetaDescription().contains(title));
		assertTrue(q.getMetaDescription().contains(description));
	}
	
	@Test
	public void should_trim_big_meta() throws Exception {
		String title = bigString('a', 100);
		String description = bigString('b', 10000);
		Question q = question
				.withTitle(title)
				.withDescription(description)
				.build();
		assertTrue(q.getMetaDescription().contains(title));
		assertFalse(q.getMetaDescription().contains(description));
	}
	
	

	@Test
	public void return_true_if_answer_was_last_touched_two_months_ago() throws Exception {
		Question inactiveQuestion = TimeMachine.goTo(new DateTime().minusMonths(2)).andExecute(new Block<Question>() {
			@Override
			public Question run() {
				return question.withTitle("question title").withDescription("description").build();
			}
		});
		
		assertTrue(inactiveQuestion.isInactiveForOneMonth());
	}
	
	@Test
	public void return_false_if_answer_was_last_touched_today() throws Exception {
		final Question myQuestion = question.withTitle("question title").withDescription("description").build();
		
		assertFalse(myQuestion.isInactiveForOneMonth());
	}

	@Test
	public void should_verify_show_mark_as_solution_rules_for_author() throws Exception {
		User author = user("Fernanda", "bla@bla.com", 1l);
		Question question = question(author);
		assertFalse(question.canMarkAsSolution(author));
		question.add(answer("", question, author));
		assertTrue(question.canMarkAsSolution(author));
	}
	
	@Test
	public void should_return_false_if_is_author_and_there_is_solution() throws Exception {
		User author = user("Fernanda", "bla@bla.com", 1l);
		Question question = question(author);
		Answer answer = answer("", question, author);
		question.markAsSolvedBy(answer);
		assertFalse(question.canMarkAsSolution(author));
	}
	
	@Test
	public void should_return_false_if_is__not_author() throws Exception {
		User author = user("Fernanda", "bla@bla.com", 1l);
		User user = user("Chico", "bla@b.com", 2L);
		Question question = question(author);
		assertFalse(question.canMarkAsSolution(user));
		Answer answer = answer("", question, author);
		question.markAsSolvedBy(answer);
		assertFalse(question.canMarkAsSolution(user));
	}
	
	private String bigString(char c, int repetitions) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < repetitions; i++) {
			builder.append(c);
		}
		return builder.toString();
	}
}
