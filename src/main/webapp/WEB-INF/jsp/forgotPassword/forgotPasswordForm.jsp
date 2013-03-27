<fmt:message key="metas.forgot_password.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<c:if test="${empty confirmations}">
<h2 class="title page-title"><fmt:message key="forgot_password.form.title" /></h2>
	<form action="${linkTo[ForgotPasswordController].requestEmailWithToken}" method="POST" class="validated-form user-form">
		<label for="email"><fmt:message key="forgot_password.form.label.email" /></label>
		<input type="text" name="email" class="required text-input" />
		<input type="submit" value="<fmt:message key="forgot_password.form.submit" />" />
	</form>
</c:if>