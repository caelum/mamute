<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="name" type="java.lang.String" required="false" %>
<%@attribute name="email" type="java.lang.String" required="false" %>

<form action="${linkTo[SignupController].signup}" method="POST" class="validated-form user-form">
	<label for="name"><fmt:message key="signup.form.username.label" /></label>
	<input id="name" type="text" name="name" class="required text-input" maxlength="100" value="${name}"/>

	<label for="email"><fmt:message key="signup.form.email.label" /></label>
	<input id="email" type="email" name="email" class="required text-input email" maxlength="100" value="${email}" />

	<label for="password"><fmt:message key="signup.form.password.label" /></label>
	<input id="password" name="password" type="password" minlength="6" maxlength="100" class="required text-input"/>
	
	<label for="password-confirmation"><fmt:message key="signup.form.confirm_password.label" /></label>
	<input id="password-confirmation" name="passwordConfirmation" type="password" minlength="6" maxlength="100" class="required text-input"/>
	
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="signup.form.submit.label" />"/>
</form>
