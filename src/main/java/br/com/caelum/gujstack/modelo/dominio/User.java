package br.com.caelum.gujstack.modelo.dominio;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id 
    private Long id;
}
