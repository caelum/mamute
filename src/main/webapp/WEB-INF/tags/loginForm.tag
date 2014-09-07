<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="redirectUrl" type="java.lang.String" required="false" %>

<form class="validated-form user-form" action="${linkTo[AuthController].login}" method="POST">
	<tags:socialLoginMethods buttonContent="auth"/>

	<label for="email">${t['signup.form.email.label']}</label>
	<c:if test="${env.supports('feature.auth.db')}">
		<input type="email" name="email" class="email required text-input" placeholder="${t['signup.form.email.placeholder']}"/>
	</c:if>
	<c:if test="${env.supports('feature.auth.ldap')}">
		<input type="text" name="email" class="required text-input" placeholder="${t['signup.form.email.placeholder']}"/>
	</c:if>


	<label for="password">${t['signup.form.password.label']}</label>
	<input name="password" type="password" class="required text-input" placeholder="${t['signup.form.password.placeholder']}"/>

	<c:if test="${env.supports('feature.auth.db')}">
		<a href="${linkTo[ForgotPasswordController].forgotPasswordForm}" class="forgot-password">
				${t['forgot_password.link.text']}
		</a>
	</c:if>

	<input name="redirectUrl" value="${redirectUrl}" type="hidden"/>
	<input class="post-submit big-submit submit" type="submit" value="Login"/>

</form>