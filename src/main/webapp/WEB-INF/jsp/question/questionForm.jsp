<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.question.title']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<tags:questionForm uri="${linkTo[QuestionController].newQuestion}" />