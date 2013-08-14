package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.LoginMethod;

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
