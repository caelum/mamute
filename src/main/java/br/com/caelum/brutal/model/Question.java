package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Question {

    @Id
    @GeneratedValue
    private Long id; 
    
    private String title;
    
    @Type(type="text")
    private String description;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private final DateTime createdAt = new DateTime();
    
    @ManyToOne
    private User author;

    public Question(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Question [title=" + title + ", createdAt=" + createdAt + "]";
    } 
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
}
