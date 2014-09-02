<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.login.title']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title subheader">Login</h2>

<div class="login-or-signup">
	<div class="login">
		<tags:loginForm redirectUrl="${redirectUrl}"/>
	</div>
	<c:if test="${!disableSignup}">
		<div class="signup">
			<tags:signupForm/>
		</div>
	</c:if>
</div>
