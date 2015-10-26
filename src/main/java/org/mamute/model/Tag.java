package org.mamute.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

import static org.mamute.infra.NormalizerBrutal.toSlug;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="cache")
@Entity
public class Tag {
	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	@NotEmpty
	private String name;

	@Column(unique = true, nullable = false)
	@Type(type = "text")
	@NotEmpty
	private String sluggedName;

	private String description;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private final User author;

	private Long usageCount = 0l;

	/**
	 * @deprecated hibernate eyes only
	 */
	public Tag() {
		this("", "", null);
	}

	public Tag(String name, String description, User author) {
		this.name = name.toLowerCase();
		this.sluggedName = toSlug(name, true);
		this.description = description;
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public String getUriName() {
		return this.sluggedName;
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
		this.usageCount ++;
	}

	public void decrementUsage(){
		this.usageCount --;
	}

	public boolean equals(Object aThat){
		if ( this == aThat ) return true;
		if ( !(aThat instanceof Tag) ) return false;
		Tag that = (Tag)aThat;
		return that.id.equals(this.id);
	}
}
