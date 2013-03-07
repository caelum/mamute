package br.com.caelum.brutal.controllers;

import org.hibernate.Session;

import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class LoginMethodDAO {
	private Session session;

	public LoginMethodDAO(Session session) {
		this.session = session;
	}
	
	public void save(LoginMethod loginMethod){
		session.save(loginMethod);		
	}
}
