package org.mamute.model;

public enum BadgeType {

    FIRST_QUESTION("badge.first_question"),
    TENTH_QUESTION("badge.tenth_question"),
    FIRST_ANSWER("badge.first_answer");

    private String id;

    BadgeType(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
