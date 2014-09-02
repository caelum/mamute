<c:set var="title" value="${t['metas.change_password.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title">${t['change_password.form.title']}</h2>

<form action="${linkTo[ForgotPasswordController].changePassword(id,token)}" method="POST" class="validated-form change-pass user-form">
	<label for="password">${t['signup.form.password.label']}</label>
	<input type="password" id="password" name="password" minlength="6" class="required text-input" />
	
	<label for="password-confirmation">${t['signup.form.confirm_password.label']}</label>
	<input type="password" id="password-confirmation" name="passwordConfirmation" minlength="6" class="required text-input"/>
	
	<input type="submit" class="submit" value="${t['change_password.submit']}" />
</form>