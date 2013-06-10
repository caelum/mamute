package br.com.caelum.brutal.controllers;

import static java.util.Arrays.asList;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.caelum.brutal.auth.Access;
import br.com.caelum.brutal.auth.FacebookAuthService;
import br.com.caelum.brutal.auth.SignupInfo;
import br.com.caelum.brutal.dao.LoginMethodDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.MethodType;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.UrlValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;

@Resource
public class FacebookAuthController extends Controller{
	
	private final FacebookAuthService facebook;
	private final UserDAO users;
	private final LoginMethodDAO loginMethods;
	private final Result result;
	private final Access access;
	private final MessageFactory messageFactory;
	private UrlValidator urlValidator;
	private final static Logger LOG = Logger.getLogger(FacebookAuthController.class);

	public FacebookAuthController(FacebookAuthService facebook, UserDAO users, 
			LoginMethodDAO loginMethods, Result result, Access access,
			MessageFactory messageFactory, UrlValidator urlValidator) {
		this.facebook = facebook;
		this.users = users;
		this.loginMethods = loginMethods;
		this.result = result;
		this.access = access;
		this.messageFactory = messageFactory;
		this.urlValidator = urlValidator;
	}
	
	@Get("/cadastrar/facebook/")
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
		if (signupInfo.containsUser()) {
			user.setPhotoUri(signupInfo.getFacebookPhotoUri());
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
