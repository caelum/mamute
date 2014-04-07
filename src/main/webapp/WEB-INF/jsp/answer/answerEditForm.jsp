<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.answer_edit.title" var="title"/>

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>
<c:url var="uri" value="/resposta/editar/${answer.id}"/>
<tags:answerForm uri="${uri}" edit="true" />