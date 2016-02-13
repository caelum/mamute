<c:set var="siteName" value="${t['site.name']}"/>
<c:set var="title" value="${t['metas.errors.title']}"/>
<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="subheader">
	<h2 class="title page-title">${t['internal_error.title']}</h2>
</div>

<c:if test="${env.name != 'production'}">	
		<pre class="stacktrace">${stacktrace}</pre>
</c:if>
<c:if test="${env.name == 'production'}">
	<!-- ${stacktrace} -->
	<div class="not-found">
		${t['internal_error.text']}
	</div>
	<div class="error-code">
		500
	</div>
</c:if>