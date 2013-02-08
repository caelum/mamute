<h2 class="title page-title">Login</h2>

<section class="first-content">
	<form class="user-form" action="<c:url value="/login"/>" method="POST">
		<input type="text" name="email" class="email required text-input" placeholder="nome@exemplo.com"/>
		<input name="password" type="password" class="required text-input" placeholder="Senha"/>
		<input name="redirectUrl" value="${redirectUrl}" type="hidden" />
		<input type="submit" value="Login"/>
	</form>
	
	<a href="${linkTo[ForgotPasswordController].forgotPasswordForm}"><fmt:message 
			key="forgot_password.link.text" /></a>
</section>
