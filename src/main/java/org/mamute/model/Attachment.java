package org.mamute.model;

import br.com.caelum.vraptor.observer.upload.UploadedFile;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

import javax.persistence.*;

@Entity
public class Attachment {

	@GeneratedValue
	@Id
	private Long id;

	private String path;

	@ManyToOne
	private Question question;

	@Transient
	private UploadedFile file;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private User owner;
	private String ip;

	/**
	 * @deprecated hibernate only
	 */
	Attachment() {
	}

	public Attachment(UploadedFile file, User owner, String ip) {
		this.file = file;
		this.owner = owner;
		this.ip = ip;
	}

	public UploadedFile getUploadedFile() {
		return file;
	}

	public Long getId() {
		return id;
	}
}
