package br.com.caelum.brutal.controllers;

import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.auth.ModeratorOnly;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.dto.SuspectMassiveVote;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AntiHackController {

	private final VoteDAO votes;
	private final Result result;

	public AntiHackController(VoteDAO votes, Result result) {
		this.votes = votes;
		this.result = result;
	}
	
	@Get("/antihack")
	@ModeratorOnly
	public void massiveVotesForm() {}
	
	@Post("/antihack")
	@ModeratorOnly
	public void showSuspects(DateTime begin, DateTime end, VoteType voteType) {
		List<SuspectMassiveVote> suspects = votes.suspectMassiveVote(voteType, begin, end);
		result.include("voteType", voteType.toString());
		result.include("startDate", begin);
		result.include("endDate", end);
		result.include("suspects", suspects);
	}
}
