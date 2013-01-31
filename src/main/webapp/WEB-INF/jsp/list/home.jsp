<c:if test="${not empty currentUser }">
	Bela lista de mensagens pra voce logado
</c:if>
<c:if test="${empty currentUser }">
	Bela lista de mensagens 
</c:if>

<a href ='<c:url value="/question/ask" />'><fmt:message key="question.ask" /></a>