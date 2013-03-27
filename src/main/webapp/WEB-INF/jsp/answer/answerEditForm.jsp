<fmt:message key="metas.answer_edit.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:answerForm uri="${linkTo[AnswerController].edit[answer.id]}" edit="true" />