
<form action="${linkTo[SignupController].signup}" method="POST" class="validated-form">
	<label>
		<fmt:message key="signup.form.username.label" />
		<input name="name" class="required" value="${name}"/>
	</label>
	<label>
		<fmt:message key="signup.form.email.label" />
		<input name="email" class="required" value="${email}" />
	</label>
	<label>
		<fmt:message key="signup.form.password.label" />
		<input name="password" type="password" class="required"/>
	</label>
	<label>
		<fmt:message key="signup.form.confirm_password.label" />
		<input name="passwordConfirmation" type="password" class="required"/>
	</label>
	<input type="submit" value="<fmt:message key="signup.form.submit.label" />"/>
</form>