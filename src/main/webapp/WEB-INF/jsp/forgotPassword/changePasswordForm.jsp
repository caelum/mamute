<h2 class="title page-title"><fmt:message key="change_password.form.title"/></h2>

<form action="${linkTo[ForgotPasswordController].changePassword[id][token]}" method="POST" class="validated-form change-pass user-form">
	<label for="password"><fmt:message key="signup.form.password.label" /></label>
	<input type="password" id="password" name="password" minlength="6" class="required text-input" />
	
	<label for="password-confirmation"><fmt:message key="signup.form.confirm_password.label" /></label>
	<input type="password" id="password-confirmation" name="password_confirmation" minlength="6" class="required text-input"/>
	
	<input type="submit" value="<fmt:message key="change_password.submit" />" />
</form>