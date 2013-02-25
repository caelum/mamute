<div class="history-original">
	<a href="#" class="dropdown-trigger" data-target-id="question-original"><fmt:message key="moderation.show_original_question" /></a>
	<div id="question-original" class="dropdown-target">
		<tags:question taggable="${answer.question}"/>
	</div>
</div>
<div class="history-comparison">
	<div class="history-original">
		<h2 class="history-title page-title title"><fmt:message key="moderation.original_answer"/>:</h2>
		<div class="post-text">${answer.markedDescription}</div>
	</div>
	
	<div class="history-version">
		<h2 class="history-title page-title title"><fmt:message key="moderation.version"/>:</h2>
		<select class="history-select-version">
			<c:forEach items="${histories}" var="information" varStatus="status">
				<option value="${status.index}">
					${information.author.name} às
					<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
						time="${information.createdAt}"></tags:jodaTime>
				</option>
			</c:forEach>
		</select>
		
		<c:forEach items="${histories}" var="information" varStatus="status">
		
			<form method="post" class="history-form moderate-form ${status.index != 0 ? 'hidden' : ''}" action="${linkTo[HistoryController].publish}${information.answer.typeName}">
			
		
				<div class="post-text">
					${information.markedDescription}
				</div>
				
				<ul class="post-touchs answer-touchs">
					<li class="touch author-touch">
						<tags:completeUser touchText="touch.edited" user="${information.author}" date="${information.createdAt}"/>
					</li>
				</ul>
				
				<h2 class="history-title page-title"><fmt:message key="moderation.comment"/></h2>
				<p class="post-text">
					${information.comment}
				</p>
				<input type="hidden" name="moderatableId" value="${information.answer.id}"/>
				<input type="hidden" name="aprovedInformationId" value="${information.id}"/>
				<input type="hidden" name="aprovedInformationType" value="AnswerInformation"/>
				
				<input type="submit" class="post-submit big-submit" value='<fmt:message key="moderation.accept" />' />
			</form>
		</c:forEach>
	</div>
</div>