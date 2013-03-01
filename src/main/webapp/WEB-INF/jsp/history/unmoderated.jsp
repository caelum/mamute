<div class="subheader subheader-with-tab">
	<h2 class="title page-title"><fmt:message key="moderation"/></h2>
	<div class="tabs">
		<a href="${linkTo[HistoryController].unmoderated['Question']}"><fmt:message key="moderation.questions"/></a>
		<a href="${linkTo[HistoryController].unmoderated['Answer']}"><fmt:message key="moderation.answers"/></a>
	</div>
</div>
<ul class="pending-questions">
	<c:forEach var="entry" items="${pending.entrySet}">
		<li class="pending">
			<c:if test="${type eq 'Question'}">
				<a href="${linkTo[HistoryController].similarQuestions[entry.key.id]}">${entry.key.title}</a>
			</c:if>
			<c:if test="${type eq 'Answer'}">
				<a href="${linkTo[HistoryController].similarAnswers[entry.key.id]}">${entry.key.question.title}</a>
			</c:if>
			<ul>
				<c:forEach var="information" items="${entry.value}">
					<li>
						<tags:jodaTime pattern="DD-MM-YYYY HH:mm" time="${information.createdAt}"></tags:jodaTime>
						 - by ${information.author}
					</li>
				</c:forEach>
			</ul>
		</li>
	</c:forEach>
</ul>