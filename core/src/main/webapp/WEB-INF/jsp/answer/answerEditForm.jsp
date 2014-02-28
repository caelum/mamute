<fmt:message key="metas.answer_edit.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>
<c:url var="uri" value="/resposta/editar/${answer.id}"/>
<tags:answerForm uri="${uri}" edit="true" />