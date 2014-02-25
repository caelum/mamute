package org.mamute.reputation.rules;

import org.mamute.model.EventType;
import org.mamute.model.VoteType;

public class NewsVoteRule implements VotableRule {

	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? EventType.NEWS_UPVOTE : EventType.NEWS_DOWNVOTE;	}

}
