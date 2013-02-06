<h2 class="title page-title">${question.title}</h2>

<section class="post-area">
<tags:voteFor item="${question}" type="question" vote="${currentVote }"/>
<div class="post-container">
	<p class="question-description" id="question-description-${question.id }">${question.markedDescription}</p>
	<tags:tagsFor question="${question}"/>
	<ul class="post-action-nav nav">
		<li class="nav-item">
			<a class="post-action small" href="<c:url value="/question/edit/${question.id }"/>"><fmt:message key="edit" /></a>
		</li>
	</ul>
	<tags:add-a-comment item="${question}" />
</div>
</section>

<c:if test="${not empty currentUser}">
	<%@ include file="/WEB-INF/jsp/answer/answerForm.jsp"%>
</c:if>

<h2 class="title page-title">
	${question.answersCount} <fmt:message key="question.list.answer.${question.answersCount > 1 ? 'plural' : 'singular' }"/>
</h2>
<ul>
<c:forEach items="${answers.votes}" var="entry">
	<c:set var="answer" value="${entry.key}" />
	<c:set var="vote" value="${entry.value}" />
	<tags:answerWith answer="${answer}" vote="${vote}"/>
</c:forEach>
</ul>
