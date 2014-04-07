<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.login.title" var="title"/>

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title subheader">Login</h2>

<div class="login-or-signup">
	<div class="login">
		<tags:loginForm redirectUrl="${redirectUrl}" />
	</div>
	<div class="signup">
		<tags:signupForm />
	</div>
</div>
