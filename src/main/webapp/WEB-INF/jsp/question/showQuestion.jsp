<h2 class="title page-title">${question.title}</h2>

<section class="post-area">
<tags:voteFor item="${question }" type="question" vote="${currentVote }"/>
<p class="question-description" id="question-description-${question.id }">${question.markedDescription}</p>
(<a href="<c:url value="/question/edit/${question.id }"/>"><fmt:message key="edit" /></a>)
<tags:add-a-comment item="${question}" />
<ul class="post-tag-list">
</ul>
</section>

<c:if test="${not empty currentUser}">
	<%@ include file="/WEB-INF/jsp/answer/answerForm.jsp"%>
</c:if>

<ul>
<c:forEach items="${answers.votes}" var="entry">
	<c:set var="answer" value="${entry.key}" />
	<c:set var="vote" value="${entry.value}" />
	<tags:answerWith answer="${answer}" vote="${vote}"/>
</c:forEach>
</ul>
