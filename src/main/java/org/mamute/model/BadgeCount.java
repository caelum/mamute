package org.mamute.model;

/**
 *
 */
public class BadgeCount {

    private BadgeType badgeType;

    private long count;

    public BadgeCount(final BadgeType badgeType, final long count) {
        this.badgeType = badgeType;
        this.count = count;
    }

    public BadgeType getBadgeType() {
        return badgeType;
    }

    public long getCount() {
        return count;
    }

}
