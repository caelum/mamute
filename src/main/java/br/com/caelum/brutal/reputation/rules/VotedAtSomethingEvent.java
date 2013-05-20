package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.interfaces.Votable;

public class VotedAtSomethingEvent {
	
	private final Vote vote;
	private final Votable votable;

	public VotedAtSomethingEvent(Vote vote, Votable votable) {
		this.vote = vote;
		this.votable = votable;
	}

	public int reward() {
		if (vote.isDown()) {
			return KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER;
		}
		return 0;
	}

	public ReputationEvent reputationEvent() {
		ReputationEvent downvoted = new ReputationEvent(EventType.DOWNVOTED_SOMETHING, votable.getQuestion(), vote.getAuthor());
		return vote.isDown() ? downvoted : ReputationEvent.VOID_EVENT;
	}

}
