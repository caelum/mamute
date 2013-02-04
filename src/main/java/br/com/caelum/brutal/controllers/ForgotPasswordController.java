package br.com.caelum.brutal.controllers;

import java.util.Arrays;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.validators.UserValidator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import br.com.caelum.vraptor.validator.I18nMessage;

@Resource
public class ForgotPasswordController {
	
	private final Mailer mailer;
	private final TemplateMailer templates;
	private final Result result;
	private final UserDAO users;
	private final Environment env;

	public ForgotPasswordController(Mailer mailer, TemplateMailer templates, Result result, UserDAO users, Environment env) {
		this.mailer = mailer;
		this.templates = templates;
		this.result = result;
		this.users = users;
		this.env = env;
	}

	@Get("/forgotpassword")
	public void forgotPasswordForm() {
	}
	
	@Get("/sentmail")
	public void sentMail() {
	}

	@Post("/forgotpassword")
	public void requestEmailWithToken(String email, User user, UserValidator validator) {
		
		user = users.loadByEmail(email);

		if (!validator.validate(user)) {
			result.include("alerts", Arrays.asList("forgot_password.invalid_email"));
			result.redirectTo(this).forgotPasswordForm();
		}
		
		user.touchForgotPasswordToken();
		String currentToken = user.getForgotPasswordToken();

		StringBuffer tokenUrl = new StringBuffer(env.get("host"));
		tokenUrl.append("/newpassword/");
		tokenUrl.append(user.getId());
		tokenUrl.append("/");
		tokenUrl.append(currentToken);

		Email forgotPasswordEmail = templates.template("esqueci_minha_senha")
				.with("user_name", user.getName())
				.with("forgot_password_url", tokenUrl.toString())
				.to(user.getName(), user.getEmail());
		try {
			mailer.send(forgotPasswordEmail);
			result.include("user", user);
			result.redirectTo(this).sentMail();
		} catch (EmailException e) {
			result.include("alerts", Arrays.asList("forgot_password.send_mail.error"));
			result.redirectTo(this).forgotPasswordForm();
		}
		
		validator.onErrorRedirectTo(this).forgotPasswordForm();		
	}

	@Get("/newpassword/{id}/{token}")
	public void changePasswordForm(Long id, String token, UserValidator validator) {
		User user = users.loadByIdAndToken(id, token);
		if (!validator.validate(user)) {
			result.include("alerts", new I18nMessage("error", "forgot_password.invalid_token"));
			validator.onErrorRedirectTo(ListController.class);
		}
		
		result.include("id", id);
		result.include("token", token);
	}
	
	@Post("/newpassword/{id}/{token}")
	public void changePassword(Long id, String token, String password, String password_confirmation, UserValidator validator) {
		User user = users.loadByIdAndToken(id, token);
		if (!validator.validate(user)) {
			result.include("alerts", new I18nMessage("error", "forgot_password.invalid_token"));
			validator.onErrorRedirectTo(ListController.class);
		}

		boolean passwordUpdated = user.updateForgottenPassword(password, password_confirmation);
		if(!passwordUpdated) {
			result.include("alerts", new I18nMessage("error", "forgot_password.password_doesnt_match"));
			result.redirectTo(this).changePasswordForm(id, token, validator);
		}
		
		users.save(user);
		user.touchForgotPasswordToken();
		// redirecionando para a autentição...
		result.redirectTo(ListController.class).home();
	}
}
