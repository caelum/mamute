package org.mamute.model;

import static javax.persistence.FetchType.EAGER;
import static org.mamute.infra.NormalizerBrutal.toSlug;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.mamute.model.interfaces.Moderatable;
import org.mamute.providers.SessionFactoryCreator;

@Cacheable
@Entity
public class NewsInformation implements Information{
	private static final int COMMENT_MIN_LENGTH = 5;
	public static final int DESCRIPTION_MIN_LENGTH = 30;
	public static final int TITLE_MAX_LENGTH = 150;
	public static final int TITLE_MIN_LENGTH = 15;

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	@Length(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = "news.errors.title.length")
	@NotEmpty(message = "news.errors.title.length")
	private String title;

	@Lob
	@Length(min = DESCRIPTION_MIN_LENGTH, message = "news.errors.description.length")
	@NotEmpty(message = "news.errors.description.length")
	private String description;

	@Type(type = "text")
	@NotEmpty
	private String sluggedTitle;
	
	@NotNull(message = "news.errors.comment.not_null")
	@Length(min = COMMENT_MIN_LENGTH, message = "news.errors.comment.length")
	@NotEmpty(message = "news.errors.comment.length")
	@Type(type = "text")
	private String comment;

	@ManyToOne(optional = false, fetch = EAGER)
	private final User author;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@Embedded
	private Moderation moderation;

	@Lob
	private String markedDescription;

	@Enumerated(EnumType.STRING)
	private UpdateStatus status;

	private String ip;
	
	@ManyToOne
	private News news;

	/**
	 * @deprecated hibernate only
	 */
	NewsInformation() {
		this("", MarkedText.notMarked(""), null, "");
	}

	public NewsInformation(String title, MarkedText description, LoggedUser user, String comment) {
        if (user == null) {
			this.author = null;
			this.ip = null;
		} else {
    		this.author = user.getCurrent();
    		this.ip = user.getIp();
		}
		setTitle(title);
		setDescription(description);
		this.comment = comment;
	}

	public void moderate(User moderator, UpdateStatus status) {
		if (status == UpdateStatus.EDITED) {
			this.status = status;
			return;
		}
		
		if (this.moderation != null) {
			throw new IllegalStateException("Already moderated");
		}
		this.status = status;
		this.moderation = new Moderation(moderator);
	}
	
	boolean isModerated() {
	    return moderation != null;
	}

	private void setTitle(String title) {
		this.title = title;
		this.sluggedTitle = toSlug(title);
	}
 
	private void setDescription(MarkedText description) {
		this.description = description.getPure();
		this.markedDescription = description.getMarked();
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getSluggedTitle() {
		return sluggedTitle;
	}

	public String getMarkedDescription() {
		return markedDescription;
	}

	public User getAuthor() {
		return author;
	}

	public void setInitStatus(UpdateStatus status) {
		if (this.status != null) {
			throw new IllegalStateException(
					"Status can only be setted once. Afterwards it should BE MODERATED!");
		}
		this.status = status;
	}

	public DateTime getCreatedAt() {
        return createdAt;
    }
	
	public Long getId() {
        return id;
    }
	
	public Moderatable getModeratable() {
        return news;
    }
	
	public UpdateStatus getStatus() {
		return status;
	}

    public boolean isPending() {
        return status == UpdateStatus.PENDING;
    }
    
    public String getComment() {
        return comment;
    }

	@Override
	public String getTypeName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isBeforeCurrent() {
		return createdAt.isBefore(news.getInformation().getCreatedAt());
	}
	

	public DateTime moderatedAt() {
		return moderation.getModeratedAt();
	}

	@Override
	public String toString() {
		return "NewsInformation [id=" + id + ", author=" + author
				+ ", status=" + status + ", news=" + news+ "]";
	}

	public void setNews(News news) {
		this.news = news;
	}

	@Override
	public void setModeratable(Moderatable moderatable) {
		news = (News) moderatable;
	}

	
	
}
