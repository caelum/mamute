package org.mamute.reputation.rules;

import org.mamute.model.EventType;
import org.mamute.model.VoteType;

public class QuestionVoteRule implements VotableRule {

	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? EventType.QUESTION_UPVOTE : EventType.QUESTION_DOWNVOTE; 
	}

}
