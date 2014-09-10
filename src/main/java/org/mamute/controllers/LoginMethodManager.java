package org.mamute.controllers;

import static java.util.Arrays.asList;
import static org.mamute.model.SanitizedText.fromTrustedText;

import java.util.List;

import javax.inject.Inject;

import org.mamute.auth.Access;
import org.mamute.auth.MergeLoginMethod;
import org.mamute.auth.SignupInfo;
import org.mamute.auth.SocialAPI;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoginMethod;
import org.mamute.model.MethodType;
import org.mamute.model.SanitizedText;
import org.mamute.model.User;

import com.google.common.base.Optional;

import br.com.caelum.vraptor.validator.I18nMessage;

public class LoginMethodManager {
	@Inject private UserDAO users;
	@Inject private MergeLoginMethod mergeLoginMethod;
	@Inject private MessageFactory messageFactory;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private Access access;
	
	public boolean merge(MethodType type, SocialAPI socialApi) {
		Optional<SignupInfo> optional = socialApi.getSignupInfo();
		
		if(!optional.isPresent()) return false;
		
		SignupInfo signupInfo = optional.get();
	    
		User existantGoogleUser = users.findByEmailAndMethod(signupInfo.getEmail(), type);
	    
		if(existantGoogleUser != null) {
	    	access.login(existantGoogleUser);
	    	return true;
		}
		
		String token = socialApi.getAccessToken().getToken();
		
		User existantUser = users.findByEmail(signupInfo.getEmail());
		if (existantUser != null) {
			mergeLoginMethod.mergeLoginMethods(token, existantUser, type);
			return true;
		}
		
		createNewUser(token, signupInfo, type);
		return true;
	}
	
	private List<I18nMessage> getConfirmationMessages(User existantUser) {
		List<I18nMessage> messages = asList(messageFactory.build("confirmation", "signup.facebook.existant_brutal", existantUser.getEmail()));
		return messages;
	}
	
	private void createNewUser(String rawToken, SignupInfo signupInfo, MethodType type) {
		User user = new User(fromTrustedText(signupInfo.getName()), signupInfo.getEmail());
		LoginMethod googleLogin = new LoginMethod(type, signupInfo.getEmail(), rawToken, user);
		if (signupInfo.containsPhotoUrl()) {
			user.setPhotoUri(signupInfo.getPhotoUri());
		}
		user.add(googleLogin);
		
		users.save(user);
		loginMethods.save(googleLogin);
		access.login(user);
	}
}
