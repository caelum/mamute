package br.com.caelum.brutal.model;


import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    private Calendar createdAt = Calendar.getInstance();
    
    private String email;
    
    @Id
    @GeneratedValue
    private Long id;

	private String password;
    
	/**
	 * @deprecated hibernate eyes only
	 */
    protected User() {
		this("", "786213675312678");
	}
    
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public void setPassword(String password) {
    	this.password = Digester.encrypt(password);
    }
    
}
