package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.factory.MessageFactory;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.vraptor.DefaultLinker;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import br.com.caelum.vraptor.validator.Validator;

@Controller
public class ForgotPasswordController {
	
	@Inject private Mailer mailer;
	@Inject private TemplateMailer templates;
	@Inject private Result result;
	@Inject private UserDAO users;
	@Inject private DefaultLinker linker;
	@Inject private Validator validator;
	@Inject private MessageFactory messageFactory;
	@Inject private BundleFormatter bundle;

	@Get("/esqueci-minha-senha")
	public void forgotPasswordForm() {
	}

	@Post("/esqueci-minha-senha")
	public void requestEmailWithToken(String email) {
		User user = users.loadByEmail(email);

		if (user == null) {
			validator.add(messageFactory.build("error", "forgot_password.invalid_email"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
			return;
		}

		Email forgotPasswordEmail = emailWithTokenFor(user);
		try {
			mailer.send(forgotPasswordEmail);
			result.include("messages", Arrays.asList(
						messageFactory.build("confirmation", "forgot_password.sent_mail", user.getEmail()),
						messageFactory.build("confirmation", "forgot_password.sent_mail.warn")
					));
			result.redirectTo(this).forgotPasswordForm();
		} catch (EmailException e) {
			validator.add(messageFactory.build("error", "forgot_password.send_mail.error"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
		}	
	}

	@Get("/mudar-senha/{id}/{token}")
	public void changePasswordForm(Long id, String token) {
		validateTokenAndGetUser(id, token);
		
		result.include("id", id);
		result.include("token", token);
	}
	
	@Post("/mudar-senha/{id}/{token}")
	public void changePassword(Long id, String token, String password, String passwordConfirmation) {
		User user = validateTokenAndGetUser(id, token);

		boolean passwordUpdated = user.updateForgottenPassword(password, passwordConfirmation);
		if(!passwordUpdated) {
			validator.add(messageFactory.build("error", "forgot_password.password_doesnt_match"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
		}
		
		user.touchForgotPasswordToken();
		users.save(user);
		result.include("messages", Arrays.asList(
					messageFactory.build("confirmation", "forgot_password.password_changed")
				));
		linker.linkTo(ListController.class).home(null);
	    result.forwardTo(AuthController.class).login(user.getEmail(), password, linker.get());
	}
	
	private String tokenUrlFor(User user) {
		String token = user.touchForgotPasswordToken();
		linker.linkTo(this).changePasswordForm(user.getId(), token);
		return linker.get();
	}
	
	private Email emailWithTokenFor(User user) {
		String url = tokenUrlFor(user);
		return templates.template("forgot_password_mail")
				.with("bundle", bundle)
				.with("user_name", user.getName())
				.with("url", url)
				.to(user.getName(), user.getEmail());
	}

	private User validateTokenAndGetUser(Long id, String token) {
		User user = users.loadByIdAndToken(id, token);
		if (user == null) {
			validator.add(messageFactory.build("error", "forgot_password.invalid_token"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
		}
		return user;
	}
}
