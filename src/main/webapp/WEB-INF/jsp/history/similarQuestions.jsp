<div class="history-comparison">
	<div class="history-original">
		<h2 class="history-title page-title title"><fmt:message key="moderation.original_question"/>:</h2>
		<tags:question taggable="${question}"/>
	</div>
	<div class="history-version">
		<tags:historiesSelect histories="${histories}" />
		
		<c:forEach items="${histories}" var="information" varStatus="status">
			<tags:historyForm index="${status.index}" information="${information}">
				<tags:question taggable="${information}"/>	
			</tags:historyForm>
		</c:forEach>
	</div>
</div>
