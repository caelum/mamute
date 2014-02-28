<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="name" type="java.lang.String" required="false" %>
<%@attribute name="email" type="java.lang.String" required="false" %>

<form action="<c:url value="/cadastrar"/>" method="POST" class="validated-form user-form">
	<a href="${facebookUrl}" class="face-button-wraper">
		<p><fmt:message key="signup.facebook_button.label" /></p>
		<span class="face-button"><fmt:message key="signup.facebook_button.content" /></span>
	</a>
	<p class="or">&#8212; <fmt:message key="auth.or" /> &#8212;</p>
	<label for="name"><fmt:message key="signup.form.username.label" /></label>
	<input id="name" type="text" name="name" class="required text-input" maxlength="100" value="${name}"/>

	<label for="email"><fmt:message key="signup.form.email.label" /></label>
	<input id="email" type="email" name="email" class="required text-input email" maxlength="100" value="${email}" />

	<label for="password"><fmt:message key="signup.form.password.label" /></label>
	<input id="password" name="password" type="password" minlength="6" maxlength="100" class="required text-input"/>
	
	<label for="password-confirmation"><fmt:message key="signup.form.confirm_password.label" /></label>
	<input id="password-confirmation" name="passwordConfirmation" type="password" minlength="6" maxlength="100" class="required text-input"/>

	<span><fmt:message key="signup.form.usage_terms.text"/> <a href="${linkTo[SignupController].showUsageTerms}"><fmt:message key="signup.form.usage_terms.link_content"/></a></span>
	
	<input class="post-submit big-submit submit" type="submit" value="<fmt:message key="signup.form.submit.label" />"/>

</form>
