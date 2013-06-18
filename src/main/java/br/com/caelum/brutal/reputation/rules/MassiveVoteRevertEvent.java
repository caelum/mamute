package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.User;

public class MassiveVoteRevertEvent {

	private int karma;
	private final User target;
	
	public MassiveVoteRevertEvent(int karma, User target) {
		this.karma = karma;
		this.target = target;
	}
	
	public ReputationEvent reputationEvent() {
		EventType massiveVoteReverted = EventType.MASSIVE_VOTE_REVERTED;
		massiveVoteReverted.setKarma(karma);
		return new ReputationEvent(massiveVoteReverted, null, target);
	}
}
