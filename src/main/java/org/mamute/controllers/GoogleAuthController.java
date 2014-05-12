package org.mamute.controllers;

import static java.util.Arrays.asList;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.mamute.auth.Access;
import org.mamute.auth.GoogleAPI;
import org.mamute.auth.MergeLoginMethod;
import org.mamute.auth.SignupInfo;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoginMethod;
import org.mamute.model.MethodType;
import org.mamute.model.User;
import org.mamute.qualifiers.Google;
import org.mamute.validators.UrlValidator;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.I18nMessage;

@Routed
@Controller
public class GoogleAuthController extends BaseController{
	
	@Inject @Google private OAuthService service;
	@Inject private Result result;
	@Inject private HttpSession session;
	@Inject private UserDAO users;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private Access access;
	@Inject private MessageFactory messageFactory;
	@Inject private MergeLoginMethod mergeLoginMethod;
	@Inject private UrlValidator urlValidator;
	@Inject private GoogleAPI google;
	
	@Get
	public void signUpViaGoogle(String redirect) {
		String url = service.getAuthorizationUrl(null);
		
		session.setAttribute("redirect", redirect);
		
		result.redirectTo(url);
	}
	
	@Get
	public void googleCallback(String code) {
		Token accessToken = google.getAccessToken(code);

		String redirect = (String) session.getAttribute("redirect");
	    
		SignupInfo signupInfo = google.getSignupInfo();
	    
		User existantGoogleUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.GOOGLE);
	    
		if(existantGoogleUser != null) {
	    	access.login(existantGoogleUser);
	    	redirectToRightUrl(redirect);
	    	return;
		}
	    
		User existantFacebookUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.FACEBOOK);
		if (existantFacebookUser != null) {
			mergeAndRedirect(accessToken, redirect, existantFacebookUser);
			return;
		}
		
		User existantBrutalUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.BRUTAL);
		if (existantBrutalUser != null) {
			mergeAndRedirect(accessToken, redirect, existantBrutalUser);
			return;
		}
	    
	    createNewUser(accessToken.getToken(), signupInfo);
	    
	    redirectToRightUrl(redirect);
	}

	private void mergeAndRedirect(Token accessToken, String redirect, User existantUser) {
		mergeLoginMethod.mergeLoginMethods(accessToken.getToken(), existantUser, MethodType.GOOGLE);
		logMessages(existantUser);
		redirectToRightUrl(redirect);
	}

	private void createNewUser(String rawToken, SignupInfo signupInfo) {
		User user = new User(signupInfo.getName(), signupInfo.getEmail());
		LoginMethod googleLogin = new LoginMethod(MethodType.GOOGLE, signupInfo.getEmail(), rawToken, user);
		if (signupInfo.containsPhotoUrl()) {
			user.setPhotoUri(signupInfo.getPhotoUri());
		}
		user.add(googleLogin);
		
		users.save(user);
		loginMethods.save(googleLogin);
		access.login(user);
	}
	
	private void logMessages(User existantUser) {
		List<I18nMessage> messages = asList(messageFactory.build("confirmation", "signup.facebook.existant_brutal", existantUser.getEmail()));
		result.include("messages", messages);
	}
	
	private void redirectToRightUrl(String state) {
		boolean valid = urlValidator.isValid(state);
		if (!valid) {
			includeAsList("messages", i18n("error", "error.invalid.url", state));
		}
        if (state != null && !state.isEmpty() && valid) {
            redirectTo(state);
        } else {
            redirectTo(ListController.class).home(null);
        }
	}
}
