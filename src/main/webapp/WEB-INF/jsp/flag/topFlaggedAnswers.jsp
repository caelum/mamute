<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flaggedComments}" var="answerAndFlag">
		<li>
			${answerAndFlag.flaggable.question.information.title} - ${answerAndFlag.flaggable.author.name} - ${answerAndFlag.flagCount} <fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>