package br.com.caelum.brutal.dao;

import javax.inject.Inject;

import org.hibernate.Session;

import br.com.caelum.brutal.model.LoginMethod;

public class LoginMethodDAO {
	private Session session;

	@Deprecated
	public LoginMethodDAO() {
	}

	@Inject
	public LoginMethodDAO(Session session) {
		this.session = session;
	}
	
	public void save(LoginMethod loginMethod){
		session.save(loginMethod);		
	}
}
