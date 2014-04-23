<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="redirectUrl" type="java.lang.String" required="false" %>

<form class="validated-form user-form" action="${linkTo[AuthController].login}" method="POST">
	<a href="${facebookUrl}" class="face-button-wraper">
		<p><fmt:message key="auth.facebook_button.label" /></p>
		<span class="face-button"><fmt:message key="auth.facebook_button.content" /></span>
	</a>
	
	<a href="/sign-up/google">Google</a>
	
	<p class="or">&#8212; <fmt:message key="auth.or" /> &#8212;</p>
	<label for="email"><fmt:message key="signup.form.email.label" /></label>
	<input type="email" name="email" class="email required text-input" placeholder="nome@exemplo.com"/>
	<label for="password"><fmt:message key="signup.form.password.label" /></label>
	<input name="password" type="password" class="required text-input" placeholder="Senha"/>
	<a href="${linkTo[ForgotPasswordController].forgotPasswordForm}" class="forgot-password">
		<fmt:message key="forgot_password.link.text" />
	</a>
	<input name="redirectUrl" value="${redirectUrl}" type="hidden" />
	<input class="post-submit big-submit submit" type="submit" value="Login"/>
	
</form>
