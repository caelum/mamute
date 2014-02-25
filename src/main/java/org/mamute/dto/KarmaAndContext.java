package org.mamute.dto;

import org.joda.time.DateTime;
import org.mamute.model.ReputationEventContext;

public class KarmaAndContext {
	private final ReputationEventContext context;
	private final Long karma;
	private DateTime date;

	public KarmaAndContext(ReputationEventContext context, Long karma, DateTime date) {
		this.context = context;
		this.karma = karma;
		this.date = date;
	}
	
	public Long getKarma() {
		return karma;
	}
	
	public ReputationEventContext getContext() {
		return context;
	}
	
	public DateTime getDate() {
		return date;
	}
}