package org.mamute.reputation.rules;

import org.mamute.model.EventType;
import org.mamute.model.VoteType;

public class CommentVoteRule implements VotableRule {


	@Override
	public EventType eventType(VoteType type) {
		if (type != VoteType.UP) {
			throw new IllegalArgumentException("comment cannot be downvoted");
		}
		return EventType.COMMENT_UPVOTE;
	}

}
