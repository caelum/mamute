<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.answer_edit.title" var="title"/>

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<fmt:message key="${status}"/>