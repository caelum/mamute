package br.com.caelum.brutal.reputation.rules;

import static br.com.caelum.brutal.model.EventType.ANSWER_DOWNVOTE;
import static br.com.caelum.brutal.model.EventType.ANSWER_UPVOTE;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.VoteType;

public class AnswerVoteRule implements VotableRule {


	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? ANSWER_UPVOTE : ANSWER_DOWNVOTE;
	}

}
