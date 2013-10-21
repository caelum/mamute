package br.com.caelum.brutal.model.flag;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutal.controllers.RetrieveKarmaDownvote;
import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.interfaces.Flaggable;

public class RetrieveKarma implements FlagAction{

	private LoggedUser user;
	private RetrieveKarmaDownvote retrieveDownvote;
	private VoteDAO votes;

	@Deprecated
	public RetrieveKarma() {
	}
	
	@Inject
	public RetrieveKarma (LoggedUser user, RetrieveKarmaDownvote bla, VoteDAO votes) {
		this.user = user;
		this.retrieveDownvote = bla;
		this.votes = votes;
	}

	@Override
	public void fire(Flaggable flaggable) {
		List<Vote> downVotes = votes.getDownVotes(flaggable.getId(), flaggable.getClass());
		retrieveDownvote.retrieveKarma(downVotes);
	}

	@Override
	public boolean shouldHandle(Flaggable flaggable) {
		boolean isQuestionOrAnswer = Question.class.isAssignableFrom(flaggable.getClass()) || Answer.class.isAssignableFrom(flaggable.getClass());
		boolean isModerator = user.isLoggedIn() && user.isModerator();
		return isModerator && isQuestionOrAnswer;
	}

}
