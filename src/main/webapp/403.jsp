 <c:set var="title" value="${t['metas.unauthorized.title']}"/>
<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="subheader">
	<h2 class="title page-title">${t['unauthorized.title']}</h2>
</div>

<div class="not-found">
	<p>
		<c:if test="${empty unauthorizedMessage}">
			${t['unauthorized.message'].args(linkTo[NavigationController].about)}
		</c:if>
		<c:if test="${not empty unauthorizedMessage}">
			${unauthorizedMessage}
		</c:if>
	</p>
</div>
<div class="error-code">
	403
</div>
