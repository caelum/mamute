 <fmt:message key="metas.unauthorized.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<div class="subheader">
	<h2 class="title page-title"><fmt:message key="unauthorized.title"/></h2>
</div>

<div class="not-found">
	<p>
		<c:if test="${empty unauthorizedMessage}">
			<fmt:message key="unauthorized.message">
				<fmt:param value="${linkTo[NavigationController].about}"/>
			</fmt:message>
		</c:if>
		<c:if test="${not empty unauthorizedMessage}">
			${unauthorizedMessage}
		</c:if>
	</p>
</div>
<div class="error-code">
	403
</div>
