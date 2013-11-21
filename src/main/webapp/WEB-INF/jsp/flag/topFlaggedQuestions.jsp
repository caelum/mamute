<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flagged}" var="questionAndFlag">
		<li class="title item-title ${ questionAndFlag.flagCount >= 3 ? 'heavy-flagged' : '' } ">
			<c:set var="question" value="${questionAndFlag.flaggable}" />
			<a href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}">
				<c:out value="${questionAndFlag.flaggable.information.title}" escapeXml="true"/>
			</a>
			<p class="flagged-author">Autor: <tags:userProfileLink user="${question.author}" isPrivate="false"></tags:userProfileLink></p> 
			<span>${questionAndFlag.flagCount}</span>
			
			<fmt:message key="moderation.flags"/>
		</li>
	</c:forEach>
</ul>