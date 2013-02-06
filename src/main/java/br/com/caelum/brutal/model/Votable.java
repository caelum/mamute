package br.com.caelum.brutal.model;

import java.io.Serializable;

public interface Votable {

	void substitute(Vote previous, Vote current);
	User getAuthor();
	Serializable getId();
	long getVoteCount();
}
