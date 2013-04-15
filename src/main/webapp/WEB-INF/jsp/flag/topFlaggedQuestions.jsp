<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flaggedComments}" var="questionAndFlag">
		<li>
			${questionAndFlag.flaggable.information.title} - ${questionAndFlag.flaggable.author.name} - ${questionAndFlag.flagCount} <fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>