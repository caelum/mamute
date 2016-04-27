package org.mamute.dto;

import org.joda.time.DateTime;
import org.mamute.model.ReputationEventContext;

/**
 *
 */
public class KarmaEvent {

    private ReputationEventContext context;

    private Long karma;

    private DateTime date;

    public KarmaEvent(ReputationEventContext context, Long karma, DateTime date) {
        this.context = context;
        this.karma = karma;
        this.date = date;
    }

    public ReputationEventContext getContext() {
        return context;
    }

    public Long getKarma() {
        return karma;
    }

    public DateTime getDate() {
        return date;
    }
}
