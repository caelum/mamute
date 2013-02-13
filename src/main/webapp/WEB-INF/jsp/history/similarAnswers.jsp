<select class="question-history-select">
	<option>selecione a versão</option>
	<c:forEach items="${histories}" var="information" varStatus="status">
		<option value="${status.index}">
			${information.author.name} às
			<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
				time="${information.createdAt}"></tags:jodaTime>
		</option>
	</c:forEach>
</select>

<c:forEach items="${histories}" var="information">

	<div class="forms hidden">
		<form method="post" class="moderate-form" action="${linkTo[HistoryController].publishAnswer[information.answer.id][information.id]}">
			<h2>
				${information.author.name} às
				<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
					time="${information.createdAt}"></tags:jodaTime>
			</h2>
			<p>
				<fmt:message key="newquestion.title"/>:
				${information.answer.question.title}
			</p>
			<p>
				${information.markedDescription}
			</p>
			
			<input type="submit" value='<fmt:message key="moderation.accept" />' />
	
		</form>
	</div>
</c:forEach>
