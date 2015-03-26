package org.mamute.model;

import br.com.caelum.vraptor.observer.upload.UploadedFile;

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

	/**
	 * @deprecated hibernate only
	 */
	Attachment() {
	}

	public Attachment(UploadedFile file) {
		this.file = file;
	}

	public UploadedFile getUploadedFile() {
		return file;
	}

	public Long getId() {
		return id;
	}
}
