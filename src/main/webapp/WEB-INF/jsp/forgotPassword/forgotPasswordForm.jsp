<form action="${linkTo[ForgotPasswordController].requestEmailWithToken}" method="POST" class="validated-form">
	<label for="email">${forgot_password.type_mail}</label>
	<input type="text" name="email" />
	<input type="submit" value="<fmt:message key="forgot_password.button" />" />
</form>
