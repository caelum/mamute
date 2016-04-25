package org.mamute.observer;

import org.mamute.dao.BadgeDAO;
import org.mamute.dao.ReputationEventDAO;
import org.mamute.dto.KarmaEvent;
import org.mamute.event.BadgeEvent;
import org.mamute.model.Badge;
import org.mamute.model.BadgeType;
import org.mamute.model.EventType;
import org.mamute.model.ReputationEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BadgeEventObserver {

    @Inject private BadgeDAO badgeDAO;

    @Inject private ReputationEventDAO reputationDAO;

    public void subscribeEvents(@Observes BadgeEvent badgeEvent) {
        final List<Function<BadgeEvent, Badge>> evaluators = new ArrayList<>();


        switch (badgeEvent.getEventType()) {
            case CREATED_QUESTION:
                evaluators.add(this::considerFirstQuestionBadge);
                evaluators.add(this::considerTenthQuestionBadge);
                break;
            case CREATED_ANSWER:
                evaluators.add(this::considerFirstAnswerBadge);
                break;
            default:
                break;
        }

        for (final Function<BadgeEvent, Badge> fn : evaluators) {
            final Badge badge = fn.apply(badgeEvent);

            if (badge != null) {
                badgeDAO.awardBadge(badge);
            }
        }
    }

    public Badge considerFirstQuestionBadge(final BadgeEvent event) {
        final Badge badge;

        if (badgeDAO.userBadgeCount(event.getUser(), BadgeType.FIRST_QUESTION) == 0) {
            badge = new Badge(event.getUser(), BadgeType.FIRST_QUESTION);
        } else {
            badge = null;
        }

        return badge;
    }

    public Badge considerTenthQuestionBadge(final BadgeEvent event) {
        final Badge badge;

        List<ReputationEvent> events = reputationDAO.karmaEvents(event.getUser());

        final long count = events.stream()
                .filter(e -> EventType.CREATED_QUESTION.toString().equals(e.getType().name()))
                .count();

        if (count >= 9 && badgeDAO.userBadgeCount(event.getUser(), BadgeType.TENTH_QUESTION) == 0) {
           badge = new Badge(event.getUser(), BadgeType.TENTH_QUESTION);
        } else {
            badge = null;
        }

        return badge;
    }

    public Badge considerFirstAnswerBadge(final BadgeEvent event) {
        final Badge badge;

        if (badgeDAO.userBadgeCount(event.getUser(), BadgeType.FIRST_ANSWER) == 0) {
            badge = new Badge(event.getUser(), BadgeType.FIRST_ANSWER);
        } else {
            badge = null;
        }

        return badge;
    }
}
