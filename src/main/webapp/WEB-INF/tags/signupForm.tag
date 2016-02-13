<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="name" type="java.lang.String" required="false" %>
<%@attribute name="email" type="java.lang.String" required="false" %>

<c:if test="${env.supports('feature.signup')}">
	<form action="${linkTo[SignupController].signup}" method="POST" class="validated-form user-form">
		<tags:socialLoginMethods buttonContent="signup"/>

		<label for="name">${t['signup.form.username.label']}</label>
		<input id="name" type="text" name="name" class="required text-input" maxlength="100" value="${name}"/>

		<label for="email">${t['signup.form.email.label']}</label>
		<input id="email" type="email" name="email" class="required text-input email" maxlength="100" value="${email}" />

		<label for="password">${t['signup.form.password.label']}</label>
		<input id="password" name="password" type="password" minlength="6" maxlength="100" class="required text-input"/>

		<label for="password-confirmation">${t['signup.form.confirm_password.label']}</label>
		<input id="password-confirmation" name="passwordConfirmation" type="password" minlength="6" maxlength="100" class="required text-input"/>

		<span>${t['signup.form.usage_terms.text']} <a href="${linkTo[SignupController].showUsageTerms}">${t['signup.form.usage_terms.link_content']}</a></span>

		<input class="post-submit big-submit submit" type="submit" value="${t['signup.form.submit.label']}"/>

	</form>
</c:if>
