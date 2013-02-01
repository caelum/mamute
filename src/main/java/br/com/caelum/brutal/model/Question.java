package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.infra.NormalizerBrutal.toSlug;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class Question {

	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "text")
	@NotEmpty
	private String title;

	@Type(type = "text")
	@NotEmpty
	private String description;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private User author;

	@Type(type = "text")
	private String sluggedTitle;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Question() {
		this("", "");
	}
	
	public Question(String title, String description) {
		this.title = title;
		this.sluggedTitle = toSlug(title);
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Question [title=" + title + ", createdAt=" + createdAt + "]";
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public String getSluggedTitle() {
		return sluggedTitle;
	}


}
