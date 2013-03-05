package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.vraptor.DefaultLinker;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
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
	private Validator validator;
	private Localization localization;

	public ForgotPasswordController(Mailer mailer, TemplateMailer templates, Result result, UserDAO users, DefaultLinker linker, Validator validator, Localization localization) {
		this.mailer = mailer;
		this.templates = templates;
		this.result = result;
		this.users = users;
		this.linker = linker;
		this.validator = validator;
		this.localization = localization;
	}

	@Get("/forgotpassword")
	public void forgotPasswordForm() {
	}
	
	@Get("/sentmail")
	public void sentMail() {
	}

	@Post("/forgotpassword")
	public void requestEmailWithToken(String email) {
		User user = users.loadByEmail(email);

		if (user == null) {
			validator.add(new I18nMessage("error", "forgot_password.invalid_email"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
			return;
		}

		Email forgotPasswordEmail = emailWithTokenFor(user);
		try {
			mailer.send(forgotPasswordEmail);
			result.include("user", user);
			result.redirectTo(this).sentMail();
		} catch (EmailException e) {
			validator.add(new I18nMessage("error", "forgot_password.send_mail.error"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
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
			validator.add(new I18nMessage("error", "forgot_password.password_doesnt_match"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
		}
		
		user.touchForgotPasswordToken();
		users.save(user);
		result.include("messages", Arrays.asList(
				new I18nMessage("confirmation", "forgot_password.password_changed")
				));
		result.redirectTo(ListController.class).home();
	}
	
	private String tokenUrlFor(User user) {
		String token = user.touchForgotPasswordToken();
		linker.linkTo(this).changePasswordForm(user.getId(), token);
		return linker.get();
	}
	
	private Email emailWithTokenFor(User user) {
		String url = tokenUrlFor(user);
		return templates.template("forgot_password_mail")
				.with("localization", localization)
				.with("user_name", user.getName())
				.with("url", url)
				.to(user.getName(), user.getEmail());
	}

	private User validateTokenAndGetUser(Long id, String token) {
		User user = users.loadByIdAndToken(id, token);
		if (user == null) {
			validator.add(new I18nMessage("error", "forgot_password.invalid_token"));
			validator.onErrorRedirectTo(this).forgotPasswordForm();
		}
		return user;
	}
}
