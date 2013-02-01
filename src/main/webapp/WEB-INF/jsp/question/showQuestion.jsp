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

<p>${answer.htmlText}</p>
<ul>
<c:forEach items="${question.answers}" var="answer">
	<c:if test="${answer.solution}">
		<li class="answer solution" data-id="${answer.id}">
			<p>${answer.htmlText}</p>
			<tags:add-a-comment />
		</li>
	</c:if>
	<c:if test="${not answer.solution}">
		<li class="answer" data-id="${answer.id}">
			<p>${answer.htmlText}</p>
			<a class="mark-as-solution" href="${linkTo[AnswerController].markAsSolution}">
				<fmt:message key="answer.markAsSolution" />
			</a>
			<tags:add-a-comment />
		</li>
	</c:if>
</c:forEach>
</ul>
