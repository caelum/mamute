package org.mamute.controllers;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.VoteDAO;
import org.mamute.dto.SuspectMassiveVote;
import org.mamute.model.VoteType;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;

@Controller
public class AntiHackController {

	@Inject private VoteDAO votes;
	@Inject private Result result;
	
	@Get("/antihack")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void massiveVotesForm() {}
	
	@Post("/antihack")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void showSuspects(DateTime begin, DateTime end, VoteType voteType) {
		List<SuspectMassiveVote> suspects = votes.suspectMassiveVote(voteType, begin, end);
		result.include("voteType", voteType.toString());
		result.include("startDate", begin);
		result.include("endDate", end);
		result.include("suspects", suspects);
	}
}
