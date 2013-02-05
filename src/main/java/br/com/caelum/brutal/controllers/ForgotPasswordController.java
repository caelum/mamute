package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.UserValidator;
import br.com.caelum.brutal.vraptor.DefaultLinker;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import br.com.caelum.vraptor.validator.I18nMessage;

@Resource
public class ForgotPasswordController {
	
	private final Mailer mailer;
	private final TemplateMailer templates;
	private final Result result;
	private final UserDAO users;
	private final DefaultLinker linker;
	private final UserValidator validator;

	public ForgotPasswordController(Mailer mailer, TemplateMailer templates, Result result, UserDAO users, DefaultLinker linker, UserValidator userValidator) {
		this.mailer = mailer;
		this.templates = templates;
		this.result = result;
		this.users = users;
		this.linker = linker;
		this.validator = userValidator;
	}

	@Get("/forgotpassword")
	public void forgotPasswordForm() {
	}
	
	@Get("/sentmail")
	public void sentMail() {
	}

	@Post("/forgotpassword")
	public void requestEmailWithToken(String email, User user) {
		user = users.loadByEmail(email);

		if (!validator.validate(user)) {
			result.include("errors", Arrays.asList("forgot_password.invalid_email"));
			result.redirectTo(this).forgotPasswordForm();
			return;
		}

		Email forgotPasswordEmail = emailWithTokenFor(user);
		try {
			mailer.send(forgotPasswordEmail);
			result.include("user", user);
			result.redirectTo(this).sentMail();
		} catch (EmailException e) {
			result.include("errors", Arrays.asList("forgot_password.send_mail.error"));
			result.redirectTo(this).forgotPasswordForm();
		}	
	}

	@Get("/newpassword/{id}/{token}")
	public void changePasswordForm(Long id, String token) {
		validateTokenAndGetUser(id, token);
		
		result.include("id", id);
		result.include("token", token);
	}
	
	@Post("/newpassword/{id}/{token}")
	public void changePassword(Long id, String token, String password, String password_confirmation) {
		User user = validateTokenAndGetUser(id, token);

		boolean passwordUpdated = user.updateForgottenPassword(password, password_confirmation);
		if(!passwordUpdated) {
			result.include("errors", Arrays.asList(new I18nMessage("error", "forgot_password.password_doesnt_match")));
			result.redirectTo(this).changePasswordForm(id, token);
		}
		
		user.touchForgotPasswordToken();
		users.save(user);
		result.include("confirmations", Arrays.asList("forgot_password.password_changed"));
		result.redirectTo(ListController.class).home();
	}
	
	private String tokenUrlFor(User user) {
		String token = user.getNewForgotPasswordToken();
		linker.linkTo(this).changePasswordForm(user.getId(), token);
		return linker.get();
	}
	
	private Email emailWithTokenFor(User user) {
		String url = tokenUrlFor(user);
		return templates.template("esqueci_minha_senha")
				.with("user_name", user.getName())
				.with("forgot_password_url", url)
				.to(user.getName(), user.getEmail());
	}

	private User validateTokenAndGetUser(Long id, String token) {
		User user = users.loadByIdAndToken(id, token);
		if (!validator.validate(user)) {
			result.include("errors", Arrays.asList(new I18nMessage("error", "forgot_password.invalid_token")));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
		}
		return user;
	}
}
