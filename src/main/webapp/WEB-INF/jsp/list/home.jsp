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

<table>
	<c:forEach var="question" items="${questions }">
		<tr>
			<td>${question.voteCount}</td>
			<td>${question.author.name } ${question.author.karma }</td>
			<td>${question.answersCount}</td>
			<td>${question.views}</td>
			<td><a
				href="<c:url value="/questions/${question.id }/${question.sluggedTitle }" />">${question.title
					}</a></td>
			<td>tags</td>
			<td><tags:prettyTime time="${question.lastUpdatedAt }" /></td>
			<td>${question.lastTouchedBy.name } ${question.lastTouchedBy.karma }</td>
		</tr>
	</c:forEach>
</table>