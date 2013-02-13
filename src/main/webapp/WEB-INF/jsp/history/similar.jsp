<h2 class="history-title">Versão</h2>
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

	<form method="post" class="history-form post-text hidden moderate-form" action="${linkTo[HistoryController].publish[information.question.id][information.id]}">
		
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
		<br style="clear:both;"/>
		
		<input type="submit" class="post-submit big-submit" value='<fmt:message key="moderation.accept" />' />
	</form>
	
</c:forEach>
