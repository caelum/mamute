package org.mamute.auth;

import javax.inject.Inject;

import org.mamute.dao.LoginMethodDAO;
import org.mamute.model.LoginMethod;
import org.mamute.model.MethodType;
import org.mamute.model.User;

public class MergeLoginMethod {
	@Inject private LoginMethodDAO loginMethods;
	@Inject private Access access;
	
	public void mergeLoginMethods(String rawToken, User existantUser, MethodType type) {
		LoginMethod login = LoginMethod.newLogin(existantUser, existantUser.getEmail(), rawToken, type);
		
		existantUser.add(login);
		loginMethods.save(login);
		access.login(existantUser);
	}
}
