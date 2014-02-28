package org.mamute.model.flag;

import java.util.List;

import javax.inject.Inject;

import org.mamute.controllers.RetrieveKarmaDownvote;
import org.mamute.dao.VoteDAO;
import org.mamute.model.Answer;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.Vote;
import org.mamute.model.interfaces.Flaggable;

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
