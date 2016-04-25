package org.mamute.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.mamute.model.Badge;
import org.mamute.model.BadgeCount;
import org.mamute.model.BadgeType;
import org.mamute.model.User;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class BadgeDAO {

    @Inject private ReputationEventDAO events;

    private Session session;

    @Inject public BadgeDAO(Session session) {
        this.session = session;
    }

    public void awardBadge(final BadgeType badgeType, final User user) {
        session.save(new Badge(user, badgeType));
    }

    public void awardBadge(final Badge badge) {
        session.save(badge);
    }

    public List<BadgeCount> userBadgeCounts(final User user) {
        final String hql = "select b.badgeKey, count(*) from Badge b group by b.badgeKey, b.user having b.user = :user";

        final List<Object[]> result = session.createQuery(hql).setParameter("user", user).list();

        List<BadgeCount> badges = result.stream().map(e -> {
            final BadgeType type = BadgeType.valueOf((String) e[0]);
            final Long count = (Long) e[1];
            return new BadgeCount(type, count);
        }).collect(Collectors.toList());
        return badges;
    }

    public List<Badge> userBadges(final User user) {
        final String hql = "from Badge b where b.user = :user order by b.createdAt desc";

        List<Badge> badges = session.createQuery(hql).setParameter("user", user).list();

        return badges;
    }

    public List<Badge> userBadges(final User user, final BadgeType type) {
        final String hql = "from Badge b where b.user = :user and b.badgeKey = :key order by b.createdAt desc";

        return session.createQuery(hql).setParameter("user", user).setParameter("key", type.toString()).list();
    }

    public Long userBadgeCount(final User user, final BadgeType type) {
        final String hql = "select count(*) from Badge b where b.user = :user and b.badgeKey = :key";

        return (Long) session.createQuery(hql).setParameter("user", user).setParameter("key", type.toString()).list().get(0);
    }

    public void rebuildBadges(final User user) {

    }
}
