package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Notification {

    @Id @GeneratedValue
    private Long id;
    
    @Type(type = "text")
    private String message;
    
    @ManyToOne
    private User user;
    
    /**
     * @deprecated hibernate eyes only
     */
    Notification() {
        // TODO Auto-generated constructor stub
    }

    public Notification(String message, User user) {
        this.message = message;
        this.user = user;
    }
    
}
