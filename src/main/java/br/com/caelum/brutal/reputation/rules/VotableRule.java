package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.VoteType;

public interface VotableRule {

	int calculate(VoteType type);

}
