package org.mamute.reputation.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;
import org.mamute.reputation.rules.KarmaCalculator;
import org.mamute.reputation.rules.VotedAtSomethingEvent;

public class VotedAtSomethingEventTest {

	@Test
	public void should_calculate_karma_for_downvote() {
		VotedAtSomethingEvent votedAtSomething = new VotedAtSomethingEvent(new Vote(null, VoteType.DOWN), null);
		assertEquals(KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER, votedAtSomething.reward());
	}
	
	@Test
	public void should_calculate_karma_for_upvote() {
		VotedAtSomethingEvent votedAtSomething = new VotedAtSomethingEvent(new Vote(null, VoteType.UP), null);
		assertEquals(0, votedAtSomething.reward());
	}

}
