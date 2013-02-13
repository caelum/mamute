<h2 class="history-title"><fmt:message key="moderation.version"/>:</h2>
<select class="question-history-select">
	<option><fmt:message key="moderation.select_version"/></option>
	<c:forEach items="${histories}" var="information" varStatus="status">
		<option value="${status.index}">
			${information.author.name} às
			<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
				time="${information.createdAt}"></tags:jodaTime>
		</option>
	</c:forEach>
</select>

<c:forEach items="${histories}" var="information">

	<form method="post" class="history-form moderate-form hidden" action="${linkTo[HistoryController].publishQuestion[information.question.id][information.id]}">
		
		<h2 class="title question-title">
			${information.title}
		</h2>
		<p>
			${information.markedDescription}
		</p>
		
		
		<ul>
			<li class="tag">${information.tagsAsString}</li>
		</ul>

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
		<input type="submit" class="post-submit big-submit" value='<fmt:message key="moderation.accept" />' />
	</form>
	
</c:forEach>
