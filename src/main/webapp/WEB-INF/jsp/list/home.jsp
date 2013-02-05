<c:if test="${not empty currentUser }">
	Bela lista de mensagens pra voce logado
</c:if>
<c:if test="${empty currentUser }">
	<%@ include file="/WEB-INF/jsp/auth/loginForm.jsp" %>
	Bela lista de mensagens 
</c:if>

<a href='<c:url value="/question/ask" />'><fmt:message
		key="question.ask" /></a>
<br />
<br />

<ol class="question-list">
	<c:forEach var="question" items="${questions }">
		<tags:list-question-item question="${question}"/>
	</c:forEach>
</ol>