<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flagged}" var="commentAndFlag">
		<li class="title item-title ${ commentAndFlag.flagCount >= 3 ? 'heavy-flagged' : '' }">
			${commentAndFlag.flaggable.getTrimmedContent()}
			<p class="flagged-author">Autor:<tags:userProfileLink user="${commentAndFlag.flaggable.author}" isPrivate="false"></tags:userProfileLink></p>
			${commentAndFlag.flagCount}
			<fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>