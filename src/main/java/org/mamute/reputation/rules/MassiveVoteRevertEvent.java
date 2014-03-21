package org.mamute.reputation.rules;

import org.mamute.model.EventType;
import org.mamute.model.ReputationEvent;
import org.mamute.model.User;

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
