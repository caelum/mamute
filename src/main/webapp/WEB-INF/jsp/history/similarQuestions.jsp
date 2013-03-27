<fmt:message key="metas.moderate_question.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<div class="history-comparison">
	<div class="history-current">
		<h2 class="history-title page-title title"><fmt:message key="moderation.current_version"/>:</h2>
		<tags:question taggable="${question}"/>
	</div>
	<div class="history-edited">
		<tags:historiesSelect histories="${histories}" />
		
		<c:forEach items="${histories}" var="information" varStatus="status">
			<tags:historyForm index="${status.index}" information="${information}">
				<tags:question taggable="${information}"/>	
			</tags:historyForm>
		</c:forEach>
	</div>
</div>
