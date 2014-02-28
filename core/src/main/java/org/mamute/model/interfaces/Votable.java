package org.mamute.model.interfaces;

import java.io.Serializable;
import java.util.List;

import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.Vote;

public interface Votable {
	void substitute(Vote previous, Vote current);
	void remove(Vote previous);
	User getAuthor();
	Serializable getId();
	long getVoteCount();
    Class<? extends Votable> getType();
	Question getQuestion();
	List<Vote> getVotes();
}
