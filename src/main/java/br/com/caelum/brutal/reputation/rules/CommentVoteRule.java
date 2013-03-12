package br.com.caelum.brutal.reputation.rules;

import br.com.caelum.brutal.model.VoteType;

public class CommentVoteRule implements VotableRule {

	@Override
	public int calculate(VoteType type) {
		if (type != VoteType.UP) {
			throw new IllegalArgumentException("comment cannot be upvoted");
		}
		return KarmaCalculator.COMMENT_VOTED_UP;
	}

}
