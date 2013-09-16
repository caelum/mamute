package br.com.caelum.brutal.controllers;

import javax.inject.Inject;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOrKarmaRule;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;
import br.com.caelum.brutal.model.VotingMachine;
import br.com.caelum.brutal.model.interfaces.Votable;
import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class VoteController {

	@Inject private Result result;
	@Inject private LoggedUser currentUser;
	@Inject private VoteDAO votes;
	@Inject private VotingMachine votingMachine;
	@Inject private ModelUrlMapping mapping;
	@Inject private LoggedUser loggedUser;
	
	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_UP)
	@Post("/{type}/{id}/voto/positivo")
	public void voteUp(Long id, String type) {
		tryToVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
		loggedUser.getCurrent().votedUp();
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_DOWN)
	@Post("/{type}/{id}/voto/negativo")
	public void voteDown(Long id, String type) {
		tryToVoteVotable(id, VoteType.DOWN, mapping.getClassFor(type));
		
	}

	@SuppressWarnings("rawtypes")
	private void tryToVoteVotable(Long id, VoteType voteType, Class votableType) {
		try {
		    Votable votable = votes.loadVotable(votableType, id);
		    Vote current = new Vote(currentUser.getCurrent(), voteType);
		    votingMachine.register(votable, current, votableType);
		    votes.save(current);
		    result.use(Results.json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(Results.http()).sendError(409);
		    return;
        }
	}
}