${question.title}
${question.description}

<c:if test="${not empty currentUser}">
	<c:import url="/WEB-INF/jsp/answer/answerForm.jsp" />
</c:if>

<c:forEach items="${question.answers}" var="answer">
<p>${answer.text}</p>
</c:forEach>