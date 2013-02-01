${question.title} <br> 
${question.markedDescription}

<c:if test="${not empty currentUser}">
	<c:import url="/WEB-INF/jsp/answer/answerForm.jspf" />
</c:if>

<c:forEach items="${question.answers}" var="answer">
	<p>${answer.htmlText}</p>
	<c:if test="${answer.solution}">
		<fmt:message key="answer.isSolution" />
	</c:if>
	<c:if test="${not answer.solution}">
		<a class="link-ajax-post"
			href="${linkTo[AnswerController].markAsSolution[answer.id]}">
			<fmt:message key="answer.markAsSolution" />
		</a>
	</c:if>
</c:forEach>
