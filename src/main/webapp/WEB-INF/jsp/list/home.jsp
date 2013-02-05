<div id="submenu">
	<c:if test="${not empty currentUser }">
		<h1>Bela lista de mensagens pra voce logado</h1>
	</c:if>
	<c:if test="${empty currentUser }">
		<h1>Bela lista de mensagens</h1>
	</c:if> 
</div>

<ol class="question-list">
	<c:forEach var="question" items="${questions }">
		<tags:list-question-item question="${question}"/>
	</c:forEach>
</ol>