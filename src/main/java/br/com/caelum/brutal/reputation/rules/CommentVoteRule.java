package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.VoteType;

public class CommentVoteRule implements VotableRule {


	@Override
	public EventType eventType(VoteType type) {
		if (type != VoteType.UP) {
			throw new IllegalArgumentException("comment cannot be downvoted");
		}
		return EventType.COMMENT_UPVOTE;
	}

}
