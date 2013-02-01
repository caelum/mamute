<c:if test="${not empty currentUser }">
	Bela lista de mensagens pra voce logado
</c:if>
<c:if test="${empty currentUser }">
	Bela lista de mensagens 
</c:if>

<a href='<c:url value="/question/ask" />'><fmt:message
		key="question.ask" /></a>
<br />
<br />

<table>
	<c:forEach var="question" items="${questions }">
		<tr>
			<td>question.votes</td>
			<td>question.anwers</td>
			<td>${question.views }</td>
			<td><a href="<c:url value="/questions/${question.id }/${question.sluggedTitle }" />">${question.title }</a></td>
			<td>tags</td>
			<td>${question.lastUpdatedAt }</td>
			<td>${question.lastTouchedBy.name } reputacao</td>
		</tr>
	</c:forEach>
</table>