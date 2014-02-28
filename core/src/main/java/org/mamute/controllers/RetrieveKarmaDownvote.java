package org.mamute.controllers;

import java.util.List;

import org.mamute.model.Vote;
import org.mamute.reputation.rules.KarmaCalculator;

public class RetrieveKarmaDownvote {
	public void retrieveKarma (List<Vote> votes) {
		for (Vote vote : votes) {
			if (vote.isDown()) {
				vote.getAuthor().descreaseKarma(KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER);
			}
		}
	}
}
