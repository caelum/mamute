package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Flag {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Type(type = "text")
	private String reason;
	
	@ManyToOne
	private User author;

}
