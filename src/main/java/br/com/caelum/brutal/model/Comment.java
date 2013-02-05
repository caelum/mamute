package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class Comment implements Updatable {
    
    @Id @GeneratedValue
    private Long id;
    
    @Lob
    @NotEmpty
    @Length(min = 15)
    private String comment;

    @Lob
    private String htmlComment;

    @ManyToOne(optional = false)
    private final User author;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private final DateTime createdAt = new DateTime();
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime lastUpdatedAt = new DateTime();
    
	@JoinTable(name = "Question_Votes")
	@OneToMany
	private final List<Vote> votes = new ArrayList<>();
	
	private long voteCount = 0;

    /**
     * @deprecated hibernate eyes
     */
    Comment() {
    	this(null, null);
    }
    
    public Comment(User author, String comment) {
		this.author = author;
		setComment(comment);
    }

	private void setComment(String comment) {
		this.comment = comment;
        this.htmlComment = MarkDown.parse(comment);
	}
    
    public User getAuthor() {
		return author;
	}
    
    public String getHtmlComment() {
		return htmlComment;
	}

	public String getTypeName() {
		return "Comment";
	}
	
	public Long getId() {
		return id;
	}
	
	public String getComment() {
		return comment;
	}

    public Class<?> getType() {
        return Comment.class;
    }
}
