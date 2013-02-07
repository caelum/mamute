<form action="${linkTo[ForgotPasswordController].changePassword[id][token]}" method="POST" class="validated-form change-pass user-form">
	<input type="password" id="password" name="password" minlength="6" class="required" placeholder="<fmt:message key='change_password.password.placeholder'/>" />
	<input type="password" id="password-confirmation" name="password_confirmation" minlength="6" class="required" placeholder="<fmt:message key='change_password.password_confirmation.placeholder'/>"/>
	<input type="submit" value="<fmt:message key="change_password.submit" />" />
</form>