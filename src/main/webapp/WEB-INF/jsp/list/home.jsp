<div class="submenu">
	<c:if test="${not empty currentUser }">
		<h2 class="title page-title">Bela lista de mensagens pra voce logado</h2>
	</c:if>
	<c:if test="${empty currentUser }">
		<h2 class="title page-title">Bela lista de mensagens</h2>
	</c:if> 
</div>

<ol class="question-list">
	<c:forEach var="question" items="${questions }">
		<tags:list-question-item question="${question}"/>
	</c:forEach>
</ol>