<div class="history-original">
	<a href="#" class="dropdown-trigger" data-target-id="question-original"><fmt:message key="moderation.show_question" /></a>
	<div id="question-original" class="dropdown-target">
		<tags:question taggable="${answer.question}"/>
	</div>
</div>
<div class="history-comparison">
	<div class="history-original">
		<h2 class="history-title page-title title"><fmt:message key="moderation.current_version"/>:</h2>
		<div class="post-text">${answer.markedDescription}</div>
	</div>
	
	<div class="history-version">
		<tags:historiesSelect histories="${histories}" />
		
		<c:forEach items="${histories}" var="information" varStatus="status">
			<tags:historyForm index="${status.index}" information="${information}">
				<div class="post-text">${information.markedDescription}</div>
			</tags:historyForm>
		</c:forEach>
	</div>
</div>