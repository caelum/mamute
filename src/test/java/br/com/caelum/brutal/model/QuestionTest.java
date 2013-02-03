package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QuestionTest {

	@Test(expected = RuntimeException.class)
	public void can_not_be_marked_as_solved_by_the_an_answer_that_is_not_mine() {
		Question shouldILiveForever = new Question("", "");
		Answer yes = new Answer("", null, null);
		shouldILiveForever.markAsSolvedBy(yes);
	}

	@Test
	public void can_be_marked_as_solved_by_the_an_answer_that_is_mine() {
		Question shouldILiveForever = new Question("", "");
		Answer yes = new Answer("my answer", shouldILiveForever, null);
		
		shouldILiveForever.markAsSolvedBy(yes);
		
		assertEquals(yes, shouldILiveForever.getSolution());
	}
	
	@Test
	public void should_be_touched_when_marked_as_solved() {
		Question shouldILiveForever = new Question("", "");
		User leo = new User("", "", "");
		Answer yes = new Answer("my answer", shouldILiveForever, leo);
		
		assertEquals(null, shouldILiveForever.getLastTouchedBy());

		shouldILiveForever.markAsSolvedBy(yes);
		
		assertEquals(leo, shouldILiveForever.getLastTouchedBy());
	}

	@Test
	public void should_remove_vote_values_and_update_vote_count() {
		Question question = new Question("", "");
		assertEquals(0l, question.getVoteCount());
		question.substitute(null, new Vote(null, VoteType.UP));
		assertEquals(1l, question.getVoteCount());
		question.substitute(new Vote(null, VoteType.UP), new Vote(null, VoteType.DOWN));
		assertEquals(-1l, question.getVoteCount());
		question.substitute(null, new Vote(null, VoteType.DOWN));
		assertEquals(-2l, question.getVoteCount());
	}

}
