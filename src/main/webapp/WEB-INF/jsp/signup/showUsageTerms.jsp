<c:set var="title" value="${t['metas.terms_of_service.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>
<h2 class="title big-text-title">${t['signup.terms.title']}</h2>

<div class="big-text">
	<tags:brutal-include value="terms"/>
</div>