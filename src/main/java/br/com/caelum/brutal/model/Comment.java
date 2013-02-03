package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class Comment {
    
    @Id @GeneratedValue
    private Long id;
    
    @Lob
    @NotEmpty
    @Length(min = 15)
    private final String comment;

    @Lob
    private final String htmlComment;

    @ManyToOne(optional = false)
    private final User author;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private final DateTime createdAt = new DateTime();
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime lastUpdatedAt = new DateTime();
    
    /**
     * @deprecated hibernate eyes
     */
    Comment() {
    	this(null, null);
    }
    
    public Comment(User author, String comment) {
		this.author = author;
		this.comment = comment;
        this.htmlComment = MarkDown.parse(comment);
    }
    
    public User getAuthor() {
		return author;
	}
    
    public String getHtmlComment() {
		return htmlComment;
	}
    
}
