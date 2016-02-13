package org.mamute.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

@Entity
public class Vote {
    
    @Id @GeneratedValue
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private final VoteType type;
    
    @ManyToOne
    private final User author;
    
    @Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
    private final DateTime createdAt = new DateTime();
    
    @Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
    private DateTime lastUpdatedAt = new DateTime();
    
    public DateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	/**
     * @deprecated hibernate eyes
     */
    Vote() {
    	this(null, null);
    }
    
    public Vote(User author, VoteType type) {
        this.author = author;
        this.type = type;
    }

	public int getCountValue() {
		return type.getCountValue();
	}

	public long substitute(Vote previous, List<Vote> votes) {
		long delta = 0;
		if (votes.remove(previous))
			delta -= previous.getCountValue();
		votes.add(this);
		delta += getCountValue();
		return delta;
	}
	
	public User getAuthor() {
		return author;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vote other = (Vote) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public VoteType getType() {
        return type;
    }
    
    public boolean isUp() {
    	return type.equals(VoteType.UP);
    }

	public boolean isDown() {
		return type.equals(VoteType.DOWN);
	}
	
	public DateTime getCreatedAt() {
		return createdAt;
	}
    
}
