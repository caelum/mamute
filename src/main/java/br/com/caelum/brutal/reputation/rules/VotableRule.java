package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.VoteType;

public interface VotableRule {

	EventType eventType(VoteType type);

}
