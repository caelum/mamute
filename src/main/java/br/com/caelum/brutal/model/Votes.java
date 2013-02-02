package br.com.caelum.brutal.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Votes {
	private final Map<Answer, Vote> votes = new HashMap<>();

	public Votes(List<Answer> answers, List<Object[]> votes) {
		for (Object[] objects : votes) {
			Answer answer = (Answer) objects[0];
			Vote vote = (Vote) objects[1];
			this.votes.put(answer, vote);
		}
		for(Answer answer : answers) {
			if(!this.votes.containsKey(answer)) {
				this.votes.put(answer, null);
			}
		}
	}
	
	public Map<Answer, Vote> getVotes() {
		return votes;
	}

}
