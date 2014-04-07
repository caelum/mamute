<fmt:message key="metas.unmoderated.title" var="title"/>
<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<tags:pendingQuestions history="${questions}"/>
<tags:pendingAnswers history="${answers}"/>
