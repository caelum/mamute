<tags:voteFor item="${question }" type="question" vote="${currentVote }"/>

<span id="question-${question.id }">${question.title}</span>
(<tags:editFor item="${question}" field="title" value="${question.title}" ajaxResult="question-${question.id}"/>)
<br>
<span id="question-description-${question.id }">${question.markedDescription}</span>
(<tags:editFor item="${question}" field="description" value="${question.description}" ajaxResult="question-description-${question.id}"/>)

<c:if test="${not empty currentUser}">
	<%@ include file="/WEB-INF/jsp/answer/answerForm.jsp"%>
</c:if>

<tags:add-a-comment item="${question}" />

<p>${answer.htmlText}</p>
<ul>
<c:forEach items="${answers.votes}" var="entry">
	<c:set var="answer" value="${entry.key}" />
	<c:set var="vote" value="${entry.value}" />
	<c:if test="${answer.solution}">
		<li class="answer solution" data-id="${answer.id}">
			<p id="answer-${answer.id}">${answer.htmlText}</p>
		</li>
	</c:if>
	<c:if test="${not answer.solution}">
		<li class="answer" data-id="${answer.id}">
			<p id="answer-${answer.id }">${answer.htmlText}</p>
			<a class="mark-as-solution" href="${linkTo[AnswerController].markAsSolution}">
				<fmt:message key="answer.mark_as_solution" />
			</a>
		</li>
	</c:if>
	(<tags:editFor item="${answer}" field="text" value="${answer.text}" ajaxResult="answer-${answer.id}" />)
	(votes ${answer.voteCount})
	<tags:voteFor item="${answer}" type="answer" vote="${vote}"/>
	<tags:add-a-comment item="${answer}"/>
</c:forEach>
</ul>
