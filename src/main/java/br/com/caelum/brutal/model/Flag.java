package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	@Enumerated(EnumType.STRING)
	private FlagType type;


	public Flag(FlagType flagType, User author) {
		this.type = flagType;
		this.author = author;
	}
	
	/**
	 * @deprecated
	 */
	Flag() {
	}
	
	public void setReason(String reason) {
		if (!type.equals(FlagType.OTHER)) {
			throw new IllegalStateException("only " + FlagType.OTHER + "should have a reason");
		}
		this.reason = reason;
	}
	
}
