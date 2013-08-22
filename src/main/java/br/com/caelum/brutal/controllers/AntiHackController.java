package br.com.caelum.brutal.controllers;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;

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
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Post;
import br.com.caelum.vraptor4.Result;

@Controller
public class AntiHackController {

	@Inject private VoteDAO votes;
	@Inject private Result result;
	@Inject private ReputationEventDAO reputationEvents;
	@Inject private KarmaCalculator karmaCalculator;
	@Inject private UserDAO users;
	
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
