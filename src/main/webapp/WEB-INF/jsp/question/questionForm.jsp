<fmt:message key="metas.question.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:questionForm uri="${linkTo[QuestionController].newQuestion}" />