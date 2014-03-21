<fmt:message key="metas.unmoderated.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<tags:pendingQuestions history="${questions}"/>
<tags:pendingAnswers history="${answers}"/>
