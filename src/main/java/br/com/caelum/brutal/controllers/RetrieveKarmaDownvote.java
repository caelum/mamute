package br.com.caelum.brutal.controllers;

import java.util.List;

import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;

public class RetrieveKarmaDownvote {
	public void retrieveKarma (List<Vote> votes) {
		for (Vote vote : votes) {
			if (vote.isDown()) {
				vote.getAuthor().descreaseKarma(KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER);
			}
		}
	}
}
