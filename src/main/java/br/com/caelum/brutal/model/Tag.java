package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;


@Entity
public class Tag {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String description;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();
	
	@ManyToOne
	private Question question;
	
	@ManyToOne
	private User author;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Tag() {
		this("", "", null, null);
	}
	
	public Tag(String name, String description, Question question, User author) {
		this.name = name;
		this.description = description;
		this.question = question;
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
