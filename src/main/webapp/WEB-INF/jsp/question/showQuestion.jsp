<div class="vote">
	<a class="up-vote vote-option ${(not empty currentVote and currentVote.value==1) ? "voted" : "" }" data-value="up" data-type="question" data-id="${question.id}">up</a><br />
	<a class="down-vote vote-option ${(not empty currentVote and currentVote.value==-1) ? "voted" : "" }"" data-value="down" data-type="question" data-id="${question.id}">down</a>
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
<c:forEach items="${answers.votes }" var="entry">
	<c:set var="answer" value="${entry.key }" />
	<c:set var="vote" value="${entry.value }" />
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
				<fmt:message key="answer.mark_as_solution" />
			</a>
			<tags:add-a-comment />
		</li>
	</c:if>
	(votes ${answer.voteCount})
<div class="vote">
	<a class="up-vote vote-option ${(not empty vote and vote.value==1) ? "voted" : "" }" data-value="up" data-type="answer" data-id="${answer.id}">up</a><br />
	<a class="down-vote vote-option ${(not empty vote and vote.value==-1) ? "voted" : "" }"" data-value="down" data-type="answer" data-id="${answer.id}">down</a>
</div>
</c:forEach>
</ul>
