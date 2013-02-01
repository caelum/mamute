${question.title}
${question.description}

<c:if test="${not empty currentUser}">
	<c:import url="/WEB-INF/jsp/answer/answerForm.jsp" />
</c:if>