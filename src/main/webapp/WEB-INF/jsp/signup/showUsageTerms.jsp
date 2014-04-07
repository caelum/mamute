<fmt:message key="metas.terms_of_service.title" var="title"/>
<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>
<h2 class="title big-text-title"><fmt:message key="signup.terms.title"/></h2>

<div class="big-text">
	<tags:brutal-include value="terms"/>
</div>