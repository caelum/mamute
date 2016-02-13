package org.mamute.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.mamute.model.LoginMethod;

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
