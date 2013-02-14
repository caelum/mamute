package br.com.caelum.brutal.model;

import javax.persistence.Column;
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
	
	@Column(unique=true)
	private String name;
	
	private String description;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();
	
	@ManyToOne
	private final User author;

	private Long usageCount;
	
	/**
	 * @deprecated hibernate eyes only
	 */
	public Tag() {
		this("", "", null);
	}
	
	public Tag(String name, String description, User author) {
		this.name = name;
		this.description = description;
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}
	
	public Long getUsageCount() {
		return usageCount;
	}
	
	public void incrementUsage() {
		if (this.usageCount == null){
			this.usageCount = 1l;
			return;
		}
		this.usageCount ++;
	}
	
	public void decrementUsage(){
		this.usageCount --;
	}
}
