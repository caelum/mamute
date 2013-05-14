package br.com.caelum.brutal.reputation.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;

public class VotedAtSomethingEventTest {

	@Test
	public void should_calculate_karma_for_downvote() {
		VotedAtSomethingEvent votedAtSomething = new VotedAtSomethingEvent(new Vote(null, VoteType.DOWN));
		assertEquals(KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER, votedAtSomething.reward());
	}
	
	@Test
	public void should_calculate_karma_for_upvote() {
		VotedAtSomethingEvent votedAtSomething = new VotedAtSomethingEvent(new Vote(null, VoteType.UP));
		assertEquals(0, votedAtSomething.reward());
	}

}
