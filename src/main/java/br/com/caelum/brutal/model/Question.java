package br.com.caelum.brutal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Question {

    @Id
    @GeneratedValue
    private Long id; 
    
    private String title;
    @Type(type="text")
    private String description;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private final DateTime createdAt = new DateTime(); 
    
}
