package br.com.caelum.brutal.model;

import static br.com.caelum.brutal.model.MarkDown.parse;
import static br.com.caelum.brutal.sanitizer.HtmlSanitizer.sanitize;

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
		this(null, "");
	}
	
	public TagPage(Tag tag, String about) {
		this.tag = tag;
		setAbout(about);
	}
	
	public void setAbout(String about){
		this.about = about;
		this.markedAbout = sanitize(parse(about));
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
	
	
}
