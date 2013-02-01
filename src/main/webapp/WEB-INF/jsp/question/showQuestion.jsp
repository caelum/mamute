<div class="vote">
	<a class="up-vote vote-option" data-type="question" data-id="${question.id}">up</a><br />
	<a class="down-vote vote-option" data-type="question" data-id="${question.id}">down</a>
</div>


${question.title}
<br>
${question.markedDescription}

<c:if test="${not empty currentUser}">
	<%@ include file="/WEB-INF/jsp/answer/answerForm.jsp"%>
</c:if>

<tags:add-a-comment />

<c:forEach items="${question.answers}" var="answer">
	<p>${answer.htmlText}</p>
	<tags:add-a-comment />
	<c:if test="${answer.solution}">
		<fmt:message key="answer.isSolution" />
	</c:if>
	<c:if test="${not answer.solution}">
		<a class="link-ajax-post"
			href="${linkTo[AnswerController].markAsSolution[answer.id]}"> <fmt:message
				key="answer.markAsSolution" />
		</a>
	</c:if>
</c:forEach>