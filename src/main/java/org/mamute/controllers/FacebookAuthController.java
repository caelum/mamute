package org.mamute.controllers;

import static java.util.Arrays.asList;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.mamute.auth.Access;
import org.mamute.auth.FacebookAuthService;
import org.mamute.auth.SignupInfo;
import org.mamute.dao.LoginMethodDAO;
import org.mamute.dao.UserDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoginMethod;
import org.mamute.model.MethodType;
import org.mamute.model.User;
import org.mamute.validators.UrlValidator;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.I18nMessage;

@Routed
@Controller
public class FacebookAuthController extends BaseController{
	
	private final static Logger LOG = Logger.getLogger(FacebookAuthController.class);
	@Inject private FacebookAuthService facebook;
	@Inject private UserDAO users;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private Result result;
	@Inject private Access access;
	@Inject private MessageFactory messageFactory;
	@Inject private UrlValidator urlValidator;
	
	@Get
	public void signupViaFacebook(String code, String state) {
		if (code == null) {
			includeAsList("messages", i18n("error", "error.signup.facebook.unknown"));
			redirectTo(SignupController.class).signupForm();
			return;
		}
		
		try {
			String rawToken = facebook.buildToken(code);
			SignupInfo signupInfo = facebook.getSignupInfo();
			User existantFacebookUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.FACEBOOK);
			if (existantFacebookUser != null) {
				access.login(existantFacebookUser);
				redirectToRightUrl(state);
				return;
			}
			User existantBrutalUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.BRUTAL);
			if (existantBrutalUser != null) {
				mergeLoginMethods(rawToken, existantBrutalUser);
				redirectToRightUrl(state);
				return;
			}
			
			createNewUser(rawToken, signupInfo);
			redirectToRightUrl(state);
		} catch (IllegalArgumentException e) {
			LOG.error("unable to signup user with facebook", e);
			includeAsList("messages", i18n("error", "error.signup.facebook.unknown"));
			redirectTo(SignupController.class).signupForm();
			return;
		}
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

	private void createNewUser(String rawToken, SignupInfo signupInfo) {
		User user = new User(signupInfo.getName(), signupInfo.getEmail());
		LoginMethod facebookLogin = new LoginMethod(MethodType.FACEBOOK, signupInfo.getEmail(), rawToken, user);
		if (signupInfo.containsPhotoUrl()) {
			user.setPhotoUri(signupInfo.getPhotoUri());
		}
		user.add(facebookLogin);
		
		users.save(user);
		loginMethods.save(facebookLogin);
		access.login(user);
	}

	private void mergeLoginMethods(String rawToken, User existantBrutalUser) {
		LoginMethod facebookLogin = LoginMethod.facebookLogin(existantBrutalUser, existantBrutalUser.getEmail(), rawToken);
		List<I18nMessage> messages = asList(messageFactory.build("confirmation", "signup.facebook.existant_brutal", existantBrutalUser.getEmail()));
		result.include("messages", messages);
		existantBrutalUser.add(facebookLogin);
		loginMethods.save(facebookLogin);
		access.login(existantBrutalUser);
	}
}
