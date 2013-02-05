<form action="${linkTo[ForgotPasswordController].changePassword[id][token]}" method="POST" class="validated-form change-pass">
	<input type="password" name="password" class="required" /><br />
	<input type="password" name="password_confirmation" class="required" /><br />
	<input type="submit" value="<fmt:message key="forgot_password.change_password_button" />" />
</form>