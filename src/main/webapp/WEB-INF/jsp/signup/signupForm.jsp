<c:set var="title" value="${t['metas.signup.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title page-title">${t['signup.form.title']}</h2>

<tags:signupForm name="${name}" email="${email}"/>