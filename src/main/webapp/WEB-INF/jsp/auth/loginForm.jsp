<fmt:message key="metas.login.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title subheader">Login</h2>

<div class="login-or-signup">
	<div class="login">
		<tags:loginForm redirectUrl="${currentUrl}" />
	</div>
	<div class="signup">
		<tags:signupForm />
	</div>
</div>
