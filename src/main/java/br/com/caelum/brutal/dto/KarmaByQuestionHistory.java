package br.com.caelum.brutal.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Question;

public class KarmaByQuestionHistory {

	private List<KarmaAndQuestion> history = new ArrayList<>();

	public KarmaByQuestionHistory(List<Object[]> results) {
		for (Object[] entry : results) {
			Question q = (Question) entry[0];
			Long karma = (Long) entry[1];
			DateTime date = (DateTime) entry[2];
			history.add(new KarmaAndQuestion(q, karma, date));
		}
	}
	
	public List<KarmaAndQuestion> getHistory() {
		return history;
	}
}
