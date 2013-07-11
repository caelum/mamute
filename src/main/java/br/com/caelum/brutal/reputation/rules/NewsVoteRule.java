package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.VoteType;

public class NewsVoteRule implements VotableRule {

	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? EventType.NEWS_UPVOTE : EventType.NEWS_DOWNVOTE;	}

}
