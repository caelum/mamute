package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.ReputationEventContext;
import br.com.caelum.brutal.model.Vote;

public class VotedAtSomethingEvent {
	
	private final Vote vote;
	private final ReputationEventContext eventContext;

	public VotedAtSomethingEvent(Vote vote, ReputationEventContext eventContext) {
		this.vote = vote;
		this.eventContext = eventContext;
	}

	public int reward() {
		if (vote.isDown()) {
			return KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER;
		}
		return 0;
	}

	public ReputationEvent reputationEvent() {
		ReputationEvent downvoted = new ReputationEvent(EventType.DOWNVOTED_SOMETHING, eventContext, vote.getAuthor());
		return vote.isDown() ? downvoted : ReputationEvent.IGNORED_EVENT;
	}

}
