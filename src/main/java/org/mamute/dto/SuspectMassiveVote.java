package org.mamute.dto;

import org.mamute.model.User;


public class SuspectMassiveVote {

	private final User voteAuthor;
	private final Long voteCount;
	private final User answerAuthor;

	public SuspectMassiveVote(User voteAuthor, Long voteCount ,User answerAuthor) {
		this.voteAuthor = voteAuthor;
		this.voteCount = voteCount;
		this.answerAuthor = answerAuthor;
	}
	
	public User getAnswerAuthor() {
		return answerAuthor;
	}
	
	public User getVoteAuthor() {
		return voteAuthor;
	}
	
	public Long getMassiveVoteCount() {
		return voteCount;
	}
	
	@Override
	public String toString() {
		return voteAuthor.getId() + " " + voteCount + " " + answerAuthor.getId();
	}
}
