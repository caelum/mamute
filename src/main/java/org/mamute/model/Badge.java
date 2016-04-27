package org.mamute.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

import javax.persistence.*;

@Entity
@Cacheable
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE, region="cache")
@Table(name="Badges")
public class Badge {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String badgeKey;

    private String comment;

    @Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
    private final DateTime createdAt = new DateTime();

    public Badge() {
    }

    public Badge(final User user, final String badgeKey) {
        this.user = user;
        this.badgeKey = badgeKey;
    }

    public Badge(final User user, final BadgeType badgeType) {
        this.user = user;
        this.badgeKey = badgeType.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBadgeKey() {
        return badgeKey;
    }

    public void setBadgeKey(String badgeKey) {
        this.badgeKey = badgeKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }
}
