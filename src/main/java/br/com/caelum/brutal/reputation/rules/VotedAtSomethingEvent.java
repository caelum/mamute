package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.Vote;

public class VotedAtSomethingEvent implements KarmaRewardEvent {
	
	private final Vote vote;

	public VotedAtSomethingEvent(Vote vote) {
		this.vote = vote;
	}

	@Override
	public int reward() {
		if (vote.isDown()) {
			return KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER;
		}
		return 0;
	}

}
