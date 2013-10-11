<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flagged}" var="answerAndFlag">
		<li>
			<c:set var="answer" value="${answerAndFlag.flaggable}" />
			<a href="${linkTo[QuestionController].showQuestion(answer.question,answer.question.sluggedTitle)}#answer-${answer.id}">
				<c:out value="${answer.question.information.title}" escapeXml="true"/>
			</a> - ${answer.author.name} - ${answerAndFlag.flagCount} <fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>