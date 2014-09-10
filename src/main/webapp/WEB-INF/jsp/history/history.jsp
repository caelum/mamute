<c:set var="title" value="${t['metas.unmoderated.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<tags:pendingQuestions history="${questions}"/>
<tags:pendingAnswers history="${answers}"/>
