<h2 class="title page-title">${question.title}</h2>


<tags:voteFor item="${question }" type="question" vote="${currentVote }"/>

<span id="question-description-${question.id }">${question.markedDescription}</span>
(<a href="<c:url value="/question/edit/${question.id }"/>"><fmt:message key="edit" /></a>)


<c:if test="${not empty currentUser}">
	<%@ include file="/WEB-INF/jsp/answer/answerForm.jsp"%>
</c:if>

<tags:add-a-comment item="${question}" />

<ul>
<c:forEach items="${answers.votes}" var="entry">
	<c:set var="answer" value="${entry.key}" />
	<c:set var="vote" value="${entry.value}" />
	<c:if test="${answer.solution}">
		<li class="answer solution" data-id="${answer.id}">
			<p id="answer-${answer.id}">${answer.markedDescription}</p>
			(<a href="<c:url value="/answer/edit/${answer.id }"/>"><fmt:message key="edit" /></a>)
			<tags:voteFor item="${answer}" type="answer" vote="${vote}"/>
			<tags:add-a-comment item="${answer}"/>
		</li>
	</c:if>
	<c:if test="${not answer.solution}">
		<li class="answer" data-id="${answer.id}">
			<p id="answer-${answer.id }">${answer.markedDescription}</p>
			<a class="mark-as-solution" href="${linkTo[AnswerController].markAsSolution}">
				<fmt:message key="answer.mark_as_solution" />
			</a>
			(<a href="<c:url value="/answer/edit/${answer.id }"/>"><fmt:message key="edit" /></a>)
			<tags:voteFor item="${answer}" type="answer" vote="${vote}"/>
			<tags:add-a-comment item="${answer}"/>
		</li>
	</c:if>
</c:forEach>
</ul>
