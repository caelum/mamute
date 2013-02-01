package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Answer {
	@Id
	@GeneratedValue
	private Long id;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private final DateTime createdAt = new DateTime();

	@Type(type="text")
	private String text;
	
	@ManyToOne
	private User author;
	
	@ManyToOne
	private Question question;
	
	
}
