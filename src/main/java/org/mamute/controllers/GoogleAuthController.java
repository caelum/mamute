package org.mamute.controllers;

import static java.util.Arrays.asList;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.mamute.auth.Access;
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
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.I18nMessage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	
	@Get
	public void signUpViaGoogle(String redirect) {
		String url = service.getAuthorizationUrl(null);
		session.setAttribute("redirect", redirect);
		
		result.redirectTo(url);
	}
	
	@Get
	public void googleCallback(String code) {
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(null, verifier);
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/me");
		service.signRequest(accessToken, request);
	    request.addHeader("GData-Version", "3.0");
	    Response response = request.send();
	    
	    JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
	    String email = jsonObject.get("emails").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
	    String name = jsonObject.get("displayName").getAsString();
	    String photoUrl = jsonObject.get("image").getAsJsonObject().get("url").getAsString();
	    
	    SignupInfo signupInfo = new SignupInfo(MethodType.GOOGLE, email, name, "", photoUrl);
	    
	    String redirect = (String) session.getAttribute("redirect");
	    
	    User existantGoogleUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.GOOGLE);
	    
	    if(existantGoogleUser != null) {
	    	access.login(existantGoogleUser);
	    	redirectToRightUrl(redirect);
	    	return;
	    }
	    
	    User existantFacebookUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.FACEBOOK);
		if (existantFacebookUser != null) {
			mergeLoginMethod.mergeLoginMethods(accessToken.getToken(), existantFacebookUser, MethodType.GOOGLE);
			logMessages(existantFacebookUser);
			redirectToRightUrl(redirect);
			return;
		}
		
		User existantBrutalUser = users.findByEmailAndMethod(signupInfo.getEmail(), MethodType.BRUTAL);
		if (existantBrutalUser != null) {
			mergeLoginMethod.mergeLoginMethods(accessToken.getToken(), existantBrutalUser, MethodType.GOOGLE);
			logMessages(existantBrutalUser);
			redirectToRightUrl(redirect);
			return;
		}
	    
	    createNewUser(accessToken.getToken(), signupInfo);
	    
	    
	    if (redirect != null) {
	    	redirectTo(redirect);
	    } else {
	    	redirectTo(ListController.class).home(null);
	    }
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
