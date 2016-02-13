<c:set var="title" value="${t['metas.forgot_password.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<c:if test="${empty confirmations}">
<h2 class="title page-title">${t['forgot_password.form.title']}</h2>
	<form action="${linkTo[ForgotPasswordController].requestEmailWithToken}" method="POST" class="validated-form user-form">
		<label for="email">${t['forgot_password.form.label.email']}</label>
		<input type="text" name="email" class="required text-input" />
		<input type="submit" class="submit" value="${t['forgot_password.form.submit']}" />
	</form>
</c:if>