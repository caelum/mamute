<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.flagged_comments.title']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<tags:flaggedQuestionsList list="${questions}" />
<tags:flaggedAnswersList list="${answers}" />
<tags:flaggedCommentsList list="${comments}" links="${commentQuestions}"/>