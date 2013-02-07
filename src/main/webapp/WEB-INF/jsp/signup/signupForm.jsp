<h2 class="title page-title"><fmt:message key="signup.form.title"/></h2>

<form action="${linkTo[SignupController].signup}" method="POST" class="validated-form user-form">
	<label for="name"><fmt:message key="signup.form.username.label" /></label>
	<input id="name" type="text" name="name" class="required" minlength="6" value="${name}"/>

	<label for="email"><fmt:message key="signup.form.email.label" /></label>
	<input id="email" type="text" name="email" class="required email" value="${email}" />

	<label for="password"><fmt:message key="signup.form.password.label" /></label>
	<input id="password" name="password" type="password" minlength="6" class="required"/>
	
	<label for="password-confirmation"><fmt:message key="signup.form.confirm_password.label" /></label>
	<input id="password-confirmation" name="passwordConfirmation" type="password" minlength="6" class="required"/>
	
	<input type="submit" value="<fmt:message key="signup.form.submit.label" />"/>
</form>