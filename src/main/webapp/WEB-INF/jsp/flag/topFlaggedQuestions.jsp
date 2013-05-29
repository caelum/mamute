<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul>
	<c:forEach items="${flagged}" var="questionAndFlag">
		<li>
			<c:set var="question" value="${questionAndFlag.flaggable}" />
			<a href="${linkTo[QuestionController].showQuestion[question][question.sluggedTitle]}">
				<c:out value="${questionAndFlag.flaggable.information.title}" escapeXml="true"/>
			</a> - ${question.author.name} - ${questionAndFlag.flagCount} <fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>