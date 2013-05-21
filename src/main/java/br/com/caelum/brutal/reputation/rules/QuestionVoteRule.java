package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.VoteType;

public class QuestionVoteRule implements VotableRule {

	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? EventType.QUESTION_UPVOTE : EventType.QUESTION_DOWNVOTE; 
	}

}
