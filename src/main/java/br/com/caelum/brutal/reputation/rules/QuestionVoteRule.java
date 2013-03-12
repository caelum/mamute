package br.com.caelum.brutal.reputation.rules;

import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.QUESTION_VOTED_DOWN;
import static br.com.caelum.brutal.reputation.rules.KarmaCalculator.QUESTION_VOTED_UP;
import br.com.caelum.brutal.model.VoteType;

public class QuestionVoteRule implements VotableRule {

	@Override
	public int calculate(VoteType type) {
		return type == VoteType.UP ? QUESTION_VOTED_UP : QUESTION_VOTED_DOWN; 
	}

}
