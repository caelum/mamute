package br.com.caelum.brutal.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Vote {
    
    @Id @GeneratedValue
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private final VoteType type;
    
    @ManyToOne
    private final User author;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private final DateTime createdAt = new DateTime();
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime lastUpdatedAt = new DateTime();
    
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

	public int getValue() {
		return type.getValue();
	}

	public long substitute(Vote previous, List<Vote> votes) {
		long delta = 0;
		if(votes.remove(previous))
			delta -= previous.getValue();
		votes.add(this);
		delta += getValue();
		return delta;
	}
	
	public User getAuthor() {
		return author;
	}
    
}
