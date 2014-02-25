package org.mamute.reputation.rules;

import org.mamute.model.EventType;
import org.mamute.model.VoteType;

public interface VotableRule {

	EventType eventType(VoteType type);

}
