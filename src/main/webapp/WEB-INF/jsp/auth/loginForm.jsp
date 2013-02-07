<h2 class="title page-title">Login</h2>

<form class="user-form" action="<c:url value="/login"/>" method="POST">
<input type="text" name="email" placeholder="nome@exemplo.com"/>
<input name="password" type="password" placeholder="Senha"/>
<input name="redirectUrl" value="${redirectUrl}" type="hidden" />
<input type="submit" value="Login"/>
</form>

<a href="${linkTo[ForgotPasswordController].forgotPasswordForm}"><fmt:message 
		key="forgot_password.link.text" /></a>
