<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<!-- Tanto aqui quanto na topFlaggedQuestions, não poderia ser separado em uma tag?? -->

<ul>
	<c:forEach items="${flagged}" var="answerAndFlag">
		<li  class="title item-title">
			<c:set var="answer" value="${answerAndFlag.flaggable}" />
			<a href="${linkTo[QuestionController].showQuestion(answer.question,answer.question.sluggedTitle)}#answer-${answer.id}">
				<c:out value="${answer.question.information.title}" escapeXml="true"/>
				<!-- Deveriamos colocar um link para author.name !! Yuri -->
			</a> - ${answer.author.name} - ${answerAndFlag.flagCount} <fmt:message key="moderation.flags"/> 
		</li>
	</c:forEach>
</ul>