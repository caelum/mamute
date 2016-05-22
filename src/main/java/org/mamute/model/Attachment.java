package org.mamute.model;

import br.com.caelum.vraptor.observer.upload.UploadedFile;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

import javax.persistence.*;
import java.awt.image.BufferedImage;

@Entity
public class Attachment {
	/**
	 * describe Attachment;
	 +-----------+--------------+------+-----+---------+----------------+
	 | Field     | Type         | Null | Key | Default | Extra          |
	 +-----------+--------------+------+-----+---------+----------------+
	 | id        | bigint(20)   | NO   | PRI | NULL    | auto_increment |
	 | createdAt | datetime     | YES  |     | NULL    |                |
	 | ip        | varchar(255) | YES  |     | NULL    |                |
	 | mime      | varchar(255) | YES  |     | NULL    |                |
	 | name      | varchar(255) | YES  |     | NULL    |                |
	 | owner_id  | bigint(20)   | YES  | MUL | NULL    |                |
	 | s3Key     | varchar(255) | YES  |     | NULL    |                |
	 | url       | varchar(320) | YES  |     | NULL    |                |
	 +-----------+--------------+------+-----+---------+----------------+
	 */
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

	private String s3Key;

	// url from where to obtain the attachment (the attachment itself will be stored in S3)
	// a random client cannot get the file using this URL; they will get access denied
	private String url;

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

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getS3Key()
	{
		return this.s3Key;
	}

	public void setS3Key(String s3Key)
	{
		this.s3Key = s3Key;
	}
}
