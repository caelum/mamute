package org.mamute.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class TagPage {
	private static final int DESCRIPTION_MIN_LENGTH = 100;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private final Tag tag;

	@Lob
	@Length(min = DESCRIPTION_MIN_LENGTH, message = "tag_page.errors.about.length")
	@NotEmpty(message = "tag_page.errors.about.length")
	private String about;

	@Lob
	private String markedAbout;
	
	
	/**
	 * @deprecated hibernate eyes only
	 */
	TagPage() {
		this(null, MarkedText.notMarked(""));
	}
	
	public TagPage(Tag tag, MarkedText about) {
		this.tag = tag;
		setAbout(about);
	}
	
	public void setAbout(MarkedText about){
		this.about = about.getPure();
		this.markedAbout = about.getMarked();
	}

	public String getTagName() {
		return tag.getName();
	}
	
	public String getMarkedAbout() {
		return markedAbout;
	}
	
	public String getAbout() {
		return about;
	}

	public String getTagUriName() {
		return tag.getUriName();
	}
	
	public Tag getTag() {
		return tag;
	}
	
}
