package org.mamute.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

@SQLDelete(sql = "update Flag set deleted = true where id = ?")
@Where(clause = "deleted = 0")
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

	private boolean deleted;

	/**
	 * @deprecated
	 */
	Flag() {
	}

	public Flag(FlagType flagType, User author) {
		this.type = flagType;
		this.author = author;
	}
	
	
	public void setReason(String reason) {
		if (!type.equals(FlagType.OTHER)) {
			throw new IllegalStateException("only " + FlagType.OTHER + "should have a reason");
		}
		this.reason = reason;
	}

	public boolean createdBy(User user) {
		return user.getId().equals(author.getId());
	}
	
	public String getReason() {
		return reason;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public FlagType getType() {
		return type;
	}
	
}
