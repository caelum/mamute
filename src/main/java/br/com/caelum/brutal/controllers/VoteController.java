package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor4.view.Results.http;
import static br.com.caelum.vraptor4.view.Results.json;

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
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Post;
import br.com.caelum.vraptor4.Result;

@Controller
public class VoteController {

	@Inject private Result result;
	@Inject private LoggedUser currentUser;
	@Inject private VoteDAO votes;
	@Inject private VotingMachine votingMachine;
	@Inject private ModelUrlMapping mapping;

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.VOTE_UP)
	@Post("/{type}/{id}/voto/positivo")
	public void voteUp(Long id, String type) {
		tryToVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
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
		    result.use(json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(http()).sendError(409);
		    return;
        }
	}
}