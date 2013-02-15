
<div class="history-comparison">
	<div class="history-original">
		<h2 class="history-title page-title title"><fmt:message key="moderation.original_question"/>:</h2>
		<tags:question taggable="${question}"/>
	</div>
	<div class="history-version">
		<h2 class="history-title page-title title"><fmt:message key="moderation.version"/>:</h2>
		<select class="history-select-version">
			<option><fmt:message key="moderation.select_version"/></option>
			<c:forEach items="${histories}" var="information" varStatus="status">
				<option ${status.index == 0 ? 'selected' : '' } value="${status.index}">
					${information.author.name} às
					<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
						time="${information.createdAt}"></tags:jodaTime>
				</option>
			</c:forEach>
		</select>
		<c:forEach items="${histories}" var="information" varStatus="status">
			<form method="post" class="history-form moderate-form ${status.index != 0 ? 'hidden' : ''}" action="${linkTo[HistoryController].publish}${information.question.typeName}">
				
				<tags:question taggable="${information}"/>	
				
				<ul class="post-touchs">
					<li class="touch author-touch">
						<tags:completeUser user="${information.author}" date="${information.createdAt}"/>
					</li>
				</ul>
				<br class="clear"/>
		
				<c:if test="${not empty information.comment}">
					<h2 class="history-title page-title"><fmt:message key="moderation.comment"/></h2>
					<p class="post-text">
						${information.comment}
					</p>
				</c:if>
		
				<input type="hidden" name="moderatableId" value="${information.question.id}"/>
				<input type="hidden" name="aprovedInformationId" value="${information.id}"/>
				<input type="hidden" name="aprovedInformationType" value="QuestionInformation"/>
				<input type="submit" class="post-submit big-submit" value='<fmt:message key="moderation.accept" />' />
			</form>
		</c:forEach>
	</div>
</div>
