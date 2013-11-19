<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<!-- Tanto aqui quanto na topFlaggedAnswers, não poderia ser separado em uma tag?? -->

<ul>
	<c:forEach items="${flagged}" var="questionAndFlag">
		<li class="title item-title">
			<c:set var="question" value="${questionAndFlag.flaggable}" />
			<a href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}">
				<c:out value="${questionAndFlag.flaggable.information.title}" escapeXml="true"/>
				<!-- Deveriamos colocar um link para author.name !! Yuri -->
			</a> - ${question.author.name} -  
			<c:choose>
				<c:when test="${questionAndFlag.flagCount >= 4 }"><span style="color: red;">${questionAndFlag.flagCount}</span></c:when>
				<c:otherwise>${questionAndFlag.flagCount}</c:otherwise>
			</c:choose>
			<fmt:message key="moderation.flags"/>
		</li>
	</c:forEach>
</ul>