package org.mamute.reputation.rules;

import static org.mamute.model.EventType.ANSWER_DOWNVOTE;
import static org.mamute.model.EventType.ANSWER_UPVOTE;

import org.mamute.model.EventType;
import org.mamute.model.VoteType;

public class AnswerVoteRule implements VotableRule {


	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? ANSWER_UPVOTE : ANSWER_DOWNVOTE;
	}

}
