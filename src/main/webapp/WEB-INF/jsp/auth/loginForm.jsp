<form class="login-form" action="<c:url value="/login"/>" method="POST">
<input name="email" />
<input name="password" type="password" />
<input name="redirectUrl" value="${redirectUrl}" type="hidden" />
<input type="submit" />
</form>

<c:if test="${invalid_login}">
	<a href="${linkTo[ForgotPasswordController].forgotPasswordForm}"><fmt:message 
		key="forgot_password.link.text" /></a>
</c:if>