<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.question.title" var="title"/>

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<tags:questionForm uri="${linkTo[QuestionController].newQuestion}" />