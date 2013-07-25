package br.com.caelum.brutal.controllers;

import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.dto.SuspectMassiveVote;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.brutal.reputation.rules.MassiveVoteRevertEvent;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AntiHackController {

	private final VoteDAO votes;
	private final Result result;
	private final ReputationEventDAO reputationEvents;
	private final KarmaCalculator karmaCalculator;
	private final UserDAO users;

	public AntiHackController(VoteDAO votes, Result result, ReputationEventDAO reputationEvents, KarmaCalculator karmaCalculator, UserDAO users) {
		this.votes = votes;
		this.result = result;
		this.reputationEvents = reputationEvents;
		this.karmaCalculator = karmaCalculator;
		this.users = users;
		
	}
	
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
	
	@Get("/revert")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void revertMassiveVoteForm() {}
	
	@Post("/revert")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void revertMassiveVote(Integer karma, Long userId, VoteType voteType) {
		User user = users.findById(userId);
		int karmaToSubtract = karma;
		if(VoteType.UP.equals(voteType)) {
			karmaToSubtract = -karma;
		}
		ReputationEvent massiveVoteEvent = new MassiveVoteRevertEvent(karmaToSubtract, user).reputationEvent();
		user.increaseKarma(karmaCalculator.karmaFor(massiveVoteEvent));
		reputationEvents.save(massiveVoteEvent);
		result.redirectTo(UserProfileController.class).reputationHistory(user, user.getSluggedName());
	}
}
