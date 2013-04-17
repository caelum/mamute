<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flagged}" var="commentAndFlag">
		<li>
			${commentAndFlag.flaggable.comment} - ${commentAndFlag.flaggable.author.name} - ${commentAndFlag.flagCount} <fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>