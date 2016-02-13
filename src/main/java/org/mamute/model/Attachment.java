package org.mamute.model;

import br.com.caelum.vraptor.observer.upload.UploadedFile;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

import javax.persistence.*;
import java.awt.image.BufferedImage;

@Entity
public class Attachment {

	@GeneratedValue
	@Id
	private Long id;

	@Transient
	private UploadedFile file;
	@Transient
	private BufferedImage image;

	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();

	@ManyToOne
	private User owner;

	private String ip;

	private String mime;

	private String name;

	/**
	 * @deprecated hibernate only
	 */
	Attachment() {
	}

	public Attachment(UploadedFile file, User owner, String ip) {
		this.file = file;
		this.owner = owner;
		this.ip = ip;
		this.mime = file.getContentType();
		this.name =file.getFileName();
	}

	public Attachment(BufferedImage image, User owner, String ip, String name) {
		this.image = image;
		this.owner = owner;
		this.ip = ip;
		this.mime = "image/png";
		this.name = name;
	}

	public UploadedFile getUploadedFile() {
		return file;
	}

	public Long getId() {
		return id;
	}

	public String fileName() {
		return name;
	}

	public String getMime() {
		return mime;
	}

	private User getOwner() {
		return owner;
	}

	public boolean canDelete(User user) {
		return getOwner().equals(user);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getImage() {
		return image;
	}
}
