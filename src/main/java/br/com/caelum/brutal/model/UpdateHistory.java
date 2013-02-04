package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

@Entity
public class UpdateHistory {

	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime moderatedAt;

	@Lob
	@NotEmpty
	@Length(min = 15)
	private final String content;

	@Lob
	private final String htmlContent;
	private final String objectType, fieldName;

	@ManyToOne
	private final User author;

	@ManyToOne
	private User moderatedBy;

	@Enumerated(EnumType.STRING)
	private UpdateStatus status;
	
	private final long targetId;

	/**
	 * @deprecated hibernate eyes only
	 */
	protected UpdateHistory() {
		this("", null, "", null, UpdateStatus.NO_NEED_TO_APPROVE, 0);
	}

	public UpdateHistory(String content, Class objectType, String fieldName,
			User author, UpdateStatus status, long targetId) {
		this.content = content;
		this.htmlContent = MarkDown.parse(content);
		if (objectType == null) {
			this.objectType = null;
		} else {
			this.objectType = objectType.getSimpleName();
		}
		this.fieldName = fieldName;
		this.author = author;
		this.status = status;
		this.targetId = targetId;
	}

	public void moderate(User moderator, UpdateStatus status) {
		if (this.moderatedAt != null) {
			throw new IllegalStateException("Already moderated");
		}
		this.status = status;
		this.moderatedBy = moderator;
		this.moderatedAt = new DateTime();
	}

	public long getTargetId() {
		return targetId;
	}

}
