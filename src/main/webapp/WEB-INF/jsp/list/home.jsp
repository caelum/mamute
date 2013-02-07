<section class="first-content">
	<h2 class="title page-title">Bela lista de mensagens pra voce</h2>
	<ol class="question-list">
		<c:forEach var="question" items="${questions }">
			<tags:list-question-item question="${question}"/>
		</c:forEach>
	</ol>
</section>
<aside class="sidebar">
	<h3 class="title section-title"><fmt:message key="tags.recent"/></h3>
	<tags:tagsUsage tagsUsage="${tagsUsage}"/>
</aside>
