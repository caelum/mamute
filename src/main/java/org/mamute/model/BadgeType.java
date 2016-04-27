package org.mamute.model;

public enum BadgeType {

    FIRST_QUESTION(BadgeClass.BRONZE, "badge.first_question"),
    TENTH_QUESTION(BadgeClass.BRONZE, "badge.tenth_question"),
    FIRST_ANSWER(BadgeClass.BRONZE, "badge.first_answer"),
    TEN_POINT_QUESTION(BadgeClass.BRONZE, "badge.ten_point_question");

    private String id;

    private BadgeClass badgeClass;

    BadgeType(final BadgeClass badgeClass, final String id) {
        this.id = id;
        this.badgeClass = badgeClass;
    }

    public String getId() {
        return id;
    }

    public BadgeClass getBadgeClass() {
        return badgeClass;
    }
}
