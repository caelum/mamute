<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="redirectUrl" type="java.lang.String" required="false" %>

<form class="validated-form user-form" action="${linkTo[AuthController].login}" method="POST">
	<tags:socialLoginMethods buttonContent="auth"/>
	
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
