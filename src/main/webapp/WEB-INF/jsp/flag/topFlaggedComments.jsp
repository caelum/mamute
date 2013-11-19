<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flagged}" var="commentAndFlag">
		<li class="title item-title">
			<!-- Eu preciso colocar um link para o author do comment e se possível para a question em que o comment está!! -->
			${commentAndFlag.flaggable.comment} - ${commentAndFlag.flaggable.author.name} - ${commentAndFlag.flagCount} <fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>