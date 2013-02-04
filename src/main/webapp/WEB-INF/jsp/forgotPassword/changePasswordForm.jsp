<form action="${linkTo[ForgotPasswordController].changePassword[id][token]}" method="POST" class="validated-form">
	<input type="password" name="password" /><br />
	<input type="password" name="password_confirmation" /><br />
	<input type="submit" value="<fmt:message key="forgot_password.change_password_button" />" />
</form>