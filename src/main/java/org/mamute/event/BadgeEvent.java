package org.mamute.event;

import org.mamute.model.EventType;
import org.mamute.model.ReputationEventContext;
import org.mamute.model.User;

public class BadgeEvent {

    private EventType eventType;

    private User user;

    private ReputationEventContext context;

    public BadgeEvent() {
        this.eventType = null;
        this.user = null;
        this.context = null;
    }

    public BadgeEvent(final EventType eventType, final User user) {
        this.eventType = eventType;
        this.user = user;
    }

    public BadgeEvent(final EventType eventType, final User user, final ReputationEventContext context) {
        this.eventType = eventType;
        this.user = user;
        this.context = context;
    }

    public EventType getEventType() {
        return eventType;
    }

    public User getUser() {
        return user;
    }

    public ReputationEventContext getContext() {
        return context;
    }
}
